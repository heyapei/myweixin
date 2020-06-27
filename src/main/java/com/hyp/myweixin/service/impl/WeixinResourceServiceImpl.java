package com.hyp.myweixin.service.impl;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.mapper.WeixinResourceConfigMapper;
import com.hyp.myweixin.mapper.WeixinResourceMapper;
import com.hyp.myweixin.pojo.modal.WeixinResource;
import com.hyp.myweixin.service.WeixinResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/5/28 22:20
 * @Description: TODO
 */
@Service
@Slf4j
public class WeixinResourceServiceImpl implements WeixinResourceService {

    @Autowired
    private WeixinResourceMapper weixinResourceMapper;
    @Autowired
    private WeixinResourceConfigMapper weixinResourceConfigMapper;

    /**
     * 通过WeixinResourceConfig的Id查询所有的数据符合状态值的数据
     *
     * @param configId 类型配置数据
     * @param status   状态值
     * @return
     */
    @Override
    public List<WeixinResource> getWeixinResourceByConfigId(int configId, int status) {
        Example example = new Example(WeixinResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("status", status);
        criteria.andEqualTo("resourceConfigId", configId);
        try {
            return weixinResourceMapper.selectByExample(example);
        } catch (Exception e) {
            log.error(e.toString());
            throw new MyDefinitionException("执行错误异常");
        }
    }

    /**
     * 保存资源文件
     *
     * @param weixinResource
     * @return 主键ID
     */
    @Override
    public Integer addWeixinResource(WeixinResource weixinResource) {
        if (weixinResource == null) {
            return null;
        }
        int i = 0;
        try {
            i = weixinResourceMapper.insertUseGeneratedKeys(weixinResource);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("保存资源文件错误，错误原因：{}", e.toString());
        }
        if (i > 0) {
            return weixinResource.getId();
        }
        return null;
    }

    /**
     * 通过md5值获取数据
     *
     * @param md5 文件md5值
     * @return
     */
    @Override
    public WeixinResource getWeixinResourceByMD5(String md5) {
        Example example = new Example(WeixinResource.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("md5", md5);
        try {
            return weixinResourceMapper.selectOneByExample(example);
        } catch (Exception e) {
            log.error(e.toString());
            throw new MyDefinitionException("通过md5值获取数据错误");
        }
    }
}
