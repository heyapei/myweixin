package com.hyp.myweixin.controller.qubaoming.user;

import com.alibaba.fastjson.JSONObject;
import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.config.weixin.WeChatPropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.dto.WechatDataDTO;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingWeixinUser;
import com.hyp.myweixin.pojo.qubaoming.query.user.QubaomingWeixinUserCreateQuery;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.qubaoming.QubaomingWeixinUserService;
import com.hyp.myweixin.utils.MyEntityUtil;
import com.hyp.myweixin.utils.MyHttpClientUtil;
import com.hyp.myweixin.utils.MyRequestVailDateUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
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
    private QubaomingWeixinUserService qubaomingWeixinUserService;
    @Autowired
    private WeChatPropertiesValue weChatPropertiesValue;
    @Autowired
    private MyHttpClientUtil myHttpClientUtil;


    @ApiOperation(value = "添加用户数据", tags = {"趣报名用户数据"})
    @PostMapping("/addUser")
    public Result<Object> addWechatInfo(
            @ApiParam(name = "创建用数据", value = "qubaomingWeixinUserCreateQuery", required = true)
            @Validated QubaomingWeixinUserCreateQuery qubaomingWeixinUserCreateQuery, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ObjectError next = bindingResult.getAllErrors().iterator().next();
            return Result.buildResult(Result.Status.SERVER_ERROR, next.getDefaultMessage());
        }
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }
        QubaomingWeixinUser qubaomingWeixinUserByOpenId = qubaomingWeixinUserService.getQubaomingWeixinUserByOpenId(qubaomingWeixinUserCreateQuery.getOpenId());

        if (qubaomingWeixinUserByOpenId != null) {
            qubaomingWeixinUserByOpenId.setCreateTime(System.currentTimeMillis());
            Integer integer = qubaomingWeixinUserService.updateSelectiveQubaomingWeixinUserBase(qubaomingWeixinUserByOpenId);
            if (integer != null && integer > 0) {
                return Result.buildResult(Result.Status.OK,
                        "更新第" + qubaomingWeixinUserByOpenId.getId() + "位用户信息成功", qubaomingWeixinUserByOpenId.getId());
            } else {
                return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR,
                        "更新第" + qubaomingWeixinUserByOpenId.getId() + "位用户信息失败", qubaomingWeixinUserByOpenId.getId());
            }
        }
        QubaomingWeixinUser qubaomingWeixinUser = null;
        try {
            qubaomingWeixinUser = MyEntityUtil.entity2VM(qubaomingWeixinUserCreateQuery, QubaomingWeixinUser.class);
        } catch (Exception e) {
            log.error("用户数据转换错误，错误原因：{}", e.toString());
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, "用户数据转换错误");
        }
        if (qubaomingWeixinUser == null) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, "用户数据转换后为空");
        }

        try {
            qubaomingWeixinUser = (QubaomingWeixinUser) MyEntityUtil.entitySetDefaultValue(qubaomingWeixinUser);
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, "用户数据默认值赋予错误");
        }
        int i = qubaomingWeixinUserService.insertReturnPk(qubaomingWeixinUser);
        if (i <= 0) {
            throw new MyDefinitionException("未能正确保存用户数据");
        }
        return Result.buildResult(Result.Status.OK,
                "欢迎" + qubaomingWeixinUser.getNickName()
                        + "！我们的第" + i + "用户！", qubaomingWeixinUser.getId());
    }

    @ApiOperation(value = "获取用户的openId", tags = {"趣报名用户数据"})
    @PostMapping("getopenid")
    public Result getOpenId(String code) {
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }
        if (StringUtils.isBlank(code)) {
            throw new MyDefinitionException("换取openId的参数不可以为空");
        }
        String appId = weChatPropertiesValue.getQuBaoMingAppId();
        String secret = weChatPropertiesValue.getQuBaoMingAppSecret();
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appId + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
        String parameter = myHttpClientUtil.getParameter(url, null, null);
        if (StringUtils.isBlank(parameter)) {
            throw new MyDefinitionException("wechat没有返回任何信息");
        }
        WechatDataDTO wechatDataDTO = null;
        try {
            wechatDataDTO = JSONObject.parseObject(parameter, WechatDataDTO.class);
        } catch (Exception e) {
            log.error("wechat返回的数据不为自定义实体，错误原因：{}", e.toString());
            throw new MyDefinitionException("wechat返回的数据不为自定义实体");
        }
        return Result.buildResult(Result.Status.OK, wechatDataDTO);
    }


}
