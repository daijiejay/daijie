package org.daijie.core.util.encrypt;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

/**
 * RSA数据签名及数据加密
 */
public class RSAUtil {
	private static byte[] pub_key = null;
	private static byte[] pri_key = null;

	// 数字签名，密钥算法
	private static final String RSA_KEY_ALGORITHM = "RSA";

	// 数字签名签名/验证算法
	private static final String SIGNATURE_ALGORITHM = "MD5withRSA";

	// RSA密钥长度，RSA算法的默认密钥长度是1024密钥长度必须是64的倍数，在512到65536位之间
	private static final int KEY_SIZE = 1024;

	public static void init(){
		try {
			initKey();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void set(byte[] pub_key, byte[] pri_key){
		RSAUtil.pub_key = pub_key;
		RSAUtil.pri_key = pri_key;
		if (pub_key == null || pri_key == null)
			try {
				initKey();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	public static void set(String pub_key, String pri_key){
		RSAUtil.pri_key = Base64.decodeBase64(pub_key);
		RSAUtil.pub_key = Base64.decodeBase64(pri_key);
		if (pub_key == null || pri_key == null)
			try {
				initKey();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * 数字签名生成密钥 第一步生成密钥对,如果已经生成过,本过程就可以跳过
	 */
	private static void initKey() throws Exception {
		KeyPairGenerator keygen = KeyPairGenerator
				.getInstance(RSA_KEY_ALGORITHM);
		SecureRandom secrand = new SecureRandom();
		secrand.setSeed("initSeed".getBytes());// 初始化随机产生器
		keygen.initialize(KEY_SIZE, secrand); // 初始化密钥生成器
		KeyPair keys = keygen.genKeyPair();
		pub_key = keys.getPublic().getEncoded();
		pri_key = keys.getPrivate().getEncoded();
	}

	/**
	 * RSA签名
	 * 
	 * @param data待签名数据
	 * @return byte[] 数字签名
	 * */
	public static String sign(byte[] data) throws Exception {
		// 取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(pri_key);
		KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
		// 生成私钥
		PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
		// 实例化Signature
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		// 初始化Signature
		signature.initSign(priKey);
		// 更新
		signature.update(data);

		return Base64.encodeBase64String(signature.sign());
	}

	/**
	 * RSA校验数字签名
	 * 
	 * @param data
	 *            待校验数据
	 * @param sign
	 *            数字签名
	 * @return boolean 校验成功返回true，失败返回false
	 * */
	public static boolean verify(byte[] data, byte[] sign) throws Exception {
		// 转换公钥材料
		// 实例化密钥工厂
		KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
		// 初始化公钥
		// 密钥材料转换
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(pub_key);
		// 产生公钥
		PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
		// 实例化Signature
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		// 初始化Signature
		signature.initVerify(pubKey);
		// 更新
		signature.update(data);
		// 验证
		return signature.verify(sign);
	}

	/**
	 * 用公钥加密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPubKey(byte[] data) throws Exception {
		// 取得公钥
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(pub_key);
		KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
		PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
		// 对数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(data);
	}

	/**
	 * 用公钥加密
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String encryptByPubKey(String data) throws Exception {
		// 私匙加密
		byte[] enSign = encryptByPubKey(data.getBytes());
		return Base64.encodeBase64String(enSign);
	}

	/**
	 * 用私钥加密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] encryptByPriKey(byte[] data) throws Exception {
		// 取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(pri_key);
		KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
		PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		// 对数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}

	/**
	 * 用私钥加密
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String encryptByPriKey(String data) throws Exception {
		// 私匙加密
		byte[] enSign = encryptByPriKey(data.getBytes());
		return Base64.encodeBase64String(enSign);
	}

	/**
	 * 用公钥解密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPubKey(byte[] data) throws Exception {
		// 取得公钥
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(pub_key);
		KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
		PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
		// 对数据解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		return cipher.doFinal(data);
	}

	/**
	 * 用公钥解密
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String decryptByPubKey(String data) throws Exception {
		// 公匙解密
		byte[] design = decryptByPubKey(Base64.decodeBase64(data));
		return new String(design);
	}

	/**
	 * 用私钥解密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] decryptByPriKey(byte[] data) throws Exception {
		// 取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(pri_key);
		KeyFactory keyFactory = KeyFactory.getInstance(RSA_KEY_ALGORITHM);
		PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		// 对数据解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}

	/**
	 * 用私钥解密
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String decryptByPriKey(String data) throws Exception {
		// 公匙解密
		byte[] design = decryptByPriKey(Base64.decodeBase64(data));
		return new String(design);
	}
	
	/**
	 * 获取公匙
	 * @return
	 */
	public static String getPubKey(){
		return Base64.encodeBase64String(pub_key);
	}
	
	/**
	 * 获取私匙
	 * @return
	 */
	public static String getPriKey(){
		return Base64.encodeBase64String(pri_key);
	}
}