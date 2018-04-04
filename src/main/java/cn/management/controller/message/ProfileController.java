package cn.management.controller.message;

import cn.management.domain.admin.AdminUser;
import cn.management.domain.admin.dto.EditProfileDto;
import cn.management.enums.ResultEnum;
import cn.management.exception.SysException;
import cn.management.service.admin.AdminUserService;
import cn.management.util.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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
        if (adminUserService.update(user)) {
            return new Result(ResultEnum.SUCCESS);
        } else {
            return new Result(ResultEnum.FAIL);
        }
    }

}
