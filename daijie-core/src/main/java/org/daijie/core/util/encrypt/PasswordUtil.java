package org.daijie.core.util.encrypt;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author daijie
 * @date 2017年6月5日
 * 密码对称加密算法工具类
 * 
 */
public class PasswordUtil {
	private static class SecretKeyFactorySingleTon {
		private static SecretKeyFactory skf = null;
		static{
			try {
				skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static class SecureRandomSingleTon{
		private static SecureRandom sr = null;
		static{
			try {
				sr = SecureRandom.getInstance("SHA1PRNG");
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String generatePassword(String plainPassword, byte[] salt)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		if (StringUtils.isBlank(plainPassword) || salt == null) {
			throw new NullPointerException("Plain password or salt can't be empty");
		}
		int iterations = 1000;
		char[] chars = plainPassword.toCharArray();
		PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
		
		byte[] hash = SecretKeyFactorySingleTon.skf.generateSecret(spec).getEncoded();
		return toHex(hash);
	}

	public static String generateSalt() throws NoSuchAlgorithmException {
		byte[] salt = new byte[32];
		SecureRandomSingleTon.sr.nextBytes(salt);
		return toHex(salt);
	}

	private static String toHex(byte[] array) throws NoSuchAlgorithmException {
		BigInteger bi = new BigInteger(1, array);
		String hex = bi.toString(16);
		int paddingLength = (array.length * 2) - hex.length();
		if (paddingLength > 0) {
			return String.format("%0" + paddingLength + "d", 0) + hex;
		} else {
			return hex;
		}
	}

	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
		long start=System.currentTimeMillis();
		String salt = generateSalt();
		long end=System.currentTimeMillis();
		System.out.println(end-start);
		System.out.println(salt);
		salt = generateSalt();
		System.out.println(salt);
		salt = generateSalt();
		System.out.println(salt);
		salt = generateSalt();
		System.out.println(salt);
	}
}
