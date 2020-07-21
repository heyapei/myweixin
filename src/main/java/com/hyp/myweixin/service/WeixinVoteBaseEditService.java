package com.hyp.myweixin.service;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.vo.page.activeeditor.ActiveEditFirstVO;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/21 19:52
 * @Description: TODO 活动编辑接口
 */
public interface WeixinVoteBaseEditService {

    /**
     *通过活动ID查询活动第一页需要回显的数据
     * 需要用户登录 且 为管理员
     * @param activeId 活动ID
     * @param userId 用户ID
     * @return 第一页需要回显的数据
     * @throws MyDefinitionException
     */
    ActiveEditFirstVO getActiveEditFirstVOByActiveId(Integer activeId, Integer userId) throws MyDefinitionException;


}
