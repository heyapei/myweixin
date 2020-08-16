package com.hyp.myweixin.service;

import com.hyp.myweixin.exception.MyDefinitionException;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/23 19:39
 * @Description: TODO 管理员操作功能实现
 */
public interface AdministratorsOptionService {

    /**
     * 趣投票
     * 通过userId判断这个userId的用户是不是超级管理员
     * @param userId 用户ID
     * @return 是否为超级管理员
     * @throws MyDefinitionException
     */
    boolean isSuperAdministrators(Integer userId) throws MyDefinitionException;


    /**
     * 判断是否为趣报名的超级管理员
     * @param userId
     * @return
     * @throws MyDefinitionException
     */
    boolean isQuBaoMingSuperAdministrators(Integer userId) throws MyDefinitionException;


}
