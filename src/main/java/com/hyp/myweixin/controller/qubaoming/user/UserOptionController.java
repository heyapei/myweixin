package com.hyp.myweixin.controller.qubaoming.user;

import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.modal.WeixinVoteUser;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.WeixinVoteUserService;
import com.hyp.myweixin.utils.MyRequestVailDateUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/15 15:58
 * @Description: TODO
 */
@RestController
@RequestMapping("qubaoming/user/option")
@Slf4j
public class UserOptionController {

    @Autowired
    private MyRequestVailDateUtil myRequestVailDateUtil;
    @Autowired
    private SecretKeyPropertiesValue secretKeyPropertiesValue;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private WeixinVoteUserService weixinVoteUserService;

    @ApiOperation(value = "添加用户数据", tags = {"趣报名添加用户数据"})
    @PostMapping("/addUser")
    public Result<Object> addWechatInfo(WeixinVoteUser weixinVoteUser, HttpServletRequest httpServletRequest) {
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }
        if (weixinVoteUser.getOpenId() == null || StringUtils.isBlank(weixinVoteUser.getOpenId())) {
            throw new MyDefinitionException(
                    Integer.parseInt(Result.Status.PARAM_IS_BLANK.getCode()),
                    "微信用户的openId不可以为空");
        }
        WeixinVoteUser userByOpenId = weixinVoteUserService.getUserByOpenId(weixinVoteUser.getOpenId());
        if (userByOpenId != null) {
            weixinVoteUser.setCreateTime(userByOpenId.getCreateTime());
            Integer integer = weixinVoteUserService.updateWeixinUserByOpenId(weixinVoteUser);
            if (integer != null && integer > 0) {
                return Result.buildResult(Result.Status.OK,
                        "更新第" + userByOpenId.getId() + "位用户信息成功", userByOpenId.getId());
            } else {
                return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR,
                        "更新第" + userByOpenId.getId() + "位用户信息失败", userByOpenId.getId());
            }
        }
        int i = weixinVoteUserService.addWechatInfo(weixinVoteUser);
        if (i <= 0) {
            throw new MyDefinitionException("未能正确保存用户数据");
        }
        return Result.buildResult(Result.Status.OK,
                "欢迎" + weixinVoteUser.getNickName()
                        + "！我们的第" + i + "用户！", weixinVoteUser.getId());
    }


}
