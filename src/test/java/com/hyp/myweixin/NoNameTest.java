package com.hyp.myweixin;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.modal.WeixinVoteBase;
import com.hyp.myweixin.utils.MyEnumUtil;
import com.hyp.myweixin.utils.MyHttpClientUtil;
import com.hyp.myweixin.utils.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/7 18:04
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class NoNameTest {


    @Autowired
    private MyHttpClientUtil myHttpClientUtil;
    @Autowired
    private RedisUtil redisUtil;


    @Test
    public void testInteger() {


        Integer activeStatus = 5;

        Boolean enumKeyRight = MyEnumUtil.enumKeyRight(activeStatus, WeixinVoteBase.ActiveStatusEnum.class);
        if (!enumKeyRight) {
            throw new MyDefinitionException("更改的活动状态不被允许,请联系系统管理");
        }

        Integer activeStatus1 = 10;

        Boolean valueRight = MyEnumUtil.enumValueRight(activeStatus1, WeixinVoteBase.ActiveStatusEnum.class);
        if (!valueRight) {
            throw new MyDefinitionException("更改的活动状态不被允许,请联系系统管理");
        }

    }


    @Test
    public void testMyRedisTO() {
        WeixinVoteBase weixinVoteBase = new WeixinVoteBase();
        weixinVoteBase.setId(1);
        weixinVoteBase.setCreateSysUserId(100);
        weixinVoteBase.setActiveDesc("这个一段中文描述");
        log.info("打印实体类：" + weixinVoteBase.toString());
        boolean weixinVoteBase1 = redisUtil.set("weixinVoteBase", weixinVoteBase, 1000);
        log.info("存入实体类，{}", weixinVoteBase1);
        Object weixinVoteBase2 = redisUtil.get("weixinVoteBase");
        if (weixinVoteBase2 != null) {
            log.info("拉取回来的数据：" + weixinVoteBase2.toString());
            log.info("拉取回来的数据2：" + (WeixinVoteBase) weixinVoteBase2);
            WeixinVoteBase ss = (WeixinVoteBase) weixinVoteBase2;
            log.info("拉取回来的数据3：" + ss.toString());
        } else {
            log.info("拉取回来数据失败");
        }
    }

    @Test
    public void testMyRedis() {
        boolean set = redisUtil.set("1", "19", 1000);
        log.info("连接redis测试：{}", set);
        Object o = redisUtil.get("1");
        log.info("连接redis测试：{}", o.toString());
        System.out.println(redisUtil.getExpire("1"));
    }


    @Test
    public void contextLoads() {

        WeixinVoteBase weixinVoteBase = new WeixinVoteBase();
        System.out.println("查看是否可以有默认值：" + weixinVoteBase.toString());

        String code = "071XkLoW0ChBy02B5RlW0x2qoW0XkLoj";
        String appId = "wx09609fe79142649a";
        String secret = "9eb2c3ea68055398afb0280fbbcd6ebe";
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appId + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";

        String parameter = myHttpClientUtil.getParameter(url, null, null);
        System.out.println("换取的结果：" + parameter);
    }

    /*测试AES算法*/
    @Test
    public void testAES() throws Exception {
        //getKey(); 5cc3b16449beb0b2048c4daa4f9e2ffd
        String nihaoya = Encode_AES_CBC_NoPadding("Egi2zRHPg6TQHNhE", "nihaoya");
        System.out.println(nihaoya); // eT0jbAS+vgXzdlYUGBOtiQ==
        System.out.println(DeCode_AES_CBC_NoPadding("Egi2zRHPg6TQHNhE", "eT0jbAS+vgXzdlYUGBOtiQ=="));

    }


    /**
     * 加密  - AES/CBC/NoPadding
     *
     * @throws Exception
     **/
    private static String Encode_AES_CBC_NoPadding(String key, String content) throws Exception {
        if (StringUtils.isBlank(key)) {
            throw new NullPointerException("key is null");
        }
        if (StringUtils.isBlank(content)) {
            return null;
        }
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = content.getBytes("UTF-8");
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(key.getBytes("UTF-8"));
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            return Base64.encodeBase64String(cipher.doFinal(plaintext));
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    /**
     * 解密  - AES/CBC/NoPadding
     **/
    private String DeCode_AES_CBC_NoPadding(String key, String content) throws Exception {
        if (StringUtils.isBlank(key)) {
            throw new NullPointerException("key is null");
        }
        if (StringUtils.isBlank(content)) {
            return null;
        }

        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            Key sKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, generateIV(key.getBytes("UTF-8")));// 初始化
            byte[] result = cipher.doFinal(Base64.decodeBase64(content.getBytes("UTF-8")));
            return new String(result, "utf-8").trim();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }

    }

    public static AlgorithmParameters generateIV(byte[] iv) throws Exception {
        AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
        params.init(new IvParameterSpec(iv));
        return params;
    }


}
