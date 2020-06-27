package com.hyp.myweixin.service.impl;

import com.hyp.myweixin.mapper.WeixinResourceConfigMapper;
import com.hyp.myweixin.pojo.modal.WeixinResourceConfig;
import com.hyp.myweixin.service.WeixinResourceConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/27 23:51
 * @Description: TODO
 */
@Service
@Slf4j
public class WeixinResourceConfigServiceImpl implements WeixinResourceConfigService {

    @Autowired
    private WeixinResourceConfigMapper weixinResourceConfigMapper;


    /**
     * 通过资源Id进行查找内容
     *
     * @param id
     * @return
     */
    @Override
    public WeixinResourceConfig getWeixinResourceConfigById(Integer id) {

        WeixinResourceConfig weixinResourceConfig = null;
        try {
            weixinResourceConfig = weixinResourceConfigMapper.selectByPrimaryKey(id);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("通过资源Id进行查找内容错误，错误原因：{}", e.toString());

        }

        return weixinResourceConfig;
    }
}
