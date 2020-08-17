package com.hyp.myweixin.service.qubaoming;

import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.query.company.CompanyUserOptionQuery;
import com.hyp.myweixin.pojo.qubaoming.query.company.ShowUserCollectionPageQuery;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/17 21:50
 * @Description: TODO
 */
public interface CompanyUserOptionService {


    /**
     * 分页查询用户收藏的公司主体列表
     *
     * @param showUserCollectionPageQuery
     * @return 收藏的主键id
     * @throws MyDefinitionException
     */
    PageInfo showCollectionListByUserId(ShowUserCollectionPageQuery showUserCollectionPageQuery) throws MyDefinitionException;


    /**
     * 添加用户对公司主体的收藏
     * 1. 首先检查公司是否存在
     * 2. 添加当前用户对公司的收藏（如果已经收藏了 就不再操作了）
     * 3. 更新公司数据表中的收藏数
     *
     * @param companyUserOptionQuery
     * @return 收藏的主键id
     * @throws MyDefinitionException
     */
    Integer addUserCollectionCompany(CompanyUserOptionQuery companyUserOptionQuery) throws MyDefinitionException;

    /**
     * 删除用户对公司主体的收藏
     * 1. 首先检查公司是否存在
     * 2. 添加当前用户对公司的收藏（如果已经收藏了 就不再操作了）
     * 3. 更新公司数据表中的收藏数
     *
     * @param companyUserOptionQuery
     * @return 收藏的主键id
     * @throws MyDefinitionException
     */
    Integer delUserCollectionCompany(CompanyUserOptionQuery companyUserOptionQuery) throws MyDefinitionException;


}
