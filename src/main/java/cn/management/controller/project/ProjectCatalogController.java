package cn.management.controller.project;

import cn.management.controller.BaseController;
import cn.management.domain.project.ProjectCatalog;
import cn.management.enums.CatalogTypeEnum;
import cn.management.enums.ResultEnum;
import cn.management.exception.SysException;
import cn.management.service.admin.AdminUserService;
import cn.management.service.project.ProjectCatalogService;
import cn.management.util.Commons;
import cn.management.util.DownloadUtil;
import cn.management.util.Result;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 项目网盘控制器
 */
@RequestMapping("project/catalog")
@Controller
public class ProjectCatalogController extends BaseController<ProjectCatalogService, ProjectCatalog> {

    /**
     * 查询网盘列表
     * @param request
     * @return
     */
    @RequestMapping("/index")
    @RequiresPermissions("projectCatalog:list")
    @ResponseBody
    public Result index(@RequestBody Map<String, Object> models, HttpServletRequest request) {
        Integer parentId = (Integer) models.get("catalogId");
        Integer loginId = (Integer) request.getSession().getAttribute(AdminUserService.LOGIN_SESSION_KEY);
        List<ProjectCatalog> list = service.getCatalogsByIds(loginId, parentId);
        if (list == null || list.size() == 0) {
            return new Result(ResultEnum.NO_RECORDS);
        } else {
            return new Result(ResultEnum.SUCCESS, list);
        }
    }

    /**
     * 根据id查询目录详细信息
     * @param models
     * @return
     */
    @RequestMapping("/findById")
    @RequiresPermissions("projectCatalog:list")
    @ResponseBody
    public Result findById(@RequestBody Map<String, Object> models) {
        Integer catalogId = (Integer) models.get("catalogId");
        ProjectCatalog projectCatalog = service.getItemById(catalogId);
        if (null == projectCatalog) {
            return new Result(ResultEnum.NO_RECORDS);
        } else {
            return new Result(ResultEnum.SUCCESS, projectCatalog);
        }
    }

    /**
     * 新建目录
     * @param projectCatalog
     * @param request
     * @return
     * @throws SysException
     */
    @RequestMapping("/add")
    @RequiresPermissions("projectCatalog:add")
    @ResponseBody
    public Result add(@RequestBody ProjectCatalog projectCatalog, HttpServletRequest request) throws SysException {
        Integer loginUserId = (Integer) request.getSession().getAttribute(AdminUserService.LOGIN_SESSION_KEY);
        projectCatalog.setCreateBy(loginUserId);
        projectCatalog.setCreateTime(new Date());
        projectCatalog.setUpdateTime(new Date());
        if (null == service.doAdd(projectCatalog, loginUserId)) {
            return new Result(ResultEnum.FAIL);
        } else {
            return new Result(ResultEnum.SUCCESS);
        }
    }

    /**
     * 上传文件
     * @param projectCatalog
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping("/upload")
    @RequiresPermissions("projectCatalog:upload")
    @ResponseBody
    public Result upload(ProjectCatalog projectCatalog, HttpServletRequest request) throws IOException, SysException {
        MultipartFile file = projectCatalog.getFile();
        //判断文件大小
        long fileSize = file.getSize();
        if (file == null || fileSize == 0) {
            return new Result(ResultEnum.DATA_ERROR, "操作失败！请选择要上传的文件！");
        }
        //设置文件大小
        int size = (int) (fileSize/1024);
        if (0 != size) {
            projectCatalog.setFileSize(size);
        } else {
            projectCatalog.setFileSize(1);
        }
        //获取文件完整名
        String fileName = file.getOriginalFilename();
        //文件后缀名
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        //设置文件类型
        if ("doc".equals(suffix) || "docx".equals(suffix)) {
            projectCatalog.setFileType(CatalogTypeEnum.DOC.getValue());
        } else if (".xls".equals(suffix) || ".xlsx".equals(suffix)) {
            projectCatalog.setFileType(CatalogTypeEnum.XLS.getValue());
        } else if (".ppt".equals(suffix) || ".pptx".equals(suffix)) {
            projectCatalog.setFileType(CatalogTypeEnum.PPT.getValue());
        } else if (".txt".equals(suffix)) {
            projectCatalog.setFileType(CatalogTypeEnum.TXT.getValue());
        } else if (".zip".equals(suffix) || ".rar".equals(suffix) ) {
            projectCatalog.setFileType(CatalogTypeEnum.ZIP.getValue());
        } else {
            projectCatalog.setFileType(CatalogTypeEnum.OTHER.getValue());
        }
        //生成新的文件名
        String prefix = UUID.randomUUID().toString().replace("-", "");
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String newFileName = date + "-" + prefix + suffix;
        //创建jersey服务器，进行跨服务器上传
        Client client = new Client();
        //把文件关联到远程服务器
        WebResource resource = client.resource(Commons.FILE_HOST + Commons.PROJECTS_PATH + "/" + newFileName);
        //上传
        resource.put(String.class, file.getBytes());
        //更新到数据库
        //原文件名
        projectCatalog.setName(fileName);
        //文件下载地址
        projectCatalog.setFilePath(Commons.PROJECTS_PATH + "/" + newFileName);
        //上传者
        Integer loginId = (Integer) request.getSession().getAttribute(AdminUserService.LOGIN_SESSION_KEY);
        projectCatalog.setCreateBy(loginId);
        projectCatalog.setCreateTime(new Date());
        projectCatalog.setUpdateTime(new Date());
        if (null == service.doAdd(projectCatalog, loginId)) {
            return new Result(ResultEnum.FAIL);
        } else {
            return new Result(ResultEnum.SUCCESS);
        }
    }

    /**
     * 修改目录
     * @param projectCatalog
     * @param request
     * @return
     * @throws SysException
     */
    @RequestMapping("/edit")
    @RequiresPermissions("projectCatalog:edit")
    @ResponseBody
    public Result edit(@RequestBody ProjectCatalog projectCatalog, HttpServletRequest request) throws SysException {
        Integer loginId = (Integer) request.getSession().getAttribute(AdminUserService.LOGIN_SESSION_KEY);
        projectCatalog.setUpdateTime(new Date());
        if (service.doUpdate(projectCatalog, loginId)) {
            return new Result(ResultEnum.SUCCESS);
        } else {
            return new Result(ResultEnum.FAIL);
        }
    }

    /**
     * 下载文件
     * @param models
     * @param request
     * @return
     */
    @RequestMapping("/download")
    @RequiresPermissions("projectCatalog:download")
    public String download(@RequestBody Map<String, Object> models, HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException, SysException {
        Integer loginId = (Integer) request.getSession().getAttribute(AdminUserService.LOGIN_SESSION_KEY);
        Integer fileId = (Integer) models.get("fileId");
        //判断文件是否存在
        ProjectCatalog projectCatalog = service.getByLoginIdAndCId(loginId, fileId);
        if (null == projectCatalog) {
            throw new SysException("文件目录不存在.");
        }
        String path = Commons.FILE_HOST + projectCatalog.getFilePath();
        //创建jersey服务器，进行跨服务器下载
        Client client = new Client();
        //把文件关联到远程服务器
        WebResource resource = client.resource(path);
        //下载
        File file = resource.get(File.class);
        InputStream inputStream = new FileInputStream(file);
        //内存中的缓存区
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int date = -1;
        while ((date = bis.read()) != -1) {
            baos.write(date);
        }
        inputStream.close();
        //文件下载
        DownloadUtil.download(baos, request, response, projectCatalog.getName());
        return null;
    }

    /**
     * 批量删除网盘目录
     * @param models
     * @param request
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("projectCatalog:delete")
    @ResponseBody
    public Result delete(@RequestBody Map<String, Object> models, HttpServletRequest request) {
        Integer loginUserId = (Integer) request.getSession().getAttribute(AdminUserService.LOGIN_SESSION_KEY);
        String ids = (String) models.get("ids");
        if (!StringUtils.isNotBlank(ids)) {
            return new Result(ResultEnum.DATA_ERROR.getCode(), "操作失败，id不能为空");
        }
        if (service.doDelete(ids, loginUserId)) {
            return new Result(ResultEnum.SUCCESS);
        } else {
            return new Result(ResultEnum.FAIL);
        }
    }

}
