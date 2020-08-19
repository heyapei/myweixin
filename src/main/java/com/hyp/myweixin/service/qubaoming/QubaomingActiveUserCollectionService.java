package com.hyp.myweixin.service.qubaoming;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingActiveUserCollection;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/18 18:59
 * @Description: TODO
 */
public interface QubaomingActiveUserCollectionService {


    /**
     * 删除符合条件的用户收藏公司数据
     *
     * @param example 查询条件
     * @return 影响行数
     * @throws MyDefinitionException
     */
    Integer deleteQubaomingActiveUserCollectionByExample(Example example) throws MyDefinitionException;



    /**
     * 根据查询条件查询一条用户收藏公司结果
     *
     * @param example 查询条件
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    QubaomingActiveUserCollection selectOneQubaomingActiveUserCollectionByExample(Example example) throws MyDefinitionException;


    /**
     * 根据查询条件查询用户收藏公司结果
     *
     * @param example 查询条件
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    List<QubaomingActiveUserCollection> selectQubaomingActiveUserCollectionByExample(Example example) throws MyDefinitionException;



    /*通用*/

    /**
     * 创建用户收藏公司基础信息 并返回主键信息 要求参数必须是完整的数据
     *
     * @param qubaomingActiveUserCollection
     * @return 主键
     * @throws MyDefinitionException
     */
    Integer insertReturnPk(QubaomingActiveUserCollection qubaomingActiveUserCollection) throws MyDefinitionException;

    /**
     * 根据主键删除用户收藏公司数据
     *
     * @param pkId 主键
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    Integer deleteByPk(Integer pkId) throws MyDefinitionException;

    /**
     * 更新有值的用户收藏活动数据信息 要求必须有主键信息
     *
     * @param qubaomingActiveUserCollection
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    Integer updateSelectiveQubaomingActiveUserCollection(QubaomingActiveUserCollection qubaomingActiveUserCollection) throws MyDefinitionException;

    /**
     * 通过主键查找用户收藏活动数据 如果没有返回null
     *
     * @param pkId 主键
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    QubaomingActiveUserCollection selectByPkId(Integer pkId) throws MyDefinitionException;


}
