package com.hyp.myweixin.controller.wechat.api;

import com.alibaba.fastjson.JSONObject;
import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.config.weixin.WeChatPropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.dto.WechatDataDTO;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.utils.MyHttpClientUtil;
import com.hyp.myweixin.utils.MyRequestVailDateUtil;
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
 * @Date 2020/6/7 17:54
 * @Description: TODO 用于和微信进行交互
 */
@RestController
@RequestMapping("/wechat/v1/api")
@Slf4j
public class ApiController {

    @Autowired
    private MyRequestVailDateUtil myRequestValidateUtil;
    @Autowired
    private SecretKeyPropertiesValue secretKeyPropertiesValue;
    @Autowired
    private WeChatPropertiesValue weChatPropertiesValue;
    @Autowired
    private MyHttpClientUtil myHttpClientUtil;

    /**
     * 小程序发过来code，后端拿到后向微信进行请求获取openId和sessionKey
     *
     * @param httpServletRequest
     */
    @PostMapping("getopenid")
    public Result getOpenId(HttpServletRequest httpServletRequest, String code) {
        boolean b = myRequestValidateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }
        if (StringUtils.isBlank(code)) {
            throw new MyDefinitionException("换取openId的参数不可以为空");
        }
        String appId = weChatPropertiesValue.getAppid();
        String secret = weChatPropertiesValue.getAppSecret();
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appId + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
        String parameter = myHttpClientUtil.getParameter(url, null, null);
        log.info("使用code换取openId，请求的结果：" + parameter);
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
