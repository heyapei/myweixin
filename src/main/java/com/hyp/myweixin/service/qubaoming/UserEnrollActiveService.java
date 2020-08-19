package com.hyp.myweixin.service.qubaoming;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.utils.MyErrorList;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/18 19:37
 * @Description: TODO
 */
public interface UserEnrollActiveService {

    /*妈的写错了，enroll应该是报名留着的 但是搞成了收藏了 卧槽*/

    /**
     * 验证用户是否可以报名
     * 1. 是否在时间范围内
     * 2. 是否已经报名了
     * 3. 报名名额是否已满
     * 4. 人员是否拥有权限
     * 5. 活动是否已上线
     *
     * @param userId
     * @param activeId
     * @return 处理结果 为null就说明可以 不为null就要处理
     * @throws MyDefinitionException
     */
    MyErrorList ValidateUserSignActive(Integer userId, Integer activeId) throws MyDefinitionException;


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
