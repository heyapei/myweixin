package com.hyp.myweixin.service.qubaoming;

import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.query.pcenter.UserCreateActiveQuery;
import com.hyp.myweixin.pojo.qubaoming.query.pcenter.UserEnrollQuery;
import com.hyp.myweixin.pojo.qubaoming.vo.pcenter.UserNumPreVO;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/31 14:17
 * @Description: TODO
 */
public interface PersonCenterService {


    /**
     * 分页查询用户创建的活动列表
     *
     * @param userId 查询条件
     * @return
     * @throws MyDefinitionException
     */
    UserNumPreVO getPersonCenterNumPreView(Integer userId) throws MyDefinitionException;

    /**
     * 分页查询用户创建的活动列表
     *
     * @param userCreateActiveQuery 查询条件
     * @return
     * @throws MyDefinitionException
     */
    PageInfo getUserCreateActiveList(UserCreateActiveQuery userCreateActiveQuery) throws MyDefinitionException;

    /**
     * 分页查询用户报名列表
     *
     * @param userEnrollQuery 查询条件
     * @return
     * @throws MyDefinitionException
     */
    PageInfo getUserSignUpPageInfo(UserEnrollQuery userEnrollQuery) throws MyDefinitionException;


    /**
     * 分页查询用户收藏列表
     *
     * @param userEnrollQuery 查询条件
     * @return
     * @throws MyDefinitionException
     */
    PageInfo getUserEnrollPageInfo(UserEnrollQuery userEnrollQuery) throws MyDefinitionException;

}
