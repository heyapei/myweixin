package com.hyp.myweixin.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/7 0:40
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MyIpMacUtilTest {

    @Autowired
    private MyIpMacUtil myIpMacUtil;
    @Autowired
    private HttpServletRequest request;

    @Test
    public void getRealIP() {

    }

    @Test
    public void isComeFromCDN() {
    }

    @Test
    public void getIpNum() {
        long ipNum = myIpMacUtil.ipToLong("222.69.133.116");
        log.info("地址转LONG：{}",ipNum);
    }

    @Test
    public void isInnerIP() {
    }

    @Test
    public  void longToIP() {
        long ipNum = myIpMacUtil.ipToLong("222.69.133.116");
        log.info("地址转LONG：{}",ipNum);
        String s = myIpMacUtil.longToIP(ipNum);
        log.info("LONG转地址：{}",s );
    }
}