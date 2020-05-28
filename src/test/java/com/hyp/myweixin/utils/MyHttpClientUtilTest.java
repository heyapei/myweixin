package com.hyp.myweixin.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/5/28 20:59
 * @Description: TODO
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MyHttpClientUtilTest {

    @Autowired
    MyHttpClientUtil myHttpClientUtil;

    @Test
    public void contextLoads() {
        String map = myHttpClientUtil.getMap("https://gank.io/api", null, null);
        log.info(map);
    }

}