package com.hyp.myweixin;

import com.hyp.myweixin.pojo.modal.WeixinVoteBase;
import com.hyp.myweixin.utils.MyHttpClientUtil;
import com.hyp.myweixin.utils.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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


}
