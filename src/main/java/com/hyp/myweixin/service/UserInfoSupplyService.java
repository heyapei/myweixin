package com.hyp.myweixin.service;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.modal.WeixinUserInfoSupply;
import com.hyp.myweixin.pojo.query.user.supply.AddUserSupplyQuery;
import com.hyp.myweixin.pojo.vo.page.user.UserInfoSupplyDetailVO;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/27 20:54
 * @Description: TODO
 */
public interface UserInfoSupplyService {

    /**
     * 更新微信补充信息
     *
     * @param addUserSupplyQuery 微信小程序提交上来的用户补充信息
     * @return 返回影响的行数
     * @throws MyDefinitionException
     */
    Integer updateWeixinUserInfoSupplyByAddUserSupplyQuery(AddUserSupplyQuery addUserSupplyQuery) throws MyDefinitionException;


    /**
     * 通过用户Id查找到用户的补充信息
     *
     * @param userId 用户ID
     * @return 返回主键
     * @throws MyDefinitionException
     */
    UserInfoSupplyDetailVO getUserInfoSupplyDetailVOByUserId(Integer userId) throws MyDefinitionException;


    /**
     * 添加微信补充信息 要求全部数据
     * 该操作只适用于第一次提交数据
     * 不允许重复提交
     *
     * @param addUserSupplyQuery 微信小程序提交上来的用户补充信息
     * @return 返回主键
     * @throws MyDefinitionException
     */
    Integer addWeixinUserInfoSupply(AddUserSupplyQuery addUserSupplyQuery) throws MyDefinitionException;

    /*通用方法*/

    /**
     * 添加微信补充信息 要求全部数据
     *
     * @param weixinUserInfoSupply 微信补充信息全部数据
     * @return 返回主键
     * @throws MyDefinitionException
     */
    Integer addWeixinUserInfoSupply(WeixinUserInfoSupply weixinUserInfoSupply) throws MyDefinitionException;

    /**
     * 按照主键删除数据
     *
     * @param pkId 含主键
     * @return 影响行数
     * @throws MyDefinitionException
     */
    Integer deleteByPkId(Integer pkId) throws MyDefinitionException;

    /**
     * 按照主键更新数据 只更新有值的数据
     *
     * @param weixinUserInfoSupply 要更新的数据（含主键）
     * @return 影响行数
     * @throws MyDefinitionException
     */
    Integer updateSelectiveByPkId(WeixinUserInfoSupply weixinUserInfoSupply) throws MyDefinitionException;


    /**
     * 按照UserId更新数据 只更新有值的数据
     *
     * @param weixinUserInfoSupply 要更新的数据（含userId）
     * @return 影响行数
     * @throws MyDefinitionException
     */
    Integer updateSelectiveByUserId(WeixinUserInfoSupply weixinUserInfoSupply) throws MyDefinitionException;


    /**
     * 按照主键查找数据
     *
     * @param pkId 主键
     * @return 数据
     * @throws MyDefinitionException
     */
    WeixinUserInfoSupply selectByPkId(Integer pkId) throws MyDefinitionException;

    /**
     * 按照用户ID查找数据
     *
     * @param userId 用户ID
     * @return 数据
     * @throws MyDefinitionException
     */
    WeixinUserInfoSupply selectByUserId(Integer userId) throws MyDefinitionException;


}
