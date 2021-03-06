package com.vocal.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class AES {

	public static String encrypt(String value, String key)
			throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		byte[] value_bytes = value.getBytes("UTF-8");
		byte[] key_bytes = getKeyBytes(key);
		return Base64.encodeBase64String(encrypt(value_bytes, key_bytes, key_bytes));
	}

	public static byte[] encrypt(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		// setup AES cipher in CBC mode with PKCS #5 padding
		Cipher localCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

		// encrypt
		localCipher.init(1, new SecretKeySpec(paramArrayOfByte2, "AES"), new IvParameterSpec(paramArrayOfByte3));
		return localCipher.doFinal(paramArrayOfByte1);
	}

	public static String decrypt(String value, String key) throws GeneralSecurityException, IOException {
		byte[] value_bytes = Base64.decodeBase64(value);
		byte[] key_bytes = getKeyBytes(key);
		return new String(decrypt(value_bytes, key_bytes, key_bytes), "UTF-8");
	}

	public static byte[] decrypt(byte[] ArrayOfByte1, byte[] ArrayOfByte2, byte[] ArrayOfByte3)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		// setup AES cipher in CBC mode with PKCS #5 padding
		Cipher localCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

		// decrypt
		localCipher.init(2, new SecretKeySpec(ArrayOfByte2, "AES"), new IvParameterSpec(ArrayOfByte3));
		return localCipher.doFinal(ArrayOfByte1);
	}

	private static byte[] getKeyBytes(String paramString) throws UnsupportedEncodingException {
		byte[] arrayOfByte1 = new byte[16];
		byte[] arrayOfByte2 = paramString.getBytes("UTF-8");
		System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0, Math.min(arrayOfByte2.length, arrayOfByte1.length));
		return arrayOfByte1;
	}

	public static void main(String[] args) throws GeneralSecurityException, IOException {

//		 AES aes = new AES(); 
//		 String val = "6"; 
//		 System.out.println(aes.encrypt(val,"walletplay")); 
//		 System.out.println(aes.decrypt("SgsKVj7rkeK7wZ/u5y9jeQ","walletplay"));
//		 String x = aes.encrypt(val,"walletplay"); 
//		 String s = aes.decrypt(x, "walletplay"); 
//		 System.out.println(s); 
		 

		System.out.println(decrypt("LcrRTyAZ6+q0LzgNJGVt+Q==", "walletplay"));
	}
}