package com.hyp.myweixin;

import com.alibaba.fastjson.JSONObject;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.service.smallwechatapi.WeixinSmallContentDetectionApiService;
import com.hyp.myweixin.utils.MyHttpClientUtil;
import com.hyp.myweixin.utils.thumbnailator.MyThumbnailImgOptionUtil;
import com.hyp.myweixin.utils.thumbnailator.MyThumbnailImgType;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/26 9:41
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TestHttpClientFile {

    @Autowired
    private WeixinSmallContentDetectionApiService weixinSmallContentDetectionApiService;

    @Test
    public void testExcelExportRedis() throws Exception {
/*

        JSONObject accessToken = weixinSmallContentDetectionApiService.getAccessToken();
        String token = accessToken.getString("access_token");
*/


    }


    @Autowired
    private MyHttpClientUtil myHttpClientUtil;
    @Autowired
    private MyThumbnailImgOptionUtil myThumbnailImgOptionUtil;

    @Test
    public void testHttpClient() throws Exception {
      /*  JSONObject accessToken = weixinSmallContentDetectionApiService.getAccessToken();
        String token = accessToken.getString("access_token");


        // String url = "https://api.weixin.qq.com/wxa/img_sec_check?access_token=" + token;//上传地址
        String url = "https://api.weixin.qq.com/wxa/img_sec_check?access_token=" + token;//上传地址

        String[] filePaths = {"C:\\Users\\heyapei\\Pictures\\u=1511979372,3833363958&fm=26&gp=0.jpg"};//待上传的文件路径
*/

        //postFiles.put("media", new File("C:\\Users\\heyapei\\Pictures\\overwatch.png"));
        //postFiles.put("media", new File("C:\\Users\\heyapei\\Pictures\\u=1511979372,3833363958&fm=26&gp=0.jpg"));

        //File file = new File("C:\\Users\\heyapei\\Pictures\\u=1511979372,3833363958&fm=26&gp=0.jpg");
       /* File file = new File("C:\\Users\\heyapei\\Pictures\\overwatch.png");
        Map<String, File> postFiles = new HashMap<>();
        MultipartFile multipartFile = myThumbnailImgOptionUtil.fileToMultipartFile(file);
        Long fileSize = myThumbnailImgOptionUtil.getFileSize(file);
        InputStream inputStream = myThumbnailImgOptionUtil.compressImage(multipartFile,
                MyThumbnailImgType.IMAGE_TYPE_JPG,
                740, 1334, 0.8f);
        myThumbnailImgOptionUtil.inputStreamToFile(inputStream, file);
        postFiles.put("media", file);
        // Map<String, Object> map = uploadFileByHttpPost(url, postFiles, null);
        // log.info("chaxunjieguo:{}", map.toString());

        String s = myHttpClientUtil.uploadFileByHttpPost(url, postFiles, null, null);
        log.info("chaxunjieguo:{}", s.toString());*/
    }

    @Test
    public void getAccessToken() {

        //myRedisUtil.del(ACCESS_TOKEN_REDIS_KEY);
        JSONObject accessToken = weixinSmallContentDetectionApiService.getAccessToken();
        String string = accessToken.getString("access_token");
        //log.info(string);
        log.info("查询结果：{}",string);
        Map<String, String> parameterMap = new HashMap<>(2);
        parameterMap.put("begin_date", "20200729");
        parameterMap.put("end_date","20200729");
        String resultAccessToken = null;
        try {
            resultAccessToken = myHttpClientUtil.postMap("https://api.weixin.qq.com/datacube/getweanalysisappiddailyvisittrend?access_token="+string, parameterMap, null);
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
        log.info("查询结果：{}",resultAccessToken);

    }


}
