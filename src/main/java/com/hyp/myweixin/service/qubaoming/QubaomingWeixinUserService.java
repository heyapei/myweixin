package com.hyp.myweixin.service.qubaoming;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingWeixinUser;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/15 22:57
 * @Description: TODO
 */
public interface QubaomingWeixinUserService {


    /**
     * 判断当前用户是否拥有完整权限 是否被禁用了 如果没有问题则不会被catch住
     *
     * @param id 用户ID
     * @throws MyDefinitionException
     */
    void validateUserRight(Integer id) throws MyDefinitionException;


    /**
     * 通过OpenId查询用户信息
     *
     * @param openId 微信的唯一值
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    QubaomingWeixinUser getQubaomingWeixinUserByOpenId(String openId) throws MyDefinitionException;



    /*通用*/

    /**
     * 创建趣报名用户基础信息 并返回主键信息 要求参数必须是完整的数据
     *
     * @param qubaomingWeixinUser
     * @return 主键
     * @throws MyDefinitionException
     */
    Integer insertReturnPk(QubaomingWeixinUser qubaomingWeixinUser) throws MyDefinitionException;

    /**
     * 根据主键删除趣报名数据
     *
     * @param pkId 主键
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    Integer deleteByPk(Integer pkId) throws MyDefinitionException;

    /**
     * 更新有值的趣报名数据信息 要求必须有主键信息
     *
     * @param qubaomingWeixinUser
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    Integer updateSelectiveQubaomingWeixinUserBase(QubaomingWeixinUser qubaomingWeixinUser) throws MyDefinitionException;

    /**
     * 通过主键查找趣报名数据 如果没有返回null
     *
     * @param pkId 主键
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    QubaomingWeixinUser selectByPkId(Integer pkId) throws MyDefinitionException;


}
