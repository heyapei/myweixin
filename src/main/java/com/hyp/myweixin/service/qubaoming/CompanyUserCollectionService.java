package com.hyp.myweixin.service.qubaoming;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.model.QuBaoMingCompanyUserCollection;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/17 21:57
 * @Description: TODO
 */
public interface CompanyUserCollectionService {


    /*通用*/

    /**
     * 通过查询条件删除用户对公司主体收藏数据
     *
     * @param example 查询条件
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    Integer deleteByExample(Example example) throws MyDefinitionException;

    /**
     * 创建用户对公司主体收藏 并返回主键信息 要求参数必须是完整的数据
     *
     * @param companyUserCollection
     * @return 主键
     * @throws MyDefinitionException
     */
    Integer insertReturnPk(QuBaoMingCompanyUserCollection companyUserCollection) throws MyDefinitionException;

    /**
     * 根据主键删除用户对公司主体收藏数据
     *
     * @param pkId 主键
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    Integer deleteByPk(Integer pkId) throws MyDefinitionException;

    /**
     * 更新有值的用户对公司主体收藏数据信息 要求必须有主键信息
     *
     * @param companyUserCollection
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    Integer updateSelectiveCompanyUserCollection(QuBaoMingCompanyUserCollection companyUserCollection) throws MyDefinitionException;

    /**
     * 通过主键查找用户对公司主体收藏数据 如果没有返回null
     *
     * @param pkId 主键
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    QuBaoMingCompanyUserCollection selectByPkId(Integer pkId) throws MyDefinitionException;


    /**
     * 通过查询条件查找一条用户对公司主体收藏数据 如果没有返回null
     *
     * @param example 查询条件
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    QuBaoMingCompanyUserCollection selectOneByExample(Example example) throws MyDefinitionException;


    /**
     * 通过查询条件查找所有用户对公司主体收藏数据 如果没有返回null
     *
     * @param example 查询条件
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    List<QuBaoMingCompanyUserCollection> selectAllByExample(Example example) throws MyDefinitionException;




}
