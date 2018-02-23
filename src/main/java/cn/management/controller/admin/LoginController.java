package cn.management.controller.admin;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.management.domain.admin.AdminUser;
import cn.management.enums.ResultEnum;
import cn.management.service.AdminUserService;
import cn.management.util.Result;

/**
 * 用户登录注销控制器
 */
@Controller
@RequestMapping("/admin")
public class LoginController {
	
	static Logger logger = LoggerFactory.getLogger(LoginController.class);
    
    /**
     * 登录页面
     * @return
     */
    @RequestMapping("/login")
    public String login() {
        return "admin/login";
    }
    
    /**
     * 用户登录
     * @param request
     * @return
     */
    @RequestMapping("/doLogin")
    @ResponseBody
    public Result doLogin(@RequestBody Map<String, Object> models, HttpServletRequest request) {
    	String loginName = (String) models.get("loginName");
    	String password = (String) models.get("password");
        if (loginName == null || "".equals(loginName)) {
            return new Result(ResultEnum.DATA_ERROR.getCode(), "用户名不能为空！");
        }
        UsernamePasswordToken token = new UsernamePasswordToken(loginName, password);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            //登录成功时，就从Shiro中取出用户的登录信息
            AdminUser user = (AdminUser)subject.getPrincipal();
            request.getSession().setAttribute(AdminUserService.LOGIN_SESSION_KEY, user.getId());
            return new Result(ResultEnum.SUCCESS.getCode(), "登录成功！");
        } catch (AuthenticationException e) {
            return new Result(ResultEnum.FAIL.getCode(), "用户名或密码错误！");
        }
    }
    
}
