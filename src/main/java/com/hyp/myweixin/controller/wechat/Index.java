package com.hyp.myweixin.controller.wechat;

import com.hyp.myweixin.exception.MyDefinitionException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
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
@Api(value = "首页地址")
public class Index {

    @RequestMapping("/")
    @ApiOperation(value="根据用户编号获取用户姓名")
    public String redirect() {
        log.info("用户想要请求首页被跳转到www.yapei.cool");
        return "redirect:http://www.yapei.cool";
    }


    @RequestMapping("/testError")
    public String testError() {
        log.info("用户想要请求首页被跳转到www.yapei.cool");
        try {
            Integer.parseInt("nihao");
        } catch (NumberFormatException e) {
            throw new MyDefinitionException("该地址请求失败");
        }
        return "redirect:http://www.yapei.cool";
    }

}
