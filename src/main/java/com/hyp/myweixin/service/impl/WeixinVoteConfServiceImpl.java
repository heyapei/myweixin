package com.hyp.myweixin.service.impl;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.mapper.WeixinVoteConfMapper;
import com.hyp.myweixin.pojo.modal.WeixinVoteConf;
import com.hyp.myweixin.service.WeixinVoteConfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/1 18:59
 * @Description: TODO
 */
@Service
@Slf4j
public class WeixinVoteConfServiceImpl implements WeixinVoteConfService {


    @Autowired
    private WeixinVoteConfMapper weixinVoteConfMapper;

    /**
     * 通过活动的ID查询配置文件
     *
     * @param voteWorkId
     * @return
     */
    @Override
    public WeixinVoteConf getWeixinVoteConfByVoteWorkId(Integer voteWorkId) {
        Example example = new Example(WeixinVoteConf.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("activeVoteBaseId", voteWorkId);
        WeixinVoteConf weixinVoteConf = null;
        try {
            weixinVoteConf = weixinVoteConfMapper.selectOneByExample(example);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("通过活动的ID查询配置文件错误，错误原因：{}", e.toString());
        }

        return weixinVoteConf;
    }

    /**
     * 保存活动配置项
     *
     * @param weixinVoteConf
     * @return
     */
    @Override
    public Integer saveWeixinVoteConf(WeixinVoteConf weixinVoteConf) {

        int i = 0;
        try {
            i = weixinVoteConfMapper.insertSelective(weixinVoteConf);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("保存活动配置项错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("保存活动配置项错误");
        }
        return i;
    }
}
