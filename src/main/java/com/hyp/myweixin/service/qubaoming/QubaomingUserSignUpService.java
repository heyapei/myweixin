package com.hyp.myweixin.service.qubaoming;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingActiveUserCollection;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingUserSignUp;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/19 20:33
 * @Description: TODO
 */
public interface QubaomingUserSignUpService {


    /**
     * 删除符合条件的用户报名数据
     *
     * @param example 查询条件
     * @return 影响行数
     * @throws MyDefinitionException
     */
    Integer deleteQubaomingUserSignUpByExample(Example example) throws MyDefinitionException;



    /**
     * 根据查询条件查询一条用户报名结果
     *
     * @param example 查询条件
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    QubaomingUserSignUp selectOneQubaomingUserSignUpByExample(Example example) throws MyDefinitionException;


    /**
     * 根据查询条件查询用户报名结果
     *
     * @param example 查询条件
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    List<QubaomingUserSignUp> selectQubaomingUserSignUpByExample(Example example) throws MyDefinitionException;



    /*通用*/

    /**
     * 创建用户报名基础信息 并返回主键信息 要求参数必须是完整的数据
     *
     * @param qubaomingUserSignUp
     * @return 主键
     * @throws MyDefinitionException
     */
    Integer insertReturnPk(QubaomingUserSignUp qubaomingUserSignUp) throws MyDefinitionException;

    /**
     * 根据主键删除用户报名数据
     *
     * @param pkId 主键
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    Integer deleteByPk(Integer pkId) throws MyDefinitionException;

    /**
     * 更新有值的用户报名数据信息 要求必须有主键信息
     *
     * @param qubaomingUserSignUp
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    Integer updateSelectiveQubaomingUserSignUp(QubaomingUserSignUp qubaomingUserSignUp) throws MyDefinitionException;

    /**
     * 通过主键查找用户报名数据 如果没有返回null
     *
     * @param pkId 主键
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    QubaomingUserSignUp selectByPkId(Integer pkId) throws MyDefinitionException;


}
