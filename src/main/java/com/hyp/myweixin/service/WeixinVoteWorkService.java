package com.hyp.myweixin.service;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/8 17:30
 * @Description: TODO
 */
public interface WeixinVoteWorkService {
    /**
     * 通过voteBaseId获取当前这个活动有多少人参加
     *
     * @param voteBaseId
     * @return
     */
    Integer getCountWorkByVoteBaseId(Integer voteBaseId);


    /**
     * 通过voteBaseId获取当前这个活动有多少人投票
     *
     * @param voteBaseId
     * @return
     */
    Integer getCountVoteByVoteBaseId(Integer voteBaseId);

}
