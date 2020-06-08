package com.hyp.myweixin.mapper;

import com.hyp.myweixin.pojo.modal.WeixinVoteWork;
import com.hyp.myweixin.utils.MyMapper;
import org.springframework.stereotype.Repository;


/**
 * @author heyapei
 */
@Repository
public interface WeixinVoteWorkMapper extends MyMapper<WeixinVoteWork> {

    Integer getCountVoteByVoteBaseId(Integer voteBaseId);
}