package com.hyp.myweixin.service;

import com.hyp.myweixin.pojo.modal.WeixinVoteConf;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/1 18:46
 * @Description: TODO
 */
public interface WeixinVoteConfService {

    /**
     * 保存活动配置项
     *
     * @param weixinVoteConf
     * @return
     */
    Integer saveWeixinVoteConf(WeixinVoteConf weixinVoteConf);


    /**
     * 通过活动的ID查询配置文件
     *
     * @param voteWorkId
     * @return
     */
    WeixinVoteConf getWeixinVoteConfByVoteWorkId(Integer voteWorkId);
}
