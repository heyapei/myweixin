package com.hyp.myweixin.controller.wechat.vote;

import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.pojo.modal.WeixinVoteUser;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.utils.MyRequestVailDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/6 16:58
 * @Description: TODO 用户对活动进行投票
 */
@RestController
@RequestMapping("/wechat/v1/vote")
@Slf4j
public class VoteController {


    @Autowired
    private MyRequestVailDateUtil myRequestVailDateUtil;
    @Autowired
    private SecretKeyPropertiesValue secretKeyPropertiesValue;

    @RequestMapping("/add")
    public Result addWechatInfo(WeixinVoteUser weixinVoteUser, HttpServletRequest httpServletRequest) {
        boolean b = myRequestVailDateUtil.validateSignMd5(httpServletRequest,secretKeyPropertiesValue.getMd5Key());
        log.info("验证结果：" + b);
        return null;
    }
}
