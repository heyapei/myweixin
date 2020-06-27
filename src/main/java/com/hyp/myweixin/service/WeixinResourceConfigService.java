package com.hyp.myweixin.service;

import com.hyp.myweixin.pojo.modal.WeixinResourceConfig;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/27 23:50
 * @Description: TODO
 */
public interface WeixinResourceConfigService {

    /**
     * 通过资源Id进行查找内容
     * @param id
     * @return
     */
    WeixinResourceConfig getWeixinResourceConfigById(Integer id);

}
