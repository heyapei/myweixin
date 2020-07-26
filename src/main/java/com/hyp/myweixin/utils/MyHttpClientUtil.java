package com.hyp.myweixin.utils;

import com.hyp.myweixin.exception.MyDefinitionException;

import java.io.File;
import java.util.Map;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/5/28 20:34
 * @Description: TODO
 */
public interface MyHttpClientUtil {


    /**
     * post请求的同时发送文件 默认超时时间5秒
     *
     * @param url       地址
     * @param postFiles 文件数据
     * @param postParam 同时携带的参数
     * @param headMap   请求头信息 （没有请求头信息填写null）
     * @return
     * @throws MyDefinitionException
     */
    String uploadFileByHttpPost(String url,
                                Map<String, File> postFiles,
                                Map<String, String> postParam,
                                Map<String, String> headMap) throws MyDefinitionException;

    /**
     * post请求的同时发送文件
     *
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
    String uploadFileByHttpPost(String url,
                                Map<String, File> postFiles,
                                Map<String, String> postParam,
                                Map<String, String> headMap,
                                int connectionRequestTimeout, int socketTimeout, int connectTimeout) throws MyDefinitionException;


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
     * @throws MyDefinitionException
     */
    String postMap(String url, Map<String, String> postMapParameter, Map<String, String> headMap,
                   int connectionRequestTimeout, int socketTimeout, int connectTimeout) throws MyDefinitionException;

    /**
     * 发送post请求，参数用map接收 默认连接超时限制时间5S
     *
     * @param url              地址
     * @param postMapParameter 参数（没有参数填写null）
     * @param headMap          请求头信息 （没有请求头信息填写null）
     * @return 响应
     * @throws MyDefinitionException
     */
    String postMap(String url, Map<String, String> postMapParameter, Map<String, String> headMap) throws MyDefinitionException;

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
     * @throws MyDefinitionException
     */
    String getMap(String url, Map<String, String> getMapParameter, Map<String, String> headMap,
                  int connectionRequestTimeout, int socketTimeout, int connectTimeout) throws MyDefinitionException;

    /**
     * get请求，参数放在map里 默认连接超时限制时间5S
     *
     * @param url             请求地址
     * @param getMapParameter 参数map（没有参数填写null）
     * @param headMap         请求头信息 （没有请求头信息填写null）
     * @return 响应
     * @throws MyDefinitionException
     */
    String getMap(String url, Map<String, String> getMapParameter, Map<String, String> headMap) throws MyDefinitionException;

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
     * @throws MyDefinitionException
     */
    String getParameter(String url, Map<String, Object> parameterMap, Map<String, String> headMap,
                        int connectionRequestTimeout, int socketTimeout, int connectTimeout) throws MyDefinitionException;

    /**
     * GET请求 如果有参数请按照map格式处理key-value对应好即可 默认连接超时限制时间5S
     *
     * @param url          链接地址
     * @param parameterMap 可以为空，但是要求传过来空的map参数，map为空(null)时表示没有参数
     * @param headMap      请求头信息 （没有请求头信息填写null）
     * @return 响应
     * @throws MyDefinitionException
     */
    String getParameter(String url, Map<String, Object> parameterMap, Map<String, String> headMap) throws MyDefinitionException;

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
     * @throws MyDefinitionException
     */
    String postJson(String url, Map<String, Object> jsonMap, Map<String, String> headMap,
                    int connectionRequestTimeout, int socketTimeout, int connectTimeout) throws MyDefinitionException;

    /**
     * post请求，参数为json字符串 默认连接超时限制时间5S
     *
     * @param url     请求地址
     * @param jsonMap 用map集合封装好的json数据 （key-value数据）（没有参数填写null）
     * @param headMap 请求头信息 （没有请求头信息填写null）
     * @return 响应
     * @throws MyDefinitionException
     */
    String postJson(String url, Map<String, Object> jsonMap, Map<String, String> headMap) throws MyDefinitionException;
}
