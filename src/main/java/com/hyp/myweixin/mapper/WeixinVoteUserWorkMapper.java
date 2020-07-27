package com.hyp.myweixin.mapper;

import com.hyp.myweixin.pojo.modal.WeixinVoteUserWork;
import com.hyp.myweixin.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface WeixinVoteUserWorkMapper extends MyMapper<WeixinVoteUserWork> {


    /**
     * 通过活动基础表 用户openId 查询用户在这个活动中的投票情况 有时间范围
     *
     * @param baseId    活动ID
     * @param openId    用户标识
     * @param workId    作品ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 在时间范围内容 某个用户在一定时间范围内的投票总数
     */
    Integer getWeixinVoteUserWorkNumByOpenIdTime(@Param("baseId") Integer baseId,
                                                 @Param("openId") String openId,
                                                 @Param("workId") Integer workId,
                                                 @Param("startTime") Date startTime,
                                                 @Param("endTime") Date endTime);


    /**
     * 按照openId查询用户投票的作品ID
     *
     * @param openId             用户ID
     * @param distinctUserWorkId 　是否筛选过滤　true筛选 false不筛选
     * @return
     */
    List<Integer> getUserVoteWorkId(@Param("openId") String openId,
                                       @Param("distinctUserWorkId") boolean distinctUserWorkId);


}