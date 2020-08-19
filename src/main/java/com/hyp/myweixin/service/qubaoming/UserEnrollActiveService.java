package com.hyp.myweixin.service.qubaoming;

import com.hyp.myweixin.exception.MyDefinitionException;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/18 19:37
 * @Description: TODO
 */
public interface UserEnrollActiveService {

    /**
     * 用户收藏活动
     * 1. 删除收藏
     * 2. 减少收藏数
     *
     * @param userId
     * @param activeId
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    Integer UserUnEnrollActive(Integer userId, Integer activeId) throws MyDefinitionException;




    /**
     * 用户收藏活动
     * 1. 判断用户权限
     * 2. 判断活动是否存在
     * 3. 添加收藏
     * 4. 添加收藏数
     *
     * @param userId
     * @param activeId
     * @return 用户收藏活动的主键信息
     * @throws MyDefinitionException
     */
    Integer UserEnrollActive(Integer userId, Integer activeId) throws MyDefinitionException;


}
