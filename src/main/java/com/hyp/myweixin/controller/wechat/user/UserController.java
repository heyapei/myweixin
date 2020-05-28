package com.hyp.myweixin.controller.wechat.user;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/5/28 18:44
 * @Description: TODO
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @GetMapping("/index")
    public String sendTextMail() {
        return "123123";
    }

    @GetMapping("")
    public String sendTextMail1() {
        return "567890";
    }
    @PostMapping("")
    public String sendTextMail2() {
        return "AAAAA";
    }
}