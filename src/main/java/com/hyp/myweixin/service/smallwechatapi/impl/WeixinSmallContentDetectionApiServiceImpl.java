package com.hyp.myweixin.service.smallwechatapi.impl;

import com.alibaba.fastjson.JSONObject;
import com.hyp.myweixin.config.weixin.WeChatPropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.service.smallwechatapi.WeixinSmallContentDetectionApiService;
import com.hyp.myweixin.utils.MyHttpClientUtil;
import com.hyp.myweixin.utils.redis.MyRedisUtil;
import com.hyp.myweixin.utils.thumbnailator.MyThumbnailImgOptionUtil;
import com.hyp.myweixin.utils.thumbnailator.MyThumbnailImgType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/25 19:54
 * @Description: TODO
 */
@Slf4j
@Service
@PropertySource("classpath:wechat.properties")
public class WeixinSmallContentDetectionApiServiceImpl implements WeixinSmallContentDetectionApiService {


    /**
     * 获取token用url
     */
    @Value("${weixin.small.access.token.url}")
    private String GET_ACCESS_TOKEN_URL;

    /**
     * 将获取回来的token存入redis，
     */
    @Value("${weixin.small.access.token.redis.key}")
    private String ACCESS_TOKEN_REDIS_KEY;
    /**
     * 过期时间设置为6000s
     */
    @Value("${weixin.small.access.token.redis.expires.time}")
    private Long ACCESS_TOKEN_REDIS_EXPIRE;

    /**
     * 文字检测链接
     */
    @Value("${weixin.small.check.msg_sec_check.url}")
    private String MSG_SEC_CHECK_URL;


    /**
     * 图片检测链接
     */
    @Value("${weixin.small.check.img_sec_check.url}")
    private String IMG_SEC_CHECK_URL;

    private static final Integer IMG_SEC_CHECK_WIDTH = 600;
    private static final Integer IMG_SEC_CHECK_HEIGHT = 1000;
    private static final Float IMG_SEC_CHECK_QUALITY = 0.25f;


    /**
     * 获取回来的accessToken的json中key值
     */
    private final String JSONOBJECT_KEY_WEIXIN_ACCESS_TOKEN = "access_token";


    @Autowired
    private WeChatPropertiesValue weChatPropertiesValue;

    @Autowired
    private MyHttpClientUtil myHttpClientUtil;
    @Autowired
    private MyRedisUtil myRedisUtil;
    @Autowired
    private MyThumbnailImgOptionUtil myThumbnailImgOptionUtil;


    /**
     * 违规图片检测
     *
     * @param multipartFile 图片
     * @param accessToken   微信token 填写null系统会自动请求
     * @return boolean值
     * @throws MyDefinitionException
     */
    @Override
    public Boolean checkImgSecCheckApi(MultipartFile multipartFile, String accessToken) throws MyDefinitionException {
        if (multipartFile == null) {
            throw new MyDefinitionException("检查图片违规要求图片内容必填");
        }
        JSONObject msgSecCheckApiMsg = null;
        try {
            msgSecCheckApiMsg = getImgSecCheckApiMsg(multipartFile, accessToken);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException("判断图片是否违规失败，" + e.getMessage());
        }
        if (msgSecCheckApiMsg.containsKey("errcode") && msgSecCheckApiMsg.getInteger("errcode").equals(0)) {
            return true;
        }
        return false;
    }


    /**
     * 违规图片检测
     *
     * @param multipartFile 图片
     * @param accessToken   微信token 填写null系统会自动请求
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public JSONObject getImgSecCheckApiMsg(MultipartFile multipartFile, String accessToken) throws MyDefinitionException {
        if (multipartFile == null) {
            throw new MyDefinitionException("检查图片违规要求图片内容必填");
        }
        if (StringUtils.isBlank(accessToken)) {
            JSONObject accessToken1 = null;
            try {
                accessToken1 = getAccessToken();
            } catch (MyDefinitionException e) {
                throw new MyDefinitionException(e.getMessage());
            }
            accessToken = accessToken1.getString(JSONOBJECT_KEY_WEIXIN_ACCESS_TOKEN);
        }
        /*生成待检测的文件*/
        File testFile = null;
        try {
            testFile = new File(multipartFile.getOriginalFilename());
            InputStream inputStream = myThumbnailImgOptionUtil.compressImage(multipartFile,
                    MyThumbnailImgType.IMAGE_TYPE_PNG,
                    IMG_SEC_CHECK_WIDTH, IMG_SEC_CHECK_HEIGHT, IMG_SEC_CHECK_QUALITY);
            myThumbnailImgOptionUtil.inputStreamToFile(inputStream, testFile);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        Map<String, File> postFiles = new HashMap<>(1);
        postFiles.put("media", testFile);
        String url = IMG_SEC_CHECK_URL + accessToken;
        String imgCheckResult = null;
        try {
            imgCheckResult = myHttpClientUtil.uploadFileByHttpPost(url, postFiles, null, null);
        } catch (Exception e) {
            log.error("违规图片检测请求失败，失败原因：{}", e.toString());
            throw new MyDefinitionException("违规图片检测请求失败");
        } finally {
            /*删除临时文件*/
            if (testFile.exists()) {
                testFile.delete();
            }
        }
        log.info("微信图片检查结果:{}", imgCheckResult);
        JSONObject jsonObject = null;
        try {
            jsonObject = JSONObject.parseObject(imgCheckResult);
        } catch (Exception e) {
            log.error("违规图片检测数据转换失败，错误原因：{}", e.toString());
            throw new MyDefinitionException("违规图片检测数据转换失败");
        }

        if (jsonObject == null) {
            throw new MyDefinitionException("没有得到违规图片检测结果");
        }
        return jsonObject;
    }

    /**
     * 违规文字检测
     *
     * @param msgText     文字
     * @param accessToken 微信token 填写null系统会自动请求
     * @return boolean值
     * @throws MyDefinitionException
     */
    @Override
    public Boolean checkMsgSecCheckApi(String msgText, String accessToken) throws MyDefinitionException {
        if (StringUtils.isBlank(msgText)) {
            throw new MyDefinitionException("判断文字是否违规要求文字内容必填");
        }
        JSONObject msgSecCheckApiMsg = null;
        try {
            msgSecCheckApiMsg = getMsgSecCheckApiMsg(msgText, accessToken);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException("判断文字是否违规失败，" + e.getMessage());
        }
        if (msgSecCheckApiMsg.containsKey("errcode") && msgSecCheckApiMsg.getInteger("errcode").equals(0)) {
            return true;
        }
        return false;
    }

    /**
     * 违规文字检测
     *
     * @param msgText     文字
     * @param accessToken 微信token 填写null系统会自动请求
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public JSONObject getMsgSecCheckApiMsg(String msgText, String accessToken) throws MyDefinitionException {
        if (StringUtils.isBlank(msgText)) {
            throw new MyDefinitionException("检查文字违规要求文字内容必填");
        }
        Map<String, Object> parameterMap = new HashMap<>(1);
        if (StringUtils.isBlank(accessToken)) {
            JSONObject accessToken1 = null;
            try {
                accessToken1 = getAccessToken();
            } catch (MyDefinitionException e) {
                throw new MyDefinitionException(e.getMessage());
            }
            accessToken = accessToken1.getString(JSONOBJECT_KEY_WEIXIN_ACCESS_TOKEN);
        }
        parameterMap.put("content", msgText);
        String url = MSG_SEC_CHECK_URL + accessToken;
        String msgCheckResult = null;
        try {
            msgCheckResult = myHttpClientUtil.postJson(url, parameterMap, null);
        } catch (Exception e) {
            log.error("违规文字检测请求失败，失败原因：{}", e.toString());
            throw new MyDefinitionException("违规文字检测请求失败");
        }
        log.info("微信文字检查结果:{}", msgCheckResult);
        JSONObject jsonObject = null;
        try {
            jsonObject = JSONObject.parseObject(msgCheckResult);
        } catch (Exception e) {
            log.error("违规文字检测数据转换失败，错误原因：{}", e.toString());
            throw new MyDefinitionException("违规文字检测数据转换失败");
        }

        if (jsonObject == null) {
            throw new MyDefinitionException("没有得到违规文字检测结果");
        }

        //{"errcode":87014,"errmsg":"risky content hint: [5JmdqHuhE-C6JiIa]"} 错误的结果
        //{"errcode":0,"errmsg":"ok"}
        return jsonObject;
    }

    /**
     * 获取小程序全局唯一后台接口调用凭据（access_token）
     * 调用绝大多数后台接口时都需使用 access_token，开发者需要进行妥善保存
     * https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
     *
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public JSONObject getAccessToken() throws MyDefinitionException {

        Object o = myRedisUtil.get(ACCESS_TOKEN_REDIS_KEY);
        if (o != null) {
            JSONObject jsonObject = null;
            String redisValue = String.valueOf(o);
            try {
                jsonObject = JSONObject.parseObject(redisValue);
            } catch (Exception e) {
                log.error("从redis获取微信token转换数据失败，错误原因：{}", e.toString());
                throw new MyDefinitionException("从redis获取微信token转换数据失败");
            }
            return jsonObject;
        }

        Map<String, Object> parameterMap = new HashMap<>(3);
        parameterMap.put("grant_type", "client_credential");
        parameterMap.put("appid", weChatPropertiesValue.getAppid());
        parameterMap.put("secret", weChatPropertiesValue.getAppSecret());
        String resultAccessToken = null;
        try {
            resultAccessToken = myHttpClientUtil.getParameter(GET_ACCESS_TOKEN_URL, parameterMap, null);
        } catch (Exception e) {
            log.error("获取微信token请求失败，失败原因：{}", e.toString());
            throw new MyDefinitionException("获取微信token请求失败");
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = JSONObject.parseObject(resultAccessToken);
        } catch (Exception e) {
            log.error("获取微信token转换数据失败，错误原因：{}", e.toString());
            throw new MyDefinitionException("获取微信token转换数据失败");
        }

        if (jsonObject == null) {
            throw new MyDefinitionException("获取微信token失败");
        } else {
            if (jsonObject.containsKey("access_token")) {
                myRedisUtil.set(ACCESS_TOKEN_REDIS_KEY, jsonObject, ACCESS_TOKEN_REDIS_EXPIRE);
            } else {
                throw new MyDefinitionException("从微信系统中拉取的数据中并没有包含token数据，而是" + resultAccessToken + "" +
                        "，该数据不符合要求");
            }
        }
        return jsonObject;
    }
}
