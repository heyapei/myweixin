package com.hyp.myweixin.service;

import com.hyp.myweixin.pojo.modal.WeixinResource;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/5/28 22:11
 * @Description: TODO
 */
public interface WeixinResourceService {

    /**
     * 通过WeixinResourceConfig的Id查询所有的数据符合状态值的数据
     *
     * @param configId 类型配置数据
     * @param status   状态值
     * @return
     */
    List<WeixinResource> getWeixinResourceByConfigId(int configId, int status);


    /**
     * 通过md5值获取数据
     *
     * @param md5 文件md5值
     * @return
     */
    WeixinResource getWeixinResourceByMD5(String md5);

    /**
     * 通过md5值配合文件配置类型获取数据
     *
     * @param md5              文件md5值
     * @param resourceConfigId 文件配置类型
     * @return
     */
    WeixinResource getWeixinResourceByMD5AndConfigId(String md5, Integer resourceConfigId);

    /**
     * 保存资源文件
     *
     * @param weixinResource
     * @return 主键ID
     */
    Integer addWeixinResource(WeixinResource weixinResource);


}
