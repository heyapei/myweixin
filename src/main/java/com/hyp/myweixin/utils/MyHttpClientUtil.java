package com.hyp.myweixin.utils;

import java.util.Map;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/5/28 20:34
 * @Description: TODO
 */
public interface MyHttpClientUtil {


    /**
     * 发送post请求，参数用map接收
     *
     * @param url                      地址
     * @param postMapParameter         参数（没有参数填写null）
     * @param headMap                  请求头信息 （没有请求头信息填写null）
     * @param connectionRequestTimeout httpClient请求连接池请求超时时间设置（毫秒 要求大于0）
     * @param socketTimeout            数据传输超时时间设置（毫秒 要求大于0）
     * @param connectTimeout           请求连接超时时间设置（毫秒 要求大于0）
     * @return 响应
     */
    String postMap(String url, Map<String, String> postMapParameter, Map<String, String> headMap,
                   int connectionRequestTimeout, int socketTimeout, int connectTimeout);

    /**
     * 发送post请求，参数用map接收 默认连接超时限制时间5S
     *
     * @param url              地址
     * @param postMapParameter 参数（没有参数填写null）
     * @param headMap          请求头信息 （没有请求头信息填写null）
     * @return 响应
     */
    String postMap(String url, Map<String, String> postMapParameter, Map<String, String> headMap);

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
    String getMap(String url, Map<String, String> getMapParameter, Map<String, String> headMap,
                  int connectionRequestTimeout, int socketTimeout, int connectTimeout);

    /**
     * get请求，参数放在map里 默认连接超时限制时间5S
     *
     * @param url             请求地址
     * @param getMapParameter 参数map（没有参数填写null）
     * @param headMap         请求头信息 （没有请求头信息填写null）
     * @return 响应
     */
    String getMap(String url, Map<String, String> getMapParameter, Map<String, String> headMap);

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
    String getParameter(String url, Map<String, Object> parameterMap, Map<String, String> headMap,
                        int connectionRequestTimeout, int socketTimeout, int connectTimeout);

    /**
     * GET请求 如果有参数请按照map格式处理key-value对应好即可 默认连接超时限制时间5S
     *
     * @param url          链接地址
     * @param parameterMap 可以为空，但是要求传过来空的map参数，map为空(null)时表示没有参数
     * @param headMap      请求头信息 （没有请求头信息填写null）
     * @return 响应
     */
    String getParameter(String url, Map<String, Object> parameterMap, Map<String, String> headMap);

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
    String postJson(String url, Map<String, Object> jsonMap, Map<String, String> headMap,
                    int connectionRequestTimeout, int socketTimeout, int connectTimeout);

    /**
     * post请求，参数为json字符串 默认连接超时限制时间5S
     *
     * @param url     请求地址
     * @param jsonMap 用map集合封装好的json数据 （key-value数据）（没有参数填写null）
     * @param headMap 请求头信息 （没有请求头信息填写null）
     * @return 响应
     */
    String postJson(String url, Map<String, Object> jsonMap, Map<String, String> headMap);
}
