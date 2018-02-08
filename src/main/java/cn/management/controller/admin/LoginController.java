package cn.management.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
    
}
