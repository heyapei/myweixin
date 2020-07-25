package com.hyp.myweixin.utils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/25 16:19
 * @Description: TODO 加密解密工具
 */
public interface MyEnDecryptionUtil {


    /**
     * AES 解密
     *
     * @param strToDecrypt 解密用密字符串
     * @param secret       密钥
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     */
    String aesDecrypt(String strToDecrypt, String secret) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException;


    /**
     * AES 加密
     *
     * @param strToEncrypt 加密用字符串
     * @param secret       密钥
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    String aesEncrypt(String strToEncrypt, String secret) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException;


    /**
     * 生成AES随机密钥
     *
     * @param keySize 密钥大小推荐128 256
     * @return
     * @throws NoSuchAlgorithmException
     */
    String aesGenerateSecret(int keySize) throws NoSuchAlgorithmException;

    /**
     * byte数组转化为16进制字符串
     *
     * @param bytes
     * @return
     */
    String byteToHexString(byte[] bytes);


    /**
     * BASE64解密
     *
     * @param key
     * @return
     * @throws IOException
     */
    byte[] base64DnCrypt(String key) throws IOException;

    /**
     * BASE64加密
     *
     * @param str
     * @return
     * @throws Exception
     */
    String base64EnCrypt(String str);

    /**
     * md5加密-32位小写
     *
     * @param encryptStr 需要加密的字符串
     * @return
     * @throws NoSuchAlgorithmException
     */
    String md532Low(String encryptStr) throws NoSuchAlgorithmException;

    /**
     * md5加密-32位大写
     *
     * @param encryptStr 需要加密的字符串
     * @return
     * @throws NoSuchAlgorithmException
     */
    String md532Upp(String encryptStr) throws NoSuchAlgorithmException;

    /**
     * md5加密-16位小写
     *
     * @param encryptStr 需要加密的字符串
     * @return
     * @throws NoSuchAlgorithmException
     */
    String md516Low(String encryptStr) throws NoSuchAlgorithmException;

    /**
     * md5加密-16位大写
     *
     * @param encryptStr 需要加密的字符串
     * @return
     * @throws NoSuchAlgorithmException
     */
    String md516Upp(String encryptStr) throws NoSuchAlgorithmException;

}
