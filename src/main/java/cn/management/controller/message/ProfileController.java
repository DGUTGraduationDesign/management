package cn.management.controller.message;

import cn.management.domain.admin.AdminUser;
import cn.management.domain.admin.dto.EditProfileDto;
import cn.management.enums.ResultEnum;
import cn.management.exception.SysException;
import cn.management.service.admin.AdminUserService;
import cn.management.util.Commons;
import cn.management.util.MD5Util;
import cn.management.util.Result;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * 个人信息控制器
 */
@Controller
@RequestMapping("message/profile")
public class ProfileController {

    @Autowired
    private AdminUserService adminUserService;

    /**
     * 查看个人信息
     * @param request
     * @return
     * @throws SysException
     */
    @RequestMapping("findProfile")
    @RequiresPermissions("messageProfile:find")
    @ResponseBody
    public Result findProfile(HttpServletRequest request) throws SysException {
        Integer loginId = (Integer) request.getSession().getAttribute(AdminUserService.LOGIN_SESSION_KEY);
        if (null == loginId) {
            throw new SysException("请先登录.");
        }
        AdminUser user = adminUserService.getItemById(loginId);
        return new Result(ResultEnum.SUCCESS, user);
    }

    /**
     * 修改个人信息
     * @param request
     * @return
     */
    @RequestMapping("editProfile")
    @RequiresPermissions("messageProfile:edit")
    @ResponseBody
    public Result editProfile(@RequestBody EditProfileDto dto, HttpServletRequest request) throws SysException {
        Integer loginId = (Integer) request.getSession().getAttribute(AdminUserService.LOGIN_SESSION_KEY);
        if (null == loginId) {
            throw new SysException("请先登录.");
        }
        AdminUser user = new AdminUser();
        user.setId(loginId);
        EditProfileDto.dtoToEntity(dto, user);
        //MD5加密
        user.setPassword(MD5Util.getMD5Value(user.getPassword()));
        if (adminUserService.update(user)) {
            return new Result(ResultEnum.SUCCESS);
        } else {
            return new Result(ResultEnum.FAIL);
        }
    }

    /**
     * 上传头像
     * @param file
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping("/upload")
    @RequiresPermissions("messageProfile:edit")
    @ResponseBody
    public Result upload(MultipartFile file, HttpServletRequest request) throws IOException {
        Integer loginId = (Integer) request.getSession().getAttribute(AdminUserService.LOGIN_SESSION_KEY);
        //判断文件大小
        long fileSize = file.getSize();
        if (file == null || fileSize == 0) {
            return new Result(ResultEnum.DATA_ERROR, "操作失败！请选择要上传的文件.");
        }
        if (fileSize > (5 * 1024 * 1024)) {
            return new Result(ResultEnum.DATA_ERROR, "操作失败！图片大小不能超过5M.");
        }
        //获取文件完整名
        String fileName = file.getOriginalFilename();
        //文件后缀名
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        if (!(".jpg".equals(suffix) || ".jpeg".equals(suffix) || ".png".equals(suffix))) {
            return new Result(ResultEnum.DATA_ERROR, "操作失败！请上传jpg、jpeg或png类型的文件.");
        }
        //生成新的文件名
        String prefix = UUID.randomUUID().toString().replace("-", "");
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String newFileName = date + "-" + prefix + suffix;
        //创建jersey服务器，进行跨服务器上传
        Client client = new Client();
        //把文件关联到远程服务器
        WebResource resource = client.resource(Commons.FILE_HOST + Commons.USER_PICTURE_PATH + "/" + newFileName);
        //上传
        resource.put(String.class, file.getBytes());
        //更新到数据库
        AdminUser adminUser = new AdminUser();
        adminUser.setId(loginId);
        adminUser.setUploadPath(Commons.FILE_HOST + Commons.USER_PICTURE_PATH + "/" + newFileName);
        adminUser.setUpdateTime(new Date());
        if (adminUserService.update(adminUser)) {
            return new Result(ResultEnum.SUCCESS);
        } else {
            return new Result(ResultEnum.FAIL);
        }
    }

}
