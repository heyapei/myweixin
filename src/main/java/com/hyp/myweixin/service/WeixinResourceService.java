package com.hyp.myweixin.service;

import com.hyp.myweixin.pojo.dto.WeixinResource;

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


}
