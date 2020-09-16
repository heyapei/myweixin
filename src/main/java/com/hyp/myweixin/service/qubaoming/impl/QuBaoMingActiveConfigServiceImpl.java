package com.hyp.myweixin.service.qubaoming.impl;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.mapper.qubaoming.QubaomingActiveConfigMapper;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingActiveConfig;
import com.hyp.myweixin.service.qubaoming.QuBaoMingActiveConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/15 18:56
 * @Description: TODO
 */
@Service
@Slf4j
public class QuBaoMingActiveConfigServiceImpl implements QuBaoMingActiveConfigService {


    @Autowired
    private QubaomingActiveConfigMapper qubaomingActiveConfigMapper;


    /**
     * 查询当前活动的一条配置信息 如果没有返回null
     *
     * @param activeId 主键
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    @Override
    public QubaomingActiveConfig selectOneByActiveId(Integer activeId) throws MyDefinitionException {
        Example example = new Example(QubaomingActiveConfig.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("activeId", activeId);
        QubaomingActiveConfig qubaomingActiveConfig = null;
        try {
            qubaomingActiveConfig = qubaomingActiveConfigMapper.selectOneByExample(example);
        } catch (Exception e) {
            log.error("查询当前活动的配置信息操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("查询当前活动的配置信息操作过程错误");
        }

        return qubaomingActiveConfig;
    }

    /**
     * 查询当前活动的配置信息 如果没有返回null
     *
     * @param activeId 主键
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    @Override
    public List<QubaomingActiveConfig> selectByActiveId(Integer activeId) throws MyDefinitionException {


        Example example = new Example(QubaomingActiveConfig.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("activeId", activeId);
        List<QubaomingActiveConfig> qubaomingActiveConfigs = null;
        try {
            qubaomingActiveConfigs = qubaomingActiveConfigMapper.selectByExample(example);
        } catch (Exception e) {
            log.error("查询当前活动的配置信息操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("查询当前活动的配置信息操作过程错误");
        }

        return qubaomingActiveConfigs;
    }

    /**
     * 创建活动配置基础信息 并返回主键信息 要求参数必须是完整的数据
     *
     * @param qubaomingActiveConfig
     * @return 主键
     * @throws MyDefinitionException
     */
    @Override
    public Integer insertReturnPk(QubaomingActiveConfig qubaomingActiveConfig) throws MyDefinitionException {

        if (qubaomingActiveConfig == null) {
            throw new MyDefinitionException("参数不能为空");
        }

        Integer pkId = null;
        try {
            int i = qubaomingActiveConfigMapper.insertUseGeneratedKeys(qubaomingActiveConfig);
            if (i > 0) {
                pkId = qubaomingActiveConfig.getId();
            }
        } catch (Exception e) {
            log.error("创建活动配置基础信息操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("创建活动配置基础信息操作过程错误");
        }
        return pkId;
    }

    /**
     * 根据主键删除数据
     *
     * @param pkId 主键
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    @Override
    public Integer deleteByPk(Integer pkId) throws MyDefinitionException {

        if (pkId == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        int i = 0;
        try {
            i = qubaomingActiveConfigMapper.deleteByPrimaryKey(pkId);
        } catch (Exception e) {
            log.error("根据主键删除配置数据操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("删除配置数据操作过程错误");
        }
        return i;
    }

    /**
     * 更新有值的数据信息 要求必须有主键信息
     *
     * @param qubaomingActiveConfig
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    @Override
    public Integer updateSelectiveQubaomingActiveConfigBase(QubaomingActiveConfig qubaomingActiveConfig) throws MyDefinitionException {
        if (qubaomingActiveConfig == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        int i = 0;
        try {
            i = qubaomingActiveConfigMapper.updateByPrimaryKeySelective(qubaomingActiveConfig);
        } catch (Exception e) {
            log.error("更新有值的配置数据信息操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("更新配置数据信息操作过程错误");
        }
        return i;
    }

    /**
     * 通过主键查找数据 如果没有返回null
     *
     * @param pkId 主键
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    @Override
    public QubaomingActiveConfig selectByPkId(Integer pkId) throws MyDefinitionException {
        if (pkId == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        QubaomingActiveConfig qubaomingActiveConfig = null;
        try {
            qubaomingActiveConfig = qubaomingActiveConfigMapper.selectByPrimaryKey(pkId);
        } catch (Exception e) {
            log.error("通过主键查找配置数据操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("查找配置数据操作过程错误");
        }
        return qubaomingActiveConfig;
    }
}
