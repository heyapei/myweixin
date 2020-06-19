package com.hyp.myweixin.service;

import com.hyp.myweixin.pojo.modal.WeixinVoteUserWork;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/14 18:57
 * @Description: TODO
 */
public interface WeixinVoteUserWorkService {


    /**
     * 保存用户对某个作品的投票记录
     *
     * @param weixinVoteUserWork
     * @return
     */
    int addUserVote(WeixinVoteUserWork weixinVoteUserWork);


    /**
     * 保存用户对某个作品的投票记录
     *
     * @param workId
     * @return
     */
    List<WeixinVoteUserWork> getWeixinVoteUserWorkByWorkId(Integer workId);

}
