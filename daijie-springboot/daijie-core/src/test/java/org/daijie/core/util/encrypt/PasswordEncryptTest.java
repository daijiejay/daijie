package org.daijie.core.util.encrypt;

import org.apache.commons.net.util.Base64;
import org.junit.Test;

public class PasswordEncryptTest {

	@Test
	public void test1() throws Exception{
		RSAUtil.init();
		String password = "1234567";
		System.out.println("原密码："+password);
		// 公匙加密
		String pubKeyEnStr = RSAUtil.encryptByPubKey(password);
		System.out.println("公匙加密:" + pubKeyEnStr);
		String priKeyDeStr = RSAUtil.decryptByPriKey(pubKeyEnStr);
		// 私匙解密
		System.out.println("私匙解密:" + priKeyDeStr);
		// 私匙加密
		String pubKeyDeStr = RSAUtil.encryptByPriKey(password);
		System.out.println("私匙加密:" + pubKeyEnStr);
		String priKeyEnStr = RSAUtil.decryptByPubKey(pubKeyDeStr);
		// 公匙解密
		System.out.println("公匙解密:" + priKeyEnStr);
		// 数据签名
		String str1 = "before";
		String str2 = "after";
		String sign = RSAUtil.sign(str1.getBytes());
		System.out.println("数据签名:" + sign);
		boolean vflag1 = RSAUtil.verify(str1.getBytes(), Base64.decodeBase64(sign));
		System.out.println("数据验证结果1:" + vflag1);
		boolean vflag2 = RSAUtil.verify(str2.getBytes(), Base64.decodeBase64(sign));
		System.out.println("数据验证结果2:" + vflag2);
		//生成盐
		String salt = PasswordUtil.generateSalt();
		System.out.println("盐："+salt);
		System.out.println("盐加密："+PasswordUtil.generatePassword(priKeyDeStr, salt.getBytes()));
		
	}
}
