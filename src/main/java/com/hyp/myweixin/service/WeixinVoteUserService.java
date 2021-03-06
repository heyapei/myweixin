package com.hyp.myweixin.service;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.modal.WeixinVoteUser;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/5 19:28
 * @Description: TODO
 */
public interface WeixinVoteUserService {


    /**
     * 判断当前用户是否拥有完整权限 是否被禁用了 如果没有问题则不会被catch住
     *
     * @param id 用户ID
     * @throws MyDefinitionException
     */
    void validateUserRight(Integer id) throws MyDefinitionException;


    /**
     * 测试事务
     *
     * @param weixinVoteUser 微信用户信息
     * @return 主键
     */
    int testTransactional(WeixinVoteUser weixinVoteUser);


    /**
     * 添加用户信息
     *
     * @param weixinVoteUser 微信用户信息
     * @return 主键
     */
    int addWechatInfo(WeixinVoteUser weixinVoteUser);

    /**
     * 通过表主键获取用户信息
     *
     * @param id 主键voteUser
     * @return 信息详情
     */
    WeixinVoteUser getUserById(Integer id);

    /**
     * 通过openId获取用户信息
     * 每个openId对应一个用户，且不可以重复
     *
     * @param openId
     * @return 信息详情
     */
    WeixinVoteUser getUserByOpenId(String openId);


    /**
     * 根据用户openId更新用户信息
     *
     * @param weixinVoteUser
     * @return
     */
    Integer updateWeixinUserByOpenId(WeixinVoteUser weixinVoteUser);


}
