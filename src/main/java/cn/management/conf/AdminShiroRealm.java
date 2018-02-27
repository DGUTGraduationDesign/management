package cn.management.conf;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cn.management.domain.admin.AdminRole;
import cn.management.domain.admin.AdminUser;
import cn.management.service.admin.AdminUserService;

/**
 * shiro权限认证
 */
public class AdminShiroRealm extends AuthorizingRealm {

	static Logger logger = LoggerFactory.getLogger(AdminShiroRealm.class);

	@Autowired
	AdminUserService adminUserService;

	/**
	 * 用户授权
	 * @param principals
	 * @return
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		AdminUser adminUser = (AdminUser) principals.getPrimaryPrincipal();
		if (adminUser.getRoleList() != null) {
			for (AdminRole role : adminUser.getRoleList()) {
				role.getPermissionList().forEach(key -> authorizationInfo.addStringPermission(key));
			}
		}
		return authorizationInfo;
	}

	/**
	 * 身份认证，验证用户输入的账号和密码是否正确。
	 * @param authcToken
	 * @return
	 * @throws AuthenticationException
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
			throws AuthenticationException {

		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		//用户登陆认证，这里可以根据实际情况做缓存
		AdminUser loginUser = adminUserService.login(token.getUsername());
		if(null == loginUser){
			return null;
		}

		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
				loginUser, //用户名
				loginUser.getPassword(), //密码
				//ByteSource.Util.bytes(loginUser.getCredentialsSalt()),//salt=username+salt
				getName()  //realm name
		);
		return authenticationInfo;
	}
}
