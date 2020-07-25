package com.hyp.myweixin.utils.impl;

import com.hyp.myweixin.utils.MyEnDecryptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;


/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/25 16:20
 * @Description: TODO
 */
@Slf4j
@Service
public class MyEnDecryptionUtilImpl implements MyEnDecryptionUtil {

    private static final String CHARSET_UTF8 = "utf-8";


    /**
     * AES 加密
     *
     * @param strToEncrypt
     * @param secret
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    @Override
    public String aesEncrypt(String strToEncrypt, String secret) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        SecretKeySpec secretKey = getKey(secret);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(CHARSET_UTF8)));
    }

    /**
     * AES 解密
     *
     * @param strToDecrypt
     * @param secret
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidKeyException
     */
    @Override
    public String aesDecrypt(String strToDecrypt, String secret) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        SecretKeySpec secretKey = getKey(secret);
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
    }

    /**
     * aes拿key的工具
     *
     * @param myKey
     * @return
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     */
    private SecretKeySpec getKey(String myKey) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] key = myKey.getBytes(CHARSET_UTF8);
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16);
        return new SecretKeySpec(key, "AES");
    }


    /**
     * 生成随机密钥
     *
     * @param keySize 密钥大小推荐128 256
     * @return
     * @throws NoSuchAlgorithmException
     */
    @Override
    public String aesGenerateSecret(int keySize) throws NoSuchAlgorithmException {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(keySize, new SecureRandom());
        SecretKey key = generator.generateKey();
        return byteToHexString(key.getEncoded());
    }

    /**
     * byte数组转化为16进制字符串
     *
     * @param bytes
     * @return
     */
    @Override
    public String byteToHexString(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String strHex = Integer.toHexString(bytes[i]);
            if (strHex.length() > 3) {
                sb.append(strHex.substring(6));
            } else {
                if (strHex.length() < 2) {
                    sb.append("0" + strHex);
                } else {
                    sb.append(strHex);
                }
            }
        }
        return sb.toString();
    }


    /**
     * BASE64解密
     *
     * @param key
     * @return
     * @throws Exception
     */
    @Override
    public byte[] base64DnCrypt(String key) throws IOException {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    /**
     * BASE64加密
     *
     * @param str
     * @return
     * @throws Exception
     */
    @Override
    public String base64EnCrypt(String str) {
        byte[] key = str.getBytes();
        return (new BASE64Encoder()).encodeBuffer(key);
    }


    /**
     * md5加密-32位小写
     *
     * @param encryptStr 需要加密的字符串
     * @return
     */
    @Override
    public String md532Low(String encryptStr) throws NoSuchAlgorithmException {

        return md5Encrypt32(encryptStr).toLowerCase();
    }

    /**
     * md5加密-32位大写
     *
     * @param encryptStr 需要加密的字符串
     * @return
     */
    @Override
    public String md532Upp(String encryptStr) throws NoSuchAlgorithmException {

        return md5Encrypt32(encryptStr).toUpperCase();
    }

    /**
     * md5加密-16位小写
     *
     * @param encryptStr 需要加密的字符串
     * @return
     */
    @Override
    public String md516Low(String encryptStr) throws NoSuchAlgorithmException {

        return md5Encrypt32(encryptStr).substring(8, 24).toLowerCase();
    }

    /**
     * md5加密-16位大写
     *
     * @param encryptStr 需要加密的字符串
     * @return
     */
    @Override
    public String md516Upp(String encryptStr) throws NoSuchAlgorithmException {

        return md5Encrypt32(encryptStr).substring(8, 24).toUpperCase();
    }

    /**
     * 加密-32位小写
     *
     * @param encryptStr 需要加密的字符串
     * @return
     */
    private String md5Encrypt32(String encryptStr) throws NoSuchAlgorithmException {
        MessageDigest md5;

        md5 = MessageDigest.getInstance("MD5");
        byte[] md5Bytes = md5.digest(encryptStr.getBytes());
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = ((int) md5Bytes[i]) & 0xff;
            if (val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        encryptStr = hexValue.toString();
        return encryptStr;
    }
}
