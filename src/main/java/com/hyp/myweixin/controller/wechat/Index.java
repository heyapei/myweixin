package com.hyp.myweixin.controller.wechat;

import com.hyp.myweixin.exception.MyDefinitionException;
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


    @RequestMapping("/1")
    public String Myredirect() {
        log.info("用户想要请求首页被跳转到www.yapei.cool");
        try {
            Integer.parseInt("nihao");
        } catch (NumberFormatException e) {
            throw new MyDefinitionException("该地址请求失败");
        }
        return "redirect:http://www.yapei.cool";
    }

}
