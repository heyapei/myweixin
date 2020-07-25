package com.hyp.myweixin.service.smallwechatapi;

import com.alibaba.fastjson.JSONObject;
import com.hyp.myweixin.exception.MyDefinitionException;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/25 19:51
 * @Description: TODO
 */
public interface WeixinSmallContentDetectionApiService {


    /**
     * 违规文字检测
     * @param msgText 文字
     * @param token 微信token
     * @return
     * @throws MyDefinitionException
     */
    JSONObject msgSecCheckApi(String msgText,String token) throws MyDefinitionException;




    /**
     * 获取小程序全局唯一后台接口调用凭据（access_token）
     * 调用绝大多数后台接口时都需使用 access_token，开发者需要进行妥善保存
     * https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
     *
     * @return
     * @throws MyDefinitionException
     */
    JSONObject getAccessToken() throws MyDefinitionException;

}
