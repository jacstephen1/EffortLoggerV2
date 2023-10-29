/*
 * Author: Jacob Wetherell
 * ASU ID: 1224869945
 * CSE 360 EffortLogger 2.0 Development
 */


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES {
	private static SecretKeySpec secretKey;
	private static byte[] key;
	private static String dkey = "fhsdfjiowqn m,sodijw28"; //Random default encryption key
	
	//Set dKey
	public void setdkey(String set)
	{
		dkey = set;
	}
	
	//Set Key Default
	public static void setKey()
	{
		try {
			key = dkey.getBytes("UTF-8");
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);
			secretKey = new SecretKeySpec(key, "AES");
		} catch (NoSuchAlgorithmException e) {}
		catch (UnsupportedEncodingException e) {
			
		}
	}
	//Set Key Overload
	public static void setKey(String skey)
	{
		try {
			key = skey.getBytes("UTF-8");
			MessageDigest sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);
			secretKey = new SecretKeySpec(key, "AES");
		} catch (NoSuchAlgorithmException e) {}
		catch (UnsupportedEncodingException e) {
			
		}
	}
	
	//Encrypt
	public static String encrypt(String stringToEncrypt)
	{
		try {
			setKey();
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return Base64.getEncoder().encodeToString(cipher.doFinal(stringToEncrypt.getBytes("UTF-8")));
		} catch (Exception e) {
			
		}
		return null;
	}
	//Encrypt Overload
	public static String encrypt(String stringToEncrypt, String sec)
	{
		try {
			setKey(sec);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return Base64.getEncoder().encodeToString(cipher.doFinal(stringToEncrypt.getBytes("UTF-8")));
		} catch (Exception e) {
			
		}
		return null;
	}
	
	//Decrypt
	public static String decrypt(String stringToDecrypt)
	{
		try {
			setKey();
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return new String(cipher.doFinal(Base64.getDecoder().decode(stringToDecrypt)));
		} catch (Exception e) {
			
		}
		return null;
	}
	//Decrypt Overload
	public static String decrypt(String stringToDecrypt, String sec)
	{
		try {
			setKey(sec);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return new String(cipher.doFinal(Base64.getDecoder().decode(stringToDecrypt)));
		} catch (Exception e) {
			
		}
		return null;
	}
}
