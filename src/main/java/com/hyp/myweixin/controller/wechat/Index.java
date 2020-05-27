package com.hyp.myweixin.controller.wechat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2020/5/27 11:06
 * @Description: TODO
 */
@Controller
@Slf4j
public class Index {

    @RequestMapping("/")
    public String redirect() {
        log.info("用户想要请求首页被跳转到www.yapei.cool");
        return "redirect:http://www.yapei.cool";
    }

}
