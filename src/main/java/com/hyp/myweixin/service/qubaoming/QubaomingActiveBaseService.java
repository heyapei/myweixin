package com.hyp.myweixin.service.qubaoming;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingActiveBase;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/15 14:53
 * @Description: TODO
 */
public interface QubaomingActiveBaseService {

    /**
     * 更新活动详情和图片
     *
     * @param qubaomingActiveBase 实体类
     * @return 影响行数
     * @throws MyDefinitionException
     */
    Integer updateActiveDetailAndImg(QubaomingActiveBase qubaomingActiveBase) throws MyDefinitionException;



    /**
     * 更新活动描述和图片
     *
     * @param qubaomingActiveBase 实体类
     * @return 影响行数
     * @throws MyDefinitionException
     */
    Integer updateActiveDescAndImg(QubaomingActiveBase qubaomingActiveBase) throws MyDefinitionException;


    /**
     * 更新活动标题和图片
     *
     * @param qubaomingActiveBase 实体类
     * @return 影响行数
     * @throws MyDefinitionException
     */
    Integer updateActiveNameAndImg(QubaomingActiveBase qubaomingActiveBase) throws MyDefinitionException;



    /**
     * 获取用户未完成的活动ID 如果没有则新建一个给用户
     *
     * @param activeUserId 用户ID
     * @return 活动ID
     * @throws MyDefinitionException
     */
    Integer getUserUnCompleteActiveId(Integer activeUserId) throws MyDefinitionException;


    /**
     * 检查用户创建的不同活动状态的活动列表 不分页
     *
     * @param activeStatus 活动状态
     * @param activeUserId 用户ID
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    List<QubaomingActiveBase> selectUserActiveByStatus(Integer activeUserId, Integer activeStatus) throws MyDefinitionException;



    /**
     * 根据查询条件查询结果
     *
     * @param example 查询条件
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    List<QubaomingActiveBase> selectUserActiveByExample(Example example) throws MyDefinitionException;



    /*通用*/

    /**
     * 创建活动基础信息 并返回主键信息 要求参数必须是完整的数据
     *
     * @param qubaomingActiveBase
     * @return 主键
     * @throws MyDefinitionException
     */
    Integer insertReturnPk(QubaomingActiveBase qubaomingActiveBase) throws MyDefinitionException;

    /**
     * 根据主键删除数据
     *
     * @param pkId 主键
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    Integer deleteByPk(Integer pkId) throws MyDefinitionException;

    /**
     * 更新有值的数据信息 要求必须有主键信息
     *
     * @param qubaomingActiveBase
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    Integer updateSelectiveQubaomingActiveBase(QubaomingActiveBase qubaomingActiveBase) throws MyDefinitionException;

    /**
     * 通过主键查找数据 如果没有返回null
     *
     * @param pkId 主键
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    QubaomingActiveBase selectByPkId(Integer pkId) throws MyDefinitionException;


}
