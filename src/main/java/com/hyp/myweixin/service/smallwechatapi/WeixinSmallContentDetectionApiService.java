package com.hyp.myweixin.service.smallwechatapi;

import com.alibaba.fastjson.JSONObject;
import com.hyp.myweixin.exception.MyDefinitionException;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/25 19:51
 * @Description: TODO
 */
public interface WeixinSmallContentDetectionApiService {


    /**
     * 获取qubaoming无数量限制的二维码
     *
     * @param scene
     * @param page
     * @return
     * @throws MyDefinitionException
     */
    String getQuBaoMingQrCodeUnlimited(String scene, String page) throws MyDefinitionException;


    /**
     * 获取二维码信息
     *
     * @return
     * @throws MyDefinitionException
     */
    String getQrCode(String accessToken) throws MyDefinitionException;


    /**
     * 违规图片检测
     *
     * @param multipartFile 图片
     * @param accessToken   微信token 填写null系统会自动请求
     * @return boolean值
     * @throws MyDefinitionException
     */
    Boolean checkImgSecCheckApi(MultipartFile multipartFile, String accessToken) throws MyDefinitionException;

    /**
     * 违规图片检测
     *
     * @param multipartFile 图片
     * @param accessToken   微信token 填写null系统会自动请求
     * @return
     * @throws MyDefinitionException
     */
    JSONObject getImgSecCheckApiMsg(MultipartFile multipartFile, String accessToken) throws MyDefinitionException;


    /**
     * 违规文字检测
     *
     * @param msgText     文字
     * @param accessToken 微信token 填写null系统会自动请求
     * @return boolean值
     * @throws MyDefinitionException
     */
    Boolean checkMsgSecCheckApi(String msgText, String accessToken) throws MyDefinitionException;


    /**
     * 违规文字检测
     *
     * @param msgText     文字
     * @param accessToken 微信token 填写null系统会自动请求
     * @return
     * @throws MyDefinitionException
     */
    JSONObject getMsgSecCheckApiMsg(String msgText, String accessToken) throws MyDefinitionException;


    /**
     * 获取小程序全局唯一后台接口调用凭据（access_token）
     * 调用绝大多数后台接口时都需使用 access_token，开发者需要进行妥善保存
     * https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
     *
     * @return
     * @throws MyDefinitionException
     */
    JSONObject getAccessToken() throws MyDefinitionException;


    /**
     * 获取趣投票的accessToken
     * 获取小程序全局唯一后台接口调用凭据（access_token）
     * 调用绝大多数后台接口时都需使用 access_token，开发者需要进行妥善保存
     * https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
     *
     * @return
     * @throws MyDefinitionException
     */
    JSONObject getQuTouPiaoAccessToken() throws MyDefinitionException;


    /**
     * 获取趣报名的accessToken
     * 获取小程序全局唯一后台接口调用凭据（access_token）
     * 调用绝大多数后台接口时都需使用 access_token，开发者需要进行妥善保存
     * https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
     *
     * @return
     * @throws MyDefinitionException
     */
    JSONObject getQuBaoMingAccessToken() throws MyDefinitionException;


    /**
     * 通过appName获取对应的accessToken
     *
     * @param appName 小程序名称
     * @return
     * @throws MyDefinitionException
     */
    String getAccessTokenByAppName(String appName) throws MyDefinitionException;

}
