package com.hyp.myweixin.utils.impl;


import com.alibaba.fastjson.JSONObject;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.utils.MyHttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2019/10/28 10:05
 * @Description: TODO
 */
@Slf4j
@Service
public class MyHttpClientUtilImpl implements MyHttpClientUtil {


    /**
     * @param url       地址
     * @param postFiles 文件数据
     * @param postParam 同时携带的参数
     * @param headMap   请求头信息 （没有请求头信息填写null）
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public String uploadFileByHttpPost(String url,
                                       Map<String, File> postFiles,
                                       Map<String, String> postParam,
                                       Map<String, String> headMap) throws MyDefinitionException {
        return uploadFileByHttpPost(url, postFiles, postParam, headMap, -1, -1, -1);
    }


    /**
     * @param url                      地址
     * @param postFiles                文件数据
     * @param postParam                同时携带的参数
     * @param headMap                  请求头信息 （没有请求头信息填写null）
     * @param connectionRequestTimeout httpClient请求连接池请求超时时间设置（毫秒 要求大于0）
     * @param socketTimeout            数据传输超时时间设置（毫秒 要求大于0）
     * @param connectTimeout           请求连接超时时间设置（毫秒 要求大于0）
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public String uploadFileByHttpPost(String url,
                                       Map<String, File> postFiles,
                                       Map<String, String> postParam,
                                       Map<String, String> headMap,
                                       int connectionRequestTimeout, int socketTimeout, int connectTimeout) throws MyDefinitionException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            //把一个普通参数和文件上传给下面这个api接口
            HttpPost httpPost = new HttpPost(url);


            if (headMap != null) {
                for (Map.Entry<String, String> entry : headMap.entrySet()) {
                    httpPost.addHeader(entry.getKey(), entry.getValue());
                }
            }
            // 设置请求和传输超时时间 默认10s
            if (connectionRequestTimeout <= 0) {
                connectionRequestTimeout = 5000;
            }
            if (socketTimeout <= 0) {
                socketTimeout = 5000;
            }
            if (connectTimeout <= 0) {
                connectTimeout = 5000;
            }
            RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout)
                    .setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
            httpPost.setConfig(requestConfig);

            //设置传输参数,设置编码。设置浏览器兼容模式，解决文件名乱码问题
            MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create().setMode(HttpMultipartMode.RFC6532);
            for (Map.Entry<String, File> entry : postFiles.entrySet()) {
                FileBody fundFileBin = new FileBody(entry.getValue(), ContentType.MULTIPART_FORM_DATA);
                //相当于<input type="file" name="media"/>
                multipartEntity.addPart(entry.getKey(), fundFileBin);
            }

            if (postParam != null) {
                //把文件转换成流对象FileBody
                Set<String> keySet = postParam.keySet();
                for (String key : keySet) {
                    //解决中文乱码
                    ContentType contentType = ContentType.create("text/plain", Charset.forName("UTF-8"));
                    StringBody stringBody = new StringBody(postParam.get(key), contentType);
                    multipartEntity.addPart(key, stringBody);
                    // multipartEntity.addTextBody(key, postParam.get(key));//这个中文会乱码
                }
            }

            HttpEntity reqEntity = multipartEntity.build();
            httpPost.setEntity(reqEntity);
            //发起请求 并返回请求的响应
            CloseableHttpResponse response = httpClient.execute(httpPost);
            try {
                //打印响应状态
                //resultMap.put("statusCode", response.getStatusLine().getStatusCode());
                //获取响应对象
                HttpEntity resEntity = response.getEntity();
                /*if (resEntity != null) {
                    //打印响应内容
                    resultMap.put("data", EntityUtils.toString(resEntity, Charset.forName("UTF-8")));
                }*/
                String result = null;
                if (response != null && response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    result = entityToString(entity);
                }
                return result;
            } catch (Exception e) {
                log.error("httpclient文件上传过程错误，错误原因:{}", e.toString());
                throw new MyDefinitionException("httpclient文件上传过程错误");
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            log.error("httpclient文件上传过程错误，错误原因:{}", e.toString());
            throw new MyDefinitionException("httpclient文件上传过程错误");
        } catch (IOException e) {
            log.error("httpclient文件上传过程IO错误，错误原因:{}", e.toString());
            throw new MyDefinitionException("httpclient文件上传过程IO错误");
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error("httpclient文件上传过程关闭源错误，错误原因:{}", e.toString());
                throw new MyDefinitionException("httpclient文件上传过程关闭源错误");
            }
        }
    }


    /**
     * 发送post请求，参数用map接收
     *
     * @param url                      地址
     * @param postMapParameter         参数（没有参数填写null）
     * @param headMap                  请求头信息 （没有请求头信息填写null）
     * @param connectionRequestTimeout httpClient请求连接池请求超时时间设置（毫秒 要求大于0）
     * @param socketTimeout            数据传输超时时间设置（毫秒 要求大于0）
     * @param connectTimeout           请求连接超时时间设置（毫秒 要求大于0）
     */
    @Override
    public String postMap(String url, Map<String, String> postMapParameter, Map<String, String> headMap,
                          int connectionRequestTimeout, int socketTimeout, int connectTimeout) throws MyDefinitionException {
        if (!isHttpUrl(url)) {
            log.error("https请求的url错误，请求地址为：{}", url);
            return "请求地址格式错误";
        }
        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        List<NameValuePair> pairs = new ArrayList<>();
        if (postMapParameter != null) {
            for (Map.Entry<String, String> entry : postMapParameter.entrySet()) {
                pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        CloseableHttpResponse response = null;
        try {
            post.setEntity(new UrlEncodedFormEntity(pairs, StandardCharsets.UTF_8));

            if (headMap != null) {
                for (Map.Entry<String, String> entry : headMap.entrySet()) {
                    post.addHeader(entry.getKey(), entry.getValue());
                }
            }
            // 设置请求和传输超时时间 默认10s
            if (connectionRequestTimeout <= 0) {
                connectionRequestTimeout = 5000;
            }
            if (socketTimeout <= 0) {
                socketTimeout = 5000;
            }
            if (connectTimeout <= 0) {
                connectTimeout = 5000;
            }
            RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout)
                    .setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
            post.setConfig(requestConfig);
            response = httpClient.execute(post);
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                result = entityToString(entity);
            }
            return result;
        } catch (UnsupportedEncodingException e) {
            log.error("发送请求时出现错误,错误理由是：{}", e.toString());
            throw new MyDefinitionException("发送请求时出现错误");
        } catch (ClientProtocolException e) {
            log.error("发送请求时出现错误,错误理由是：{}", e.toString());
            throw new MyDefinitionException("发送请求时出现错误");

        } catch (IOException e) {
            log.error("发送请求时出现错误,错误理由是：{}", e.toString());
            throw new MyDefinitionException("发送请求时出现错误");
        } finally {
            try {
                httpClient.close();
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                log.error("关闭输出流出现错误,错误理由是：{}", e.toString());
                throw new MyDefinitionException("关闭输出流出现错误");
            }

        }
    }

    /**
     * 发送post请求，参数用map接收 默认连接超时限制时间5S
     *
     * @param url              地址
     * @param postMapParameter 参数（没有参数填写null）
     * @param headMap          请求头信息 （没有请求头信息填写null）
     * @return 响应
     */
    @Override
    public String postMap(String url, Map<String, String> postMapParameter, Map<String, String> headMap)
            throws MyDefinitionException {
        return postMap(url, postMapParameter, headMap, -1, -1, -1);
    }

    /**
     * get请求，参数放在map里
     *
     * @param url                      请求地址
     * @param getMapParameter          参数map（没有参数填写null）
     * @param headMap                  请求头信息 （没有请求头信息填写null）
     * @param connectionRequestTimeout httpClient请求连接池请求超时时间设置（毫秒 要求大于0）
     * @param socketTimeout            数据传输超时时间设置（毫秒 要求大于0）
     * @param connectTimeout           请求连接超时时间设置（毫秒 要求大于0）
     * @return 响应
     */
    @Override
    public String getMap(String url, Map<String, String> getMapParameter, Map<String, String> headMap,
                         int connectionRequestTimeout, int socketTimeout, int connectTimeout)
            throws MyDefinitionException {
        if (!isHttpUrl(url)) {
            log.error("https请求的url错误，请求地址为：{}", url);
            return "请求地址格式错误";
        }
        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        List<NameValuePair> pairs = new ArrayList<>();
        if (getMapParameter != null) {
            for (Map.Entry<String, String> entry : getMapParameter.entrySet()) {
                pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        CloseableHttpResponse response = null;
        try {
            URIBuilder builder = new URIBuilder(url);
            builder.setParameters(pairs);
            HttpGet get = new HttpGet(builder.build());

            if (headMap != null) {
                for (Map.Entry<String, String> entry : headMap.entrySet()) {
                    get.addHeader(entry.getKey(), entry.getValue());
                }
            }
            // 设置请求和传输超时时间 默认10s
            if (connectionRequestTimeout <= 0) {
                connectionRequestTimeout = 5000;
            }
            if (socketTimeout <= 0) {
                socketTimeout = 5000;
            }
            if (connectTimeout <= 0) {
                connectTimeout = 5000;
            }
            RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout)
                    .setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
            get.setConfig(requestConfig);

            response = httpClient.execute(get);
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                result = entityToString(entity);
            }
            return result;
        } catch (URISyntaxException e) {
            log.error("发送请求时出现错误,错误理由是：{}", e.toString());
            throw new MyDefinitionException("发送请求时出现错误");
        } catch (ClientProtocolException e) {
            log.error("发送请求时出现错误,错误理由是：{}", e.toString());
            throw new MyDefinitionException("发送请求时出现错误");
        } catch (IOException e) {
            log.error("发送请求时出现错误,错误理由是：{}", e.toString());
            throw new MyDefinitionException("发送请求时出现错误");
        } finally {
            try {
                httpClient.close();
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                log.error("关闭输出流出现错误,错误理由是：{}", e.toString());
                throw new MyDefinitionException("关闭输出流出现错误");
            }
        }
    }

    /**
     * get请求，参数放在map里 默认连接超时限制时间5S
     *
     * @param url             请求地址
     * @param getMapParameter 参数map（没有参数填写null）
     * @param headMap         请求头信息 （没有请求头信息填写null）
     * @return 响应
     */
    @Override
    public String getMap(String url, Map<String, String> getMapParameter, Map<String, String> headMap)
            throws MyDefinitionException {
        return getMap(url, getMapParameter, headMap,
                -1, -1, -1);
    }

    /**
     * GET请求 如果有参数请按照map格式处理key-value对应好即可
     *
     * @param url                      链接地址
     * @param parameterMap             可以为空，但是要求传过来空的map参数，map为空(null)时表示没有参数
     * @param headMap                  请求头信息 （没有请求头信息填写null）
     * @param connectionRequestTimeout httpClient请求连接池请求超时时间设置（毫秒 要求大于0）
     * @param socketTimeout            数据传输超时时间设置（毫秒 要求大于0）
     * @param connectTimeout           请求连接超时时间设置（毫秒 要求大于0）
     * @return 响应
     */
    @Override
    public String getParameter(String url, Map<String, Object> parameterMap, Map<String, String> headMap,
                               int connectionRequestTimeout, int socketTimeout, int connectTimeout)
            throws MyDefinitionException {
        if (!isHttpUrl(url)) {
            log.error("https请求的url错误，请求地址为：{}", url);
            return "请求地址格式错误";
        }
        if (parameterMap != null) {
            url += "?";
            int i = 0;
            for (String key : parameterMap.keySet()) {
                try {
                    url += key + "=" + URLEncoder.encode(parameterMap.get(key).toString(), "UTF-8");
                } catch (UnsupportedEncodingException e) {

                }
                i++;
                if (i <= parameterMap.size() - 1) {
                    url += "&";
                }
            }
        }

        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        CloseableHttpResponse response = null;
        try {
            if (headMap != null) {
                for (Map.Entry<String, String> entry : headMap.entrySet()) {
                    get.addHeader(entry.getKey(), entry.getValue());
                }
            }
            // 设置请求和传输超时时间 默认10s
            if (connectionRequestTimeout <= 0) {
                connectionRequestTimeout = 5000;
            }
            if (socketTimeout <= 0) {
                socketTimeout = 5000;
            }
            if (connectTimeout <= 0) {
                connectTimeout = 5000;
            }
            RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout)
                    .setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
            get.setConfig(requestConfig);

            response = httpClient.execute(get);
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                result = entityToString(entity);
            }
            return result;
        } catch (IOException e) {
            log.error("发送请求时出现错误,错误理由是：{}", e.toString());
            throw new MyDefinitionException("发送请求时出现错误");
        } finally {
            try {
                httpClient.close();
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                log.error("关闭输出流出现错误,错误理由是：{}", e.toString());
                throw new MyDefinitionException("关闭输出流出现错误");
            }
        }
    }

    /**
     * GET请求 如果有参数请按照map格式处理key-value对应好即可 默认连接超时限制时间5S
     *
     * @param url          链接地址
     * @param parameterMap 可以为空，但是要求传过来空的map参数，map为空(null)时表示没有参数
     * @param headMap      请求头信息 （没有请求头信息填写null）
     * @return 响应
     */
    @Override
    public String getParameter(String url, Map<String, Object> parameterMap, Map<String, String> headMap)
            throws MyDefinitionException {
        return getParameter(url, parameterMap, headMap,
                -1, -1, -1);
    }

    /**
     * post请求，参数为json字符串
     *
     * @param url                      请求地址
     * @param jsonMap                  用map集合封装好的json数据 （key-value数据）（没有参数填写null）
     * @param headMap                  请求头信息 （没有请求头信息填写null）
     * @param connectionRequestTimeout httpClient请求连接池请求超时时间设置（毫秒 要求大于0）
     * @param socketTimeout            数据传输超时时间设置（毫秒 要求大于0）
     * @param connectTimeout           请求连接超时时间设置（毫秒 要求大于0）
     * @return 响应
     */
    @Override
    public String postJson(String url, Map<String, Object> jsonMap, Map<String, String> headMap,
                           int connectionRequestTimeout, int socketTimeout, int connectTimeout)
            throws MyDefinitionException {
        if (!isHttpUrl(url)) {
            log.error("https请求的url错误，请求地址为：{}", url);
            return "请求地址格式错误";
        }
        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        CloseableHttpResponse response = null;
        try {

            if (jsonMap != null) {
                post.setEntity(
                        new ByteArrayEntity(new JSONObject(jsonMap).toString().getBytes(StandardCharsets.UTF_8)));
            }
            if (headMap != null) {
                for (Map.Entry<String, String> entry : headMap.entrySet()) {
                    post.addHeader(entry.getKey(), entry.getValue());
                }
            }
            // 设置请求和传输超时时间 默认10s
            if (connectionRequestTimeout <= 0) {
                connectionRequestTimeout = 5000;
            }
            if (socketTimeout <= 0) {
                socketTimeout = 5000;
            }
            if (connectTimeout <= 0) {
                connectTimeout = 5000;
            }
            RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(connectionRequestTimeout)
                    .setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
            post.setConfig(requestConfig);
            response = httpClient.execute(post);
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                HttpEntity entity = response.getEntity();
                result = entityToString(entity);
            }
            return result;
        } catch (UnsupportedEncodingException e) {
            log.error("发送请求时出现错误,错误理由是：{}", e.toString());
            throw new MyDefinitionException("发送请求时出现错误");
        } catch (ClientProtocolException e) {
            log.error("发送请求时出现错误,错误理由是：{}", e.toString());
            throw new MyDefinitionException("发送请求时出现错误");
        } catch (IOException e) {
            log.error("发送请求时出现错误,错误理由是：{}", e.toString());
            throw new MyDefinitionException("发送请求时出现错误");
        } finally {
            try {
                httpClient.close();
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                log.error("关闭输出流出现错误,错误理由是：{}", e.toString());
                throw new MyDefinitionException("关闭输出流出现错误");
            }
        }
    }

    /**
     * post请求，参数为json字符串 默认连接超时限制时间5S
     *
     * @param url     请求地址
     * @param jsonMap 用map集合封装好的json数据 （key-value数据）（没有参数填写null）
     * @param headMap 请求头信息 （没有请求头信息填写null）
     * @return 响应
     */
    @Override
    public String postJson(String url, Map<String, Object> jsonMap, Map<String, String> headMap)
            throws MyDefinitionException {
        return postJson(url, jsonMap, headMap,
                -1, -1, -1);
    }

    /**
     * 处理返回结果
     *
     * @param entity
     * @return
     * @throws IOException
     */
    private String entityToString(HttpEntity entity) throws IOException {
        String result = null;
        if (entity != null) {
            long lenth = entity.getContentLength();
            if (lenth != -1 && lenth < 2048) {
                result = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            } else {
                InputStreamReader reader1 = new InputStreamReader(entity.getContent(), StandardCharsets.UTF_8);
                CharArrayBuffer buffer = new CharArrayBuffer(2048);
                char[] tmp = new char[1024];
                int l;
                while ((l = reader1.read(tmp)) != -1) {
                    buffer.append(tmp, 0, l);
                }
                result = buffer.toString();
            }
            EntityUtils.consume(entity);
        }
        return result;
    }


    /**
     * 判断字符串是否为URL
     *
     * @param urls 用户头像key
     * @return true:是URL、false:不是URL
     */
    private boolean isHttpUrl(String urls) {
        boolean isurl = false;
        if (StringUtils.isBlank(urls)) {
            return isurl;
        }
        //设置正则表达式
        String regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))"
                + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)";
        //比对
        Pattern pat = Pattern.compile(regex.trim());
        Matcher mat = pat.matcher(urls.trim());
        //判断是否匹配
        isurl = mat.matches();
        if (isurl) {
            isurl = true;
        }
        return isurl;
    }


}
