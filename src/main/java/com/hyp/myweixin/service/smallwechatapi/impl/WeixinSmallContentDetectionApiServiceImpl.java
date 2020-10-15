package com.hyp.myweixin.service.smallwechatapi.impl;

import cn.hutool.core.codec.Base64;
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
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
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
     * 趣投票的AppName
     */
    @Value("${qutoupiao.app.name}")
    private String QUTOUPIAO_APP_NAME;


    /**
     * 趣报名的AppName
     */
    @Value("${qubaoming.app.name}")
    private String QUBAOMING_APP_NAME;


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
     * 将获取回来的QUTOUPIAO token存入redis，
     */
    @Value("${weixin.small.qutoupiao.access.token.redis.key}")
    private String QUTOUPIAO_ACCESS_TOKEN_REDIS_KEY;

    /**
     * 将获取回来的QUBAOMING token存入redis，
     */
    @Value("${weixin.small.qubaoming.access.token.redis.key}")
    private String QUBAOMING_ACCESS_TOKEN_REDIS_KEY;


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
    /**
     * 二维码接口 C
     */
    @Value("${weixin.acode.create.qr_code.url}")
    private String CREATE_QR_CODE_URL;

    /**
     * qubaoming数量无限制的二维码图片存放地址
     */
    @Value("${weixin.acode.get.qubaoming.unlimited.img.path}")
    private String QUBAOMING_UNLIMITED_ACODE_IMG_PATH;
    /**
     * 二维码接口 A
     */
    @Value("${weixin.acode.get.url}")
    private String QR_CODE_GET_URL;
    /**
     * 二维码接口 B
     */
    @Value("${weixin.acode.get.unlimited.url}")
    private String QR_CODE_GET_UNLIMITED_URL;
    /**
     * 发送消息通知的接口地址
     */
    @Value("${weixin.message.send.url}")
    private String MESSAGE_SEND_URL;

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
     * 发送模板信息
     *
     * @param jsonObject 发送的参数
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public JSONObject sendQuBaoMingUserSubmitMessage(JSONObject jsonObject) throws MyDefinitionException {

        if (jsonObject == null) {
            throw new MyDefinitionException("发送的json参数不能为空");
        }
        JSONObject accessToken1 = null;
        try {
            accessToken1 = getQuBaoMingAccessToken();
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        String accessToken = accessToken1.getString(JSONOBJECT_KEY_WEIXIN_ACCESS_TOKEN);
        String url = MESSAGE_SEND_URL + accessToken;

        log.info("请求地址：{}",url);
        String msgCheckResult = null;
        try {
            msgCheckResult = myHttpClientUtil.postJson(url, jsonObject, null);
        } catch (Exception e) {
            log.error("发送消息通知失败，失败原因：{}，失败的信息是：{}", e.toString(), jsonObject.toString());
            throw new MyDefinitionException("发送消息通知失败");
        }
        log.info("发送消息通知结果:{}", msgCheckResult);
        JSONObject jsonObject1 = null;
        try {
            jsonObject1 = JSONObject.parseObject(msgCheckResult);
        } catch (Exception e) {
            log.error("发送消息通知结果数据转换失败，错误原因：{}", e.toString());
            throw new MyDefinitionException("发送消息通知结果数据转换失败");
        }
        if (jsonObject == null) {
            throw new MyDefinitionException("发送消息通知结果数据转换失败");
        }
        return jsonObject1;
    }

    /**
     * 获取无数量限制的二维码
     *
     * @param scene
     * @param page
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public String getQuBaoMingQrCodeUnlimited(String scene, String page) throws MyDefinitionException {

        String pageTemp = page;
        if (page != null) {
            page = page.replaceAll("/", "");
        } else {
            page = "defaultIndex";
        }
        String qrCodeFileName = scene + page + ".png";
        String pathBase = null;
        try {
            pathBase = ResourceUtils.getURL("classpath:").getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new MyDefinitionException("获取项目路径失败");
        }
        pathBase = pathBase + QUBAOMING_UNLIMITED_ACODE_IMG_PATH;

        File pathFile = new File(pathBase);
        //如果目录不存在
        if (!pathFile.exists()) {
            //创建目录
            pathFile.mkdirs();
        }

        String realFileName = QUBAOMING_UNLIMITED_ACODE_IMG_PATH + "/" + qrCodeFileName;

        String fileTemp = pathFile + "/" + qrCodeFileName;
        File file = new File(fileTemp);
        if (file.exists()) {
            return realFileName;
        } else {
            String accessTokenByAppName = getAccessTokenByAppName("qubaoming");
            String urlTemp = QR_CODE_GET_UNLIMITED_URL + accessTokenByAppName;
            String base64 = null;

            try {
                //URL url = new URL("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessTokenByAppName);
                URL url = new URL(urlTemp);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");// 提交模式
                // conn.setConnectTimeout(10000);//连接超时 单位毫秒
                // conn.setReadTimeout(2000);//读取超时 单位毫秒
                // 发送POST请求必须设置如下两行
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                // 获取URLConnection对象对应的输出流
                PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
                // 发送请求参数
                JSONObject paramJson = new JSONObject();
                paramJson.put("scene", scene);
                paramJson.put("page", pageTemp);
                paramJson.put("width", 430);
                paramJson.put("auto_color", true);
                /**
                 * line_color生效
                 * paramJson.put("auto_color", false);
                 * JSONObject lineColor = new JSONObject();
                 * lineColor.put("r", 0);
                 * lineColor.put("g", 0);
                 * lineColor.put("b", 0);
                 * paramJson.put("line_color", lineColor);
                 * */
                printWriter.write(paramJson.toString());
                // flush输出流的缓冲
                printWriter.flush();
                //开始获取数据
                BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
                OutputStream os = new FileOutputStream(new File(fileTemp));
                int len;
                byte[] arr = new byte[1024];
                while ((len = bis.read(arr)) != -1) {
                    os.write(arr, 0, len);
                    os.flush();
                }
                os.close();
                return realFileName;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return null;
    }

    /**
     * 获取二维码B信息
     *
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public String getQrCode(String accessToken) throws MyDefinitionException {

        if (StringUtils.isBlank(accessToken)) {
            JSONObject accessToken1 = null;
            try {
                accessToken1 = getAccessToken();
            } catch (MyDefinitionException e) {
                throw new MyDefinitionException(e.getMessage());
            }
            accessToken = accessToken1.getString(JSONOBJECT_KEY_WEIXIN_ACCESS_TOKEN);
        }

        //see(accessToken);

        String urlTemp = QR_CODE_GET_UNLIMITED_URL + accessToken;
        String base64 = null;

        try {
            //URL url = new URL("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessTokenByAppName);
            URL url = new URL(urlTemp);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");// 提交模式
            // conn.setConnectTimeout(10000);//连接超时 单位毫秒
            // conn.setReadTimeout(2000);//读取超时 单位毫秒
            // 发送POST请求必须设置如下两行
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
            // 发送请求参数
            JSONObject paramJson = new JSONObject();
            paramJson.put("scene", "a=1234567890");
            paramJson.put("page", "pages/index/index");
            paramJson.put("width", 430);
            paramJson.put("auto_color", true);
            /**
             * line_color生效
             * paramJson.put("auto_color", false);
             * JSONObject lineColor = new JSONObject();
             * lineColor.put("r", 0);
             * lineColor.put("g", 0);
             * lineColor.put("b", 0);
             * paramJson.put("line_color", lineColor);
             * */


            String path = null;
            try {
                path = ResourceUtils.getURL("classpath:").getPath();
                //log.info("系统路径：{}", path);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new MyDefinitionException("获取项目路径失败");
            }

            //String savePath = path + imgVideResConfig.getQuBaoMingActiveImgBasePath();


            printWriter.write(paramJson.toString());
            // flush输出流的缓冲
            printWriter.flush();
            //开始获取数据
            BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
            OutputStream os = new FileOutputStream(new File("C:\\Users\\heyapei\\Pictures\\Screenshots\\abc.png"));
            int len;
            byte[] arr = new byte[1024];
            while ((len = bis.read(arr)) != -1) {
                os.write(arr, 0, len);
                os.flush();
            }
            os.close();

            File file = new File("C:\\Users\\heyapei\\Pictures\\Screenshots\\abc.png");
            BufferedImage image = ImageIO.read(file);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", stream);
            base64 = Base64.encode(stream.toByteArray());


        } catch (Exception e) {
            e.printStackTrace();
        }


        //log.info("获取二维码B的结果:{}", base64);

        return base64;
    }

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
        parameterMap.put("appid", weChatPropertiesValue.getQuTouPiaoAppId());
        parameterMap.put("secret", weChatPropertiesValue.getQuTouPiaoAppSecret());
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


    /**
     * 获取趣投票的accessToken
     * 获取小程序全局唯一后台接口调用凭据（access_token）
     * 调用绝大多数后台接口时都需使用 access_token，开发者需要进行妥善保存
     * https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
     *
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public JSONObject getQuTouPiaoAccessToken() throws MyDefinitionException {

        Object o = myRedisUtil.get(QUTOUPIAO_ACCESS_TOKEN_REDIS_KEY);
        if (o != null) {
            JSONObject jsonObject = null;
            String redisValue = String.valueOf(o);
            try {
                jsonObject = JSONObject.parseObject(redisValue);
            } catch (Exception e) {
                log.error("从redis获取微信趣投票token转换数据失败，错误原因：{}", e.toString());
                throw new MyDefinitionException("从redis获取微信趣投票token转换数据失败");
            }
            return jsonObject;
        }

        Map<String, Object> parameterMap = new HashMap<>(3);
        parameterMap.put("grant_type", "client_credential");
        parameterMap.put("appid", weChatPropertiesValue.getQuTouPiaoAppId());
        parameterMap.put("secret", weChatPropertiesValue.getQuTouPiaoAppSecret());
        String resultAccessToken = null;
        try {
            resultAccessToken = myHttpClientUtil.getParameter(GET_ACCESS_TOKEN_URL, parameterMap, null);
        } catch (Exception e) {
            log.error("获取微信趣投票token请求失败，失败原因：{}", e.toString());
            throw new MyDefinitionException("获取微信趣投票token请求失败");
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = JSONObject.parseObject(resultAccessToken);
        } catch (Exception e) {
            log.error("获取微信趣投票token转换数据失败，错误原因：{}", e.toString());
            throw new MyDefinitionException("获取微信趣投票token转换数据失败");
        }

        if (jsonObject == null) {
            throw new MyDefinitionException("获取微信趣投票token失败");
        } else {
            if (jsonObject.containsKey("access_token")) {
                myRedisUtil.set(QUTOUPIAO_ACCESS_TOKEN_REDIS_KEY, jsonObject, ACCESS_TOKEN_REDIS_EXPIRE);
            } else {
                throw new MyDefinitionException("从微信系统中拉取的数据中并没有包含趣投票token数据，而是" + resultAccessToken + "" +
                        "，该数据不符合要求");
            }
        }
        return jsonObject;
    }

    /**
     * 获取趣报名的accessToken
     * 获取小程序全局唯一后台接口调用凭据（access_token）
     * 调用绝大多数后台接口时都需使用 access_token，开发者需要进行妥善保存
     * https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
     *
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public JSONObject getQuBaoMingAccessToken() throws MyDefinitionException {
        Object o = myRedisUtil.get(QUBAOMING_ACCESS_TOKEN_REDIS_KEY);
        if (o != null) {
            JSONObject jsonObject = null;
            String redisValue = String.valueOf(o);
            try {
                jsonObject = JSONObject.parseObject(redisValue);
            } catch (Exception e) {
                log.error("从redis获取微信趣报名token转换数据失败，错误原因：{}", e.toString());
                throw new MyDefinitionException("从redis获取微信趣报名token转换数据失败");
            }
            return jsonObject;
        }

        Map<String, Object> parameterMap = new HashMap<>(3);
        parameterMap.put("grant_type", "client_credential");
        parameterMap.put("appid", weChatPropertiesValue.getQuBaoMingAppId());
        parameterMap.put("secret", weChatPropertiesValue.getQuBaoMingAppSecret());
        String resultAccessToken = null;
        try {
            resultAccessToken = myHttpClientUtil.getParameter(GET_ACCESS_TOKEN_URL, parameterMap, null);
        } catch (Exception e) {
            log.error("获取微信趣报名token请求失败，失败原因：{}", e.toString());
            throw new MyDefinitionException("获取微信趣报名token请求失败");
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = JSONObject.parseObject(resultAccessToken);
        } catch (Exception e) {
            log.error("获取微信趣报名token转换数据失败，错误原因：{}", e.toString());
            throw new MyDefinitionException("获取微信趣报名token转换数据失败");
        }

        if (jsonObject == null) {
            throw new MyDefinitionException("获取微信趣报名token失败");
        } else {
            if (jsonObject.containsKey("access_token")) {
                myRedisUtil.set(QUBAOMING_ACCESS_TOKEN_REDIS_KEY, jsonObject, ACCESS_TOKEN_REDIS_EXPIRE);
            } else {
                throw new MyDefinitionException("从微信系统中拉取的趣报名数据中并没有包含token数据，而是" + resultAccessToken + "" +
                        "，该数据不符合要求");
            }
        }
        return jsonObject;
    }


    /**
     * 通过appName获取对应的accessToken
     *
     * @param appName 小程序名称
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public String getAccessTokenByAppName(String appName) throws MyDefinitionException {

        String accessToken = null;
        if (StringUtils.isNotBlank(appName)) {
            JSONObject accessToken1 = null;
            try {

                if (appName.equalsIgnoreCase(QUBAOMING_APP_NAME)) {
                    accessToken1 = getQuBaoMingAccessToken();
                } else if (appName.equalsIgnoreCase(QUTOUPIAO_APP_NAME)) {
                    accessToken1 = getQuTouPiaoAccessToken();
                } else {
                    throw new MyDefinitionException("并没有定义该appName值" + appName);
                }
            } catch (MyDefinitionException e) {
                throw new MyDefinitionException(e.getMessage());
            }
            accessToken = accessToken1.getString(JSONOBJECT_KEY_WEIXIN_ACCESS_TOKEN);
        } else {
            throw new MyDefinitionException("必须指定程序名称");
        }
        return accessToken;
    }
}
