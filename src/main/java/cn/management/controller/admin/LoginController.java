package cn.management.controller.admin;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.management.domain.admin.AdminUser;
import cn.management.service.AdminUserService;

/**
 * 用户登录注销控制器
 */
@Controller
@RequestMapping("/admin")
public class LoginController {
	
	/**
	 * 登录页面
	 * @return
	 */
    @RequestMapping("/login")
    public String login() {
    	return "admin/login";
    }
    
    @RequestMapping("/doLogin")
    public String doLogin(HttpServletRequest request) {
    	String loginName = request.getParameter("loginName").trim();
    	String password = request.getParameter("password").trim();
    	UsernamePasswordToken token = new UsernamePasswordToken(loginName, password);
    	Subject subject = SecurityUtils.getSubject();
        try {
	    	subject.login(token);
	        //登录成功时，就从Shiro中取出用户的登录信息
	        AdminUser user = (AdminUser)subject.getPrincipal();
	    	request.getSession().setAttribute(AdminUserService.LOGIN_SESSION_KEY, user.getId());
	    	return "index";
	    } catch (AuthenticationException e) {
	    	return "admin/login";
	    }
    }
    
}
