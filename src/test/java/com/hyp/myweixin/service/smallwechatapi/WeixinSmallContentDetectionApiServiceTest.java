package com.hyp.myweixin.service.smallwechatapi;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/25 20:00
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class WeixinSmallContentDetectionApiServiceTest {

    @Autowired
    private WeixinSmallContentDetectionApiService weixinSmallContentDetectionApiService;

    @Test
    public void getAccessToken() {
        JSONObject accessToken = weixinSmallContentDetectionApiService.getAccessToken();

        //35_63LOgkyE7IzOlPPQ3uOn-M7YLILLFuBYp3lVOdZXRzVhrCOXZOiGe9FxGq_GYDtRDV0pUro5Q0hc8cnn5v2EtS37BJQcxms9xb4XXWtcE0TQV4yqh13jCQBIsP2lWdgTG7UB4oB35ZgXFb-tYFRiAHAHOA
        //35_63LOgkyE7IzOlPPQ3uOn-M7YLILLFuBYp3lVOdZXRzVhrCOXZOiGe9FxGq_GYDtRDV0pUro5Q0hc8cnn5v2EtS37BJQcxms9xb4XXWtcE0TQV4yqh13jCQBIsP2lWdgTG7UB4oB35ZgXFb-tYFRiAHAHOA
        log.info("查询结果：{}", accessToken);
    }
}