package com.hyp.myweixin.service.qubaoming;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingActiveConfig;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/15 18:56
 * @Description: TODO
 */
public interface ActiveConfigService {


    /**
     * 查询当前活动的配置信息 如果没有返回null
     *
     * @param activeId 主键
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    QubaomingActiveConfig selectOneByActiveId(Integer activeId) throws MyDefinitionException;


    /**
     * 查询当前活动的配置信息 如果没有返回null
     *
     * @param activeId 主键
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    List<QubaomingActiveConfig> selectByActiveId(Integer activeId) throws MyDefinitionException;



    /*通用*/

    /**
     * 创建活动配置信息 并返回主键信息 要求参数必须是完整的数据
     *
     * @param qubaomingActiveConfig
     * @return 主键
     * @throws MyDefinitionException
     */
    Integer insertReturnPk(QubaomingActiveConfig qubaomingActiveConfig) throws MyDefinitionException;

    /**
     * 根据主键删除配置数据
     *
     * @param pkId 主键
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    Integer deleteByPk(Integer pkId) throws MyDefinitionException;

    /**
     * 更新有值的配置数据信息 要求必须有主键信息
     *
     * @param qubaomingActiveConfig
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    Integer updateSelectiveQubaomingActiveConfigBase(QubaomingActiveConfig qubaomingActiveConfig) throws MyDefinitionException;

    /**
     * 通过主键查找配置数据 如果没有返回null
     *
     * @param pkId 主键
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    QubaomingActiveConfig selectByPkId(Integer pkId) throws MyDefinitionException;
}
