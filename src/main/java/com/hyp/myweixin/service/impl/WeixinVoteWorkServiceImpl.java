package com.hyp.myweixin.service.impl;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.mapper.WeixinVoteWorkMapper;
import com.hyp.myweixin.pojo.modal.WeixinVoteWork;
import com.hyp.myweixin.service.WeixinVoteWorkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/8 17:36
 * @Description: TODO
 */
@Service
@Slf4j
public class WeixinVoteWorkServiceImpl implements WeixinVoteWorkService {

    @Autowired
    private WeixinVoteWorkMapper weixinVoteWorkMapper;

    /**
     * 通过voteBaseId获取当前这个活动有多少人参加
     *
     * @param voteBaseId
     * @return
     */
    @Override
    public Integer getCountWorkByVoteBaseId(Integer voteBaseId) {
        int i = 0;
        Example example = new Example(WeixinVoteWork.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("activeVoteBaseId", voteBaseId);
        List<WeixinVoteWork> weixinVoteWorks = null;
        try {
            weixinVoteWorks = weixinVoteWorkMapper.selectByExample(example);
        } catch (Exception e) {
            log.error("通过voteBaseId获取当前这个活动有多少人参加失败，错误原因{}", e.toString());
            throw new MyDefinitionException("通过voteBaseId获取当前这个活动有多少人参加失败");
        }
        if (weixinVoteWorks != null) {
            i = weixinVoteWorks.size();
        }
        return i;
    }

    /**
     * 通过voteBaseId获取当前这个活动有多少人投票
     *
     * @param voteBaseId
     * @return
     */
    @Override
    public Integer getCountVoteByVoteBaseId(Integer voteBaseId) {
        try {
            return weixinVoteWorkMapper.getCountVoteByVoteBaseId(voteBaseId);
        } catch (Exception e) {
            log.error("通过voteBaseId获取当前这个活动有多少人投票查询失败，错误原因{}", e.toString());
            throw new MyDefinitionException("通过voteBaseId获取当前这个活动有多少人投票查询失败");
        }
    }
}
