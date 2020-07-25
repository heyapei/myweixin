package com.hyp.myweixin.utils.impl;

import com.hyp.myweixin.utils.MyEnDecryptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
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

    /**
     * utf8编码
     */
    private static final String CHARSET_UTF8 = "utf-8";

    /**
     * 密钥长度
     */
    private static final int RSA_KEY_LENGTH = 1024;

    /**
     * RSA最大加密明文大小
     */
    private static final int RSA_MAX_ENCRYPT_BLOCK = 117;
    /**
     * RSA最大解密密文大小
     */
    private static final int RSA_MAX_DECRYPT_BLOCK = 128;

    /**
     * 获取RAS的密钥对
     *
     * @return
     * @throws Exception
     */
    @Override
    public KeyPair rsaGetKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(RSA_KEY_LENGTH);
        return generator.generateKeyPair();
    }

    /**
     * 获取私钥
     *
     * @param privateKey 私钥字符串
     * @return
     * @throws Exception
     */
    @Override
    public PrivateKey rsaGetPrivateKey(String privateKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey =
                (new org.apache.commons.codec.binary.Base64()).decode(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 获取公钥
     *
     * @param publicKey 公钥字符串
     * @return
     * @throws Exception
     */
    @Override
    public PublicKey rsaGetPublicKey(String publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodedKey =
                (new org.apache.commons.codec.binary.Base64()).decode(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        return keyFactory.generatePublic(keySpec);
    }


    /**
     * RSA加密
     *
     * @param data      待加密数据
     * @param publicKey 公钥
     * @return
     * @throws Exception
     */
    @Override
    public String rsaEncrypt(String data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int inputLen = data.getBytes().length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offset > 0) {
            if (inputLen - offset > RSA_MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data.getBytes(), offset, RSA_MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * RSA_MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        // 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
        // 加密后的字符串
        return org.apache.commons.codec.binary.Base64.encodeBase64String(encryptedData);
    }

    /**
     * RSA解密
     *
     * @param data       待解密数据
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    @Override
    public  String rsaDecrypt(String data, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] dataBytes = org.apache.commons.codec.binary.Base64.decodeBase64(data);
        int inputLen = dataBytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offset > 0) {
            if (inputLen - offset > RSA_MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(dataBytes, offset, RSA_MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * RSA_MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        // 解密后的内容
        return new String(decryptedData, "UTF-8");
    }

    /**
     * 签名
     *
     * @param data       待签名数据
     * @param privateKey 私钥
     * @return 签名
     * @throws Exception
     */
    @Override
    public String rsaSign(String data, PrivateKey privateKey) throws Exception {
        byte[] keyBytes = privateKey.getEncoded();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initSign(key);
        signature.update(data.getBytes());
        return new String(org.apache.commons.codec.binary.Base64.encodeBase64(signature.sign()));
    }

    /**
     * 验签
     *
     * @param srcData   原始字符串
     * @param publicKey 公钥
     * @param sign      签名
     * @return 是否验签通过
     * @throws Exception
     */
    @Override
    public boolean rsaVerify(String srcData, PublicKey publicKey, String sign) throws Exception {
        byte[] keyBytes = publicKey.getEncoded();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey key = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(key);
        signature.update(srcData.getBytes());
        return signature.verify(org.apache.commons.codec.binary.Base64.decodeBase64(sign.getBytes()));
    }


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
