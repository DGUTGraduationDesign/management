package cn.management.controller.business;

import cn.management.controller.BaseController;
import cn.management.domain.business.BusinessContract;
import cn.management.enums.DeleteTypeEnum;
import cn.management.enums.ResultEnum;
import cn.management.exception.SysException;
import cn.management.service.admin.AdminUserService;
import cn.management.service.business.BusinessContractService;
import cn.management.util.Commons;
import cn.management.util.DownloadUtil;
import cn.management.util.Result;
import com.alibaba.fastjson.JSON;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 合同控制器
 */
@Controller
@RequestMapping("business/contract")
public class BusinessContractController extends BaseController<BusinessContractService, BusinessContract> {

    /**
     * 条件查询合同列表
     * @param models
     * @return
     */
    @RequestMapping("/index")
    @RequiresPermissions("businessContract:list")
    @ResponseBody
    public Result index(@RequestBody Map<String, Object> models) {
        BusinessContract businessContract = JSON.parseObject((String)models.get("businessContract"), BusinessContract.class);
        Integer page = (Integer) models.get("page");
        Example example = new Example(BusinessContract.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(businessContract.getContractName())) {
            criteria.andLike("contractName", "%" + businessContract.getContractName() + "%");
        }
        criteria.andEqualTo("delFlag", DeleteTypeEnum.DELETED_FALSE.getVal());
        setExample(example);
        return list(page);
    }

    /**
     * 上传报告
     * @param businessContract
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping("/upload")
    @RequiresPermissions("businessContract:upload")
    @ResponseBody
    public Result upload(BusinessContract businessContract, HttpServletRequest request) throws IOException, SysException {
        MultipartFile file = businessContract.getFile();
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
        //生成新的文件名
        String prefix = UUID.randomUUID().toString().replace("-", "");
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String newFileName = date + "-" + prefix + suffix;
        //创建jersey服务器，进行跨服务器上传
        Client client = new Client();
        //把文件关联到远程服务器
        WebResource resource = client.resource(Commons.FILE_HOST + Commons.CONTRACTS_PATH + "/" + newFileName);
        //上传
        resource.put(String.class, file.getBytes());
        //更新到数据库
        //原文件名
        businessContract.setFileName(fileName);
        //文件下载地址
        businessContract.setFilePath(Commons.CONTRACTS_PATH + "/" + newFileName);
        //上传者
        Integer loginId = (Integer) request.getSession().getAttribute(AdminUserService.LOGIN_SESSION_KEY);
        businessContract.setCreateBy(loginId);
        if (null == service.doAdd(businessContract)) {
            return new Result(ResultEnum.FAIL);
        } else {
            return new Result(ResultEnum.SUCCESS);
        }
    }


    /**
     * 下载报告
     * @param contractIds
     * @param request
     * @return
     */
    @RequestMapping("/download")
    @RequiresPermissions("businessContract:download")
    public String download(String contractIds, HttpServletRequest request, HttpServletResponse response) throws IOException, SysException {
        List<BusinessContract> list = new ArrayList<BusinessContract>();
        String[] rId = contractIds.split(",");
        for (String contractId : rId) {
            BusinessContract contract = service.getItemById(Integer.valueOf(contractId));
            if (null != contract) {
                list.add(contract);
            }
        }
        if (null == list || 0 == list.size()) {
            throw new SysException("文件不存在.");
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String fileName = "";
        if (1 == list.size()) {
            BusinessContract businessContract = list.get(0);
            fileName = businessContract.getFileName();
            String path = Commons.FILE_HOST + businessContract.getFilePath();
            //创建jersey服务器，进行跨服务器下载
            Client client = new Client();
            //把文件关联到远程服务器
            WebResource resource = client.resource(path);
            //下载
            File file = resource.get(File.class);
            InputStream inputStream = new FileInputStream(file);
            //内存中的缓存区
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            int date = -1;
            while ((date = bis.read()) != -1) {
                baos.write(date);
            }
            inputStream.close();
        } else {
            fileName = Commons.ZIP_NAME;
            String zipPath = request.getSession().getServletContext().getRealPath(Commons.ZIP_PATH + "/" + Commons.ZIP_NAME);
            //如果不存在则创建一个
            File zipFile = new File(zipPath);
            if (!zipFile.exists()) {
                zipFile.createNewFile();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(zipPath));
            ZipOutputStream zos = new ZipOutputStream(bos);
            ZipEntry ze = null;
            //将所有需要下载的pdf文件都写入临时zip文件
            for (BusinessContract businessContract : list) {
                //创建jersey服务器，进行跨服务器读取文件
                Client client = new Client();
                String path = Commons.FILE_HOST + businessContract.getFilePath();
                WebResource resource = client.resource(path);
                File catalog = resource.get(File.class);
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(catalog));
                ze = new ZipEntry(businessContract.getFileName());
                zos.putNextEntry(ze);
                int s = -1;
                while ((s = bis.read()) != -1) {
                    zos.write(s);
                }
                bis.close();
            }
            zos.flush();
            zos.close();
            //将zip输出到baos
            InputStream inputStream = new FileInputStream(zipPath);
            //内存中的缓存区
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            int date = -1;
            while ((date = bis.read()) != -1) {
                baos.write(date);
            }
            inputStream.close();
            //删除临时文件
            zipFile.delete();
        }
        //文件下载
        DownloadUtil.download(baos, request, response, fileName);
        return null;
    }


    /**
     * 批量删除报告
     * @param models
     * @return
     */
    @RequestMapping("/delete")
    @RequiresPermissions("businessContract:delete")
    @ResponseBody
    public Result delete(@RequestBody Map<String, Object> models, HttpServletRequest request) {
        String ids = (String) models.get("ids");
        if (!StringUtils.isNotBlank(ids)) {
            return new Result(ResultEnum.DATA_ERROR.getCode(), "操作失败，id不能为空");
        }
        //删除文件
        String[] idArray = ids.split(",");
        for (String id : idArray) {
            BusinessContract businessContract = service.getItemById(Integer.valueOf(id));
            String path = request.getSession().getServletContext().getRealPath("/") + businessContract.getFilePath();
            File file = new File(path);
            if (file.exists() && file.isFile()) {
                file.delete();
            }
        }
        //更新删除字段
        if (service.logicalDelete(ids)) {
            return new Result(ResultEnum.SUCCESS);
        } else {
            return new Result(ResultEnum.FAIL);
        }
    }

}
