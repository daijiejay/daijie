package org.daijie.shiro.security.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.daijie.core.controller.ApiController;
import org.daijie.core.controller.enums.ResultCode;
import org.daijie.core.factory.specific.ModelResultInitialFactory.Result;
import org.daijie.core.httpResult.ApiResult;
import org.daijie.core.httpResult.ModelResult;
import org.daijie.core.util.encrypt.PasswordUtil;
import org.daijie.core.util.encrypt.RSAUtil;
import org.daijie.mybatis.model.User;
import org.daijie.shiro.authc.AuthorizationToken;
import org.daijie.shiro.authc.ShiroConstants;
import org.daijie.shiro.authc.UserToken;
import org.daijie.shiro.security.cloud.UserCloud;
import org.daijie.shiro.session.ShiroRedisSession.Redis;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(description="用户注册")
public class RegisterController extends ApiController<UserCloud, Exception> {

	@ApiOperation(notes = "注册", value = "注册")
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ModelResult<Object> register(@RequestParam String username, @RequestParam String password) throws Exception{
		//公钥传给客户端
		String publicKey = (String) Redis.getAttribute(ShiroConstants.RSA_PUBLIC_KEY + Redis.getSession().getId());
		//客户端调用登录接口时进行公钥加密后传参
		String pubEncryptPassword = RSAUtil.encryptByPubKey(password);

		User user = service.getUser(username);
		if(user != null){
			return Result.build("该账号已注册", ApiResult.SUCCESS, ResultCode.CODE_200);
		}
		user = new User();
		String salt = PasswordUtil.generateSalt();
		String saltPassword = PasswordUtil.generatePassword(password, salt.getBytes());
		user.setSalt(salt);
		user.setUserName(username);
		user.setPassword(saltPassword);
		boolean result = service.addUser(user);
		if(!result){
			return Result.build("注册失败", ApiResult.SUCCESS, ResultCode.CODE_200);
		}
		RSAUtil.set(publicKey, null);
		UserToken userToken = new UserToken();
		userToken.setAuthc(user);
		AuthorizationToken token = new AuthorizationToken(username, 
				saltPassword, 
				pubEncryptPassword, salt, "user", userToken);
		token.setRememberMe(true);  
		Subject subject = SecurityUtils.getSubject();
		subject.login(token);
		subject.isAuthenticated();
		return Result.build("注册成功", ApiResult.SUCCESS, ResultCode.CODE_200);
	}
}
