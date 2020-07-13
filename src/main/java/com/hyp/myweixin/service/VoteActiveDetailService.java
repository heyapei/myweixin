package com.hyp.myweixin.service;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.vo.page.PageEditWorkDetailVO;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/13 20:40
 * @Description: TODO
 */
public interface VoteActiveDetailService {


    /**
     * 通过活动id获取活动编辑页的第一页内容
     *
     * @param workId 活动ID
     * @return
     * @throws MyDefinitionException
     */
    PageEditWorkDetailVO getPageEditWorkDetailVOByWorkId(Integer workId) throws MyDefinitionException;

}
