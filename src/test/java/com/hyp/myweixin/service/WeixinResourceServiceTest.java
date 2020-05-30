package com.hyp.myweixin.service;

import com.hyp.myweixin.pojo.modal.WeixinResource;
import com.hyp.myweixin.pojo.modal.WeixinResourceConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/5/28 22:30
 * @Description: TODO
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class WeixinResourceServiceTest {

    @Autowired
    WeixinResourceService weixinResourceService;

    @Test
    public void contextLoads() {
        List<WeixinResource> weixinResourceByConfigId = weixinResourceService.getWeixinResourceByConfigId(
                WeixinResourceConfig.ConfigType.HeadImg.getConfigType(),
                WeixinResource.Status.Allow.getState());
        weixinResourceByConfigId.stream().forEach(string -> {
            log.info(string.toString());
        });
    }
}