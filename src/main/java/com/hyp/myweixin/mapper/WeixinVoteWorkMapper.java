package com.hyp.myweixin.mapper;

import com.hyp.myweixin.pojo.modal.WeixinVoteWork;
import com.hyp.myweixin.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
 * @author heyapei
 */
@Repository
public interface WeixinVoteWorkMapper extends MyMapper<WeixinVoteWork> {

    /**
     * 查看总的投票次数
     * @param voteBaseId
     * @return
     */
    Integer getCountVoteByVoteBaseId(Integer voteBaseId);

    /**
     * 查询作品当前排名
     * @param voteBaseId
     * @param userWorkId
     * @return
     */
    Integer getRankNumByUserWorkId(@Param("voteBaseId")Integer voteBaseId,@Param("userWorkId") Integer userWorkId);
}