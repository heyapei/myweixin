package com.hyp.myweixin.service;

import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.query.voteactive.OwnerActiveQuery;
import com.hyp.myweixin.pojo.vo.page.activeeditor.ActiveWorkForOwnerVO;
import com.hyp.myweixin.pojo.vo.page.activeeditor.ActiveWorkOverviewForOwnerVO;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/26 17:28
 * @Description: TODO
 */
public interface UserActiveService {


    /**
     * 获取用户活动的大致信息
     *
     * @param userId 用户ID
     * @return
     * @throws MyDefinitionException
     */
    ActiveWorkOverviewForOwnerVO getActiveWorkOverviewForOwnerVOByUserId(Integer userId) throws MyDefinitionException;

    /**
     * 查询用户名下所有的活动
     * 分页查询
     *
     * @param ownerActiveQuery
     * @return
     * @throws MyDefinitionException
     */
    PageInfo<ActiveWorkForOwnerVO> getActiveWorkForOwnerVOListByUserId(OwnerActiveQuery ownerActiveQuery) throws MyDefinitionException;

}
