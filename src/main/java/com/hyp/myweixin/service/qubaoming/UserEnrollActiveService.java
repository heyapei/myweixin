package com.hyp.myweixin.service.qubaoming;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.utils.MyErrorList;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/18 19:37
 * @Description: TODO
 */
public interface UserEnrollActiveService {

    /*妈的写错了，enroll应该是报名留着的 但是搞成了收藏了 卧槽*/


    /**
     * 获取活动的报名人的头像数据
     * @param activeId
     * @param pageNum
     * @param pageSize
     * @return
     * @throws MyDefinitionException
     */
    PageInfo<Object> getSignUpUserInfoByActiveIdPage(Integer activeId,Integer pageNum,Integer pageSize) throws MyDefinitionException;




    /**
     * 用户报名参加活动
     * 判断用户是否有权限
     * 判断填报的信息是否符合活动规定
     * 1. 写入报名数据
     * 2. 添加参与人数
     *
     * @param userId
     * @param activeId
     * @param signUpOption 报名的填报信息
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    Integer addSignUpByActiveIdAndUserId(Integer userId, Integer activeId, String signUpOption) throws MyDefinitionException;


    /**
     * 获取指定活动要求的必填项目
     *
     * @param activeId
     * @return 返回的是String类型，前端判断是否包含某个字段用于显示
     * @throws MyDefinitionException
     */
    String getSignUpOption(Integer activeId) throws MyDefinitionException;

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
