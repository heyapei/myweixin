package com.hyp.myweixin.service.qubaoming;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.model.WechatCompany;
import com.hyp.myweixin.pojo.qubaoming.query.company.CompanyCreateQuery;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/15 15:38
 * @Description: TODO
 */
public interface WechatCompanyService {


    /**
     * 通过Example查询条件进行查询
     * @param example 查询条件
     * @return 列表数据
     * @throws MyDefinitionException
     */
    List<WechatCompany> selectListByExample(Example example) throws MyDefinitionException;



    /**
     * 通过用户ID查询用户下的主办方信息
     *
     * @param userId 用户ID
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    List<WechatCompany> selectListByUserId(Integer userId) throws MyDefinitionException;


    /*通用*/

    /**
     * 创建主办方基础信息 并返回主键信息 要求参数必须是完整的数据
     *
     * @param wechatCompany
     * @return 主键
     * @throws MyDefinitionException
     */
    Integer insertReturnPk(WechatCompany wechatCompany) throws MyDefinitionException;

    /**
     * 根据主键删除主办方数据
     *
     * @param pkId 主键
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    Integer deleteByPk(Integer pkId) throws MyDefinitionException;

    /**
     * 更新有值的主办方数据信息 要求必须有主键信息
     *
     * @param wechatCompany
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    Integer updateSelectiveWechatCompany(WechatCompany wechatCompany) throws MyDefinitionException;

    /**
     * 通过主键查找主办方数据 如果没有返回null
     *
     * @param pkId 主键
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    WechatCompany selectByPkId(Integer pkId) throws MyDefinitionException;

}
