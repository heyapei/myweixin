package com.hyp.myweixin.mapper;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.modal.WeixinVoteBase;
import com.hyp.myweixin.utils.MyMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface WeixinVoteBaseMapper extends MyMapper<WeixinVoteBase> {

    /**
     * 更新活动描述和图片
     *
     * @param weixinVoteBase 实体类
     * @return 影响行数
     * @throws MyDefinitionException
     */
    Integer updateActiveRewardAndImg(WeixinVoteBase weixinVoteBase) ;


    /**
     * 更新活动描述和图片
     *
     * @param weixinVoteBase 实体类
     * @return 影响行数
     * @throws MyDefinitionException
     */
    Integer updateActiveDescAndImg(WeixinVoteBase weixinVoteBase);


    /**
     * 更新活动标题和图片
     *
     * @param weixinVoteBase 实体类
     * @return 影响行数
     * @throws MyDefinitionException
     */
    Integer updateActiveNameAndImg(WeixinVoteBase weixinVoteBase);


}