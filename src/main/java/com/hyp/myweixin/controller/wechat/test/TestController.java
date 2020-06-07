package com.hyp.myweixin.controller.wechat.test;

import com.hyp.myweixin.utils.MyIpMacUtil;
import com.hyp.myweixin.utils.amaputil.AmapApiUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/7 11:36
 * @Description: TODO
 */
@RestController
@RequestMapping("/hyptest")
@Slf4j
public class TestController {


    @Autowired
    private AmapApiUtil amapApiUtil;
    @Autowired
    private MyIpMacUtil myIpMacUtil;

    @GetMapping("testasync")
    public String sendTextMail2(HttpServletRequest httpServletRequest) {

        return "处理结果";
    }
}
