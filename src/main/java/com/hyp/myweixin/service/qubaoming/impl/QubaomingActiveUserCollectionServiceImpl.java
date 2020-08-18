package com.hyp.myweixin.service.qubaoming.impl;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.mapper.qubaoming.QubaomingActiveUserCollectionMapper;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingActiveUserCollection;
import com.hyp.myweixin.service.qubaoming.QubaomingActiveUserCollectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/18 19:28
 * @Description: TODO
 */
@Slf4j
@Service
public class QubaomingActiveUserCollectionServiceImpl implements QubaomingActiveUserCollectionService {

    @Autowired
    private QubaomingActiveUserCollectionMapper qubaomingActiveUserCollectionMapper;

    /**
     * 删除符合条件的用户收藏公司数据
     *
     * @param example 查询条件
     * @return 影响行数
     * @throws MyDefinitionException
     */
    @Override
    public Integer deleteQubaomingActiveUserCollectionByExample(Example example) throws MyDefinitionException {

        if (example == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        int i = 0;
        try {
            i = qubaomingActiveUserCollectionMapper.deleteByExample(example);
        } catch (Exception e) {
            log.error("删除符合条件的用户收藏公司数据操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("删除符合条件的数据操作过程错误");
        }
        return i;
    }

    /**
     * 根据查询条件查询一条用户收藏公司数据结果
     *
     * @param example 查询条件
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    @Override
    public QubaomingActiveUserCollection selectOneQubaomingActiveUserCollectionByExample(Example example) throws MyDefinitionException {

        if (example == null) {
            throw new MyDefinitionException("参数不能为空");
        }

        QubaomingActiveUserCollection qubaomingActiveUserCollection = null;
        try {
            qubaomingActiveUserCollection = qubaomingActiveUserCollectionMapper.selectOneByExample(example);
        } catch (Exception e) {
            log.error("根据查询条件查询一条用户收藏公司数据结果操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("查询操作过程错误");
        }

        return qubaomingActiveUserCollection;
    }

    /**
     * 根据查询条件查询用户收藏公司结果
     *
     * @param example 查询条件
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    @Override
    public List<QubaomingActiveUserCollection> selectQubaomingActiveUserCollectionByExample(Example example) throws MyDefinitionException {
        if (example == null) {
            throw new MyDefinitionException("参数不能为空");
        }

        List<QubaomingActiveUserCollection> qubaomingActiveUserCollectionList = null;
        try {
            qubaomingActiveUserCollectionList = qubaomingActiveUserCollectionMapper.selectByExample(example);
        } catch (Exception e) {
            log.error("根据查询条件查询全部用户收藏公司结果操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("查询操作过程错误");
        }

        return qubaomingActiveUserCollectionList;
    }

    /**
     * 创建用户收藏活动基础信息 并返回主键信息 要求参数必须是完整的数据
     *
     * @param qubaomingActiveUserCollection
     * @return 主键
     * @throws MyDefinitionException
     */
    @Override
    public Integer insertReturnPk(QubaomingActiveUserCollection qubaomingActiveUserCollection) throws MyDefinitionException {
        if (qubaomingActiveUserCollection == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        Integer pkId = null;
        try {
            pkId = null;
            int i = qubaomingActiveUserCollectionMapper.insertUseGeneratedKeys(qubaomingActiveUserCollection);
            if (i > 0) {
                pkId = qubaomingActiveUserCollection.getId();
            }
        } catch (Exception e) {
            log.error("创建用户收藏活动基础信息并返回主键信息操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("创建信息操作过程错误");
        }
        return pkId;
    }

    /**
     * 根据主键删除用户收藏活动数据
     *
     * @param pkId 主键
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    @Override
    public Integer deleteByPk(Integer pkId) throws MyDefinitionException {
        if (pkId == null) {
            throw new MyDefinitionException("参数能为空");
        }

        int i = 0;
        try {
            i = qubaomingActiveUserCollectionMapper.deleteByPrimaryKey(pkId);
        } catch (Exception e) {
            log.error("根据主键删除用户收藏活动数据操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("删除数据操作过程错误");
        }
        return i;
    }

    /**
     * 更新有值的用户收藏活动数据信息 要求必须有主键信息
     *
     * @param qubaomingActiveUserCollection
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    @Override
    public Integer updateSelectiveQubaomingActiveUserCollection(QubaomingActiveUserCollection qubaomingActiveUserCollection) throws MyDefinitionException {
        if (qubaomingActiveUserCollection == null) {
            throw new MyDefinitionException("参数能为空");
        }
        int i = 0;
        try {
            i = qubaomingActiveUserCollectionMapper.updateByPrimaryKeySelective(qubaomingActiveUserCollection);
        } catch (Exception e) {
            log.error("更新有值的用户收藏活动数据信息操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("更新数据信息操作过程错误");
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
    public QubaomingActiveUserCollection selectByPkId(Integer pkId) throws MyDefinitionException {
        if (pkId == null) {
            throw new MyDefinitionException("参数能为空");
        }
        QubaomingActiveUserCollection qubaomingActiveUserCollection = null;
        try {
            qubaomingActiveUserCollection = qubaomingActiveUserCollectionMapper.selectByPrimaryKey(pkId);
        } catch (Exception e) {
            log.error("通过主键查找用户收藏活动数据操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("查找数据操作过程错误");
        }
        return qubaomingActiveUserCollection;
    }
}
