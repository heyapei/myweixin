package com.hyp.myweixin.controller.wechat.user;


import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.config.weixin.PropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.modal.WeixinVoteUser;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.WeixinVoteUserService;
import com.hyp.myweixin.utils.MyRequestVailDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/5/28 18:44
 * @Description: TODO
 */
@RestController
@RequestMapping("/wechat/v1/user")
@Slf4j
public class UserController {

    @Autowired
    private WeixinVoteUserService weixinVoteUserService;
    @Autowired
    private MyRequestVailDateUtil myRequestValidateUtil;
    @Autowired
    private SecretKeyPropertiesValue secretKeyPropertiesValue;

    @PostMapping("/add")
    public Result addWechatInfo(WeixinVoteUser weixinVoteUser, HttpServletRequest httpServletRequest) {
        log.info("其中对应的weixin数据：" + weixinVoteUser.toString());
        boolean b = myRequestValidateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }
        if (weixinVoteUser.getOpenId() == null) {
            throw new MyDefinitionException(
                    Integer.parseInt(Result.Status.PARAM_IS_BLANK.getCode()),
                    "微信用户的openId不可以为空");
        }
        WeixinVoteUser userByOpenId = weixinVoteUserService.getUserByOpenId(weixinVoteUser.getOpenId());
        if (userByOpenId != null) {
            return Result.buildResult(Result.Status.OK,
                    "该微信用户已经是我们的第" + userByOpenId.getId()
                            + "位用户，无需重复保存");
        }

        int i = weixinVoteUserService.addWechatInfo(weixinVoteUser);
        if (i <= 0) {
            throw new MyDefinitionException("未能正确保存用户数据");
        }
        return Result.buildResult(Result.Status.OK,
                "欢迎" + weixinVoteUser.getNickName()
                        + "！我们的第" + i + "用户！");
    }




    /*下面是一个测试*/


    @Autowired
    private PropertiesValue propertiesValue;

    /**
     * 麻痹呀 突然又自己好了
     *
     * @return
     */
    @GetMapping("testWeixin")
    public String sendTextMail2(HttpServletRequest httpServletRequest) {

        boolean b = myRequestValidateUtil.validateSignMd5(httpServletRequest, secretKeyPropertiesValue.getMd5Key());
        log.info("验证结果：" + b);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }
        String appid = propertiesValue.getAppid();
        log.info("微信：ci" + appid + "32");

        return "AAAAA";
    }
}