package com.hyp.myweixin.service;

import com.hyp.myweixin.pojo.modal.WeixinResource;
import com.hyp.myweixin.pojo.modal.WeixinResourceConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
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

    @Test
    public void testDate() {
        boolean parsable = NumberUtils.isParsable("123121222222222222222222222");
        log.info("信息：" + parsable);
        parsable = NumberUtils.isParsable("1sdff");
        log.info("信息2：" + parsable);
    }

    @Test
    public void testiphone() {
        String userAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 13_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/7.0.12(0x17000c30) NetType/WIFI Language/zh_CN";
        String iphoneOS = "iPhone OS";
        String mobileType = null;
        if (userAgent.contains(iphoneOS)) {
            mobileType = userAgent.replaceAll(" ", "");
            String iphoneOSIndex = iphoneOS.replaceAll(" ", "");
            mobileType = mobileType.substring(mobileType.indexOf(iphoneOSIndex) + 6, mobileType.indexOf(iphoneOSIndex) + 10);
        } else {
            mobileType = "未知型号";
        }

        System.out.println("手机型号" + mobileType);


    }

    @Test
    public void addWeixinResource() {
        WeixinResource weixinResource = new WeixinResource();
        weixinResource.setResourceConfigId(0);
        weixinResource.setType("0");
        weixinResource.setPath("/upload/lunbo/1.png");
        //weixinResource.setPath("/upload/lunbo/2.png");
        // weixinResource.setPath("/upload/lunbo/3.png");
        // weixinResource.setPath("/upload/lunbo/4.png");
        weixinResource.setName("");
        weixinResource.setRealName("");
        weixinResource.setStatus(0);
        weixinResource.setSize(0L);
        weixinResource.setMd5("");
        weixinResource.setTitle("");
        weixinResource.setDescription("");
        weixinResource.setCreateTime(new Date());
        weixinResource.setCreateUserId(0);
        weixinResource.setThumbnailPath1("");
        weixinResource.setThumbnailPath2("");
        weixinResource.setThumbnailPath3("");
        weixinResource.setThumbnailUrl1("");
        weixinResource.setThumbnailUrl2("");
        weixinResource.setThumbnailUrl3("");


        weixinResourceService.addWeixinResource(weixinResource);
    }
}