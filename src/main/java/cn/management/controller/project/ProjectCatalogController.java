package cn.management.controller.project;

import cn.management.controller.BaseController;
import cn.management.domain.project.ProjectCatalog;
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
        if (null == service.addSelectiveMapper(projectCatalog)) {
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
    public Result upload(ProjectCatalog projectCatalog, HttpServletRequest request) throws IOException {
        MultipartFile file = projectCatalog.getFile();
        //判断文件大小
        long fileSize = file.getSize();
        if (file == null || fileSize == 0) {
            return new Result(ResultEnum.DATA_ERROR, "操作失败！请选择要上传的文件！");
        }
        if (fileSize > (10 * 1024 * 1024)) {
            return new Result(ResultEnum.DATA_ERROR, "操作失败！文件大小不能超过10M！");
        }
        //获取文件完整名
        String fileName = file.getOriginalFilename();
        //文件后缀名
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        //设置文件类型
        if ("doc".equals(suffix) || "docx".equals(suffix)) {
            projectCatalog.setFileType(1);
        } else if (".xls".equals(suffix) || ".xlsx".equals(suffix)) {
            projectCatalog.setFileType(2);
        } else if (".ppt".equals(suffix) || ".pptx".equals(suffix)) {
            projectCatalog.setFileType(3);
        } else if (".txt".equals(suffix)) {
            projectCatalog.setFileType(4);
        } else if (".txt".equals(suffix)) {
            projectCatalog.setFileType(5);
        } else if (".zip".equals(suffix) || ".rar".equals(suffix) ) {
            projectCatalog.setFileType(6);
        } else {
            projectCatalog.setFileType(7);
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
        if (null == service.addSelectiveMapper(projectCatalog)) {
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
        projectCatalog.setUpdateTime(new Date());
        if (service.update(projectCatalog)) {
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
    public String download(@RequestBody Map<String, Object> models, HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {
        Integer fileId = (Integer) models.get("fileId");
        ProjectCatalog projectCatalog = service.getItemById(fileId);
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
        if (service.logicalDelete(ids)) {
            return new Result(ResultEnum.SUCCESS);
        } else {
            return new Result(ResultEnum.FAIL);
        }
    }

}
