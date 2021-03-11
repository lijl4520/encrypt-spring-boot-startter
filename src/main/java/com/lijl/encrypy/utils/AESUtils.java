package com.lijl.encrypy.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @Author Lijl
 * @ClassName AESUtils
 * @Description 对称加密工具类
 * @Date 2021/3/10 9:17
 * @Version 1.0
 */
public class AESUtils {

    private static final String AES_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * @Author lijiale
     * @MethodName getCipher
     * @Description 获取 Cipher
     * @Date 9:20 2021/3/10
     * @Version 1.0
     * @param key
     * @param model
     * @return: javax.crypto.Cipher
    **/
    private static Cipher getCipher(byte[] key, int model) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key,"AES");
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(model,secretKeySpec);
        return cipher;
    }

    /**
     * @Author lijiale
     * @MethodName encrypy
     * @Description AES加密
     * @Date 9:22 2021/3/10
     * @Version 1.0
     * @param data
     * @param key
     * @return: java.lang.String
    **/
    public static String encrypy(byte[] data, byte[] key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = getCipher(key, Cipher.ENCRYPT_MODE);
        return Base64.getEncoder().encodeToString(cipher.doFinal(data));
    }

    /**
     * @Author lijiale
     * @MethodName decrypt
     * @Description AES解密
     * @Date 9:24 2021/3/10
     * @Version 1.0
     * @param data
     * @param key
     * @return: byte[]
    **/
    public static byte[] decrypt(byte[] data,byte[] key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = getCipher(key, Cipher.DECRYPT_MODE);
        return cipher.doFinal(Base64.getDecoder().decode(data));
    }
}
