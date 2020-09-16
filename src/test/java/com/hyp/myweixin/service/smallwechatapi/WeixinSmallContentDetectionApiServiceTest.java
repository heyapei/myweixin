package com.hyp.myweixin.service.smallwechatapi;

import com.alibaba.fastjson.JSONObject;
import com.hyp.myweixin.utils.thumbnailator.MyThumbnailImgOptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/25 20:00
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class WeixinSmallContentDetectionApiServiceTest {

    @Autowired
    private WeixinSmallContentDetectionApiService weixinSmallContentDetectionApiService;
    @Autowired
    private MyThumbnailImgOptionUtil myThumbnailImgOptionUtil;


    @Test
    public void testgetQrCode() {

        String accessTokenByAppName = weixinSmallContentDetectionApiService.getAccessTokenByAppName("qubaoming");
        log.info("获取的token:{}", accessTokenByAppName);
       /* byte[] qrCode = weixinSmallContentDetectionApiService.getQrCode(accessTokenByAppName);
        log.info("查询结果：{}", qrCode);*/

        /*String  qrCode = weixinSmallContentDetectionApiService.getQrCode(accessTokenByAppName);
        log.info("查询结果：{}", qrCode);*/

        String quBaoMingQrCodeUnlimited = weixinSmallContentDetectionApiService.getQuBaoMingQrCodeUnlimited("a=11", "pages/index/index");
        log.info("查询结果：{}", quBaoMingQrCodeUnlimited);


        /*try {
            URL url = new URL("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessTokenByAppName);
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
            *//**
             * line_color生效
             * paramJson.put("auto_color", false);
             * JSONObject lineColor = new JSONObject();
             * lineColor.put("r", 0);
             * lineColor.put("g", 0);
             * lineColor.put("b", 0);
             * paramJson.put("line_color", lineColor);
             * *//*

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
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }

    @Test
    public void testmsgSecCheckApi() {
        JSONObject jsonObject = weixinSmallContentDetectionApiService.getMsgSecCheckApiMsg("", null);
        log.info("查询结果：{}", jsonObject);
    }

    @Test
    public void getAccessToken() {
        JSONObject accessToken = weixinSmallContentDetectionApiService.getAccessToken();

        //35_63LOgkyE7IzOlPPQ3uOn-M7YLILLFuBYp3lVOdZXRzVhrCOXZOiGe9FxGq_GYDtRDV0pUro5Q0hc8cnn5v2EtS37BJQcxms9xb4XXWtcE0TQV4yqh13jCQBIsP2lWdgTG7UB4oB35ZgXFb-tYFRiAHAHOA
        //35_63LOgkyE7IzOlPPQ3uOn-M7YLILLFuBYp3lVOdZXRzVhrCOXZOiGe9FxGq_GYDtRDV0pUro5Q0hc8cnn5v2EtS37BJQcxms9xb4XXWtcE0TQV4yqh13jCQBIsP2lWdgTG7UB4oB35ZgXFb-tYFRiAHAHOA
        log.info("查询结果：{}", accessToken);
    }

    @Test
    public void checkImgSecCheckApi() {

        File file = new File("C:\\Users\\heyapei\\Pictures\\overwatch1 - 副本.png");
        Map<String, File> postFiles = new HashMap<>();
        MultipartFile multipartFile = myThumbnailImgOptionUtil.fileToMultipartFile(file);
        postFiles.put("media", file);

        Boolean aBoolean = weixinSmallContentDetectionApiService.checkImgSecCheckApi(multipartFile, null);
        log.info("查询结果：{}", aBoolean);
    }
}