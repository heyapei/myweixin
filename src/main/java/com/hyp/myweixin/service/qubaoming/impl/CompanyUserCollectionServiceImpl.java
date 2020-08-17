package com.hyp.myweixin.service.qubaoming.impl;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.mapper.qubaoming.CompanyUserCollectionMapper;
import com.hyp.myweixin.pojo.qubaoming.model.CompanyUserCollection;
import com.hyp.myweixin.service.qubaoming.CompanyUserCollectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/17 22:01
 * @Description: TODO
 */
@Service
@Slf4j
public class CompanyUserCollectionServiceImpl implements CompanyUserCollectionService {

    @Autowired
    private CompanyUserCollectionMapper companyUserCollectionMapper;

    /**
     * 通过查询条件删除用户对公司主体收藏数据
     *
     * @param example 查询条件
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    @Override
    public Integer deleteByExample(Example example) throws MyDefinitionException {

        if (example == null) {
            throw new MyDefinitionException("参数不能为空");
        }

        int i = 0;
        try {
            i = companyUserCollectionMapper.deleteByExample(example);
        } catch (Exception e) {
            log.error("通过查询条件删除用户对公司主体收藏数据操作过程错误，错误原因：{}",e.toString());
            throw new MyDefinitionException("删除用户对公司主体收藏数据操作过程错误");
        }
        return i;
    }

    /**
     * 创建用户对公司主体收藏 并返回主键信息 要求参数必须是完整的数据
     *
     * @param companyUserCollection
     * @return 主键
     * @throws MyDefinitionException
     */
    @Override
    public Integer insertReturnPk(CompanyUserCollection companyUserCollection) throws MyDefinitionException {
        if (companyUserCollection == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        Integer pkId = null;
        try {
            pkId = null;
            int i = companyUserCollectionMapper.insertUseGeneratedKeys(companyUserCollection);
            if (i > 0) {
                pkId = companyUserCollection.getId();
            }
        } catch (Exception e) {
            log.error("创建用户对公司主体收藏并返回主键信息操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("创建活动基础信息操作过程错误");
        }
        return pkId;
    }

    /**
     * 根据主键删除用户对公司主体收藏数据
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
            i = companyUserCollectionMapper.deleteByPrimaryKey(pkId);
        } catch (Exception e) {
            log.error("根据主键删除用户对公司主体收藏数据操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("删除数据操作过程错误");
        }
        return i;
    }

    /**
     * 更新有值的用户对公司主体收藏数据信息 要求必须有主键信息
     *
     * @param companyUserCollection
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    @Override
    public Integer updateSelectiveCompanyUserCollection(CompanyUserCollection companyUserCollection) throws MyDefinitionException {
        if (companyUserCollection == null) {
            throw new MyDefinitionException("参数能为空");
        }
        int i = 0;
        try {
            i = companyUserCollectionMapper.updateByPrimaryKeySelective(companyUserCollection);
        } catch (Exception e) {
            log.error("更新有值的用户对公司主体收藏数据信息操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("更新数据信息操作过程错误");
        }
        return i;
    }

    /**
     * 通过主键查找用户对公司主体收藏数据 如果没有返回null
     *
     * @param pkId 主键
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    @Override
    public CompanyUserCollection selectByPkId(Integer pkId) throws MyDefinitionException {
        if (pkId == null) {
            throw new MyDefinitionException("参数能为空");
        }
        CompanyUserCollection companyUserCollection = null;
        try {
            companyUserCollection = companyUserCollectionMapper.selectByPrimaryKey(pkId);
        } catch (Exception e) {
            log.error("通过主键查找用户对公司主体收藏数据操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("查找数据操作过程错误");
        }
        return companyUserCollection;
    }

    /**
     * 通过查询条件查找一条用户对公司主体收藏数据 如果没有返回null
     *
     * @param example 查询条件
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    @Override
    public CompanyUserCollection selectOneByExample(Example example) throws MyDefinitionException {

        if (example == null) {
            throw new MyDefinitionException("参数能为空");
        }

        CompanyUserCollection companyUserCollection = null;
        try {
            companyUserCollection = companyUserCollectionMapper.selectOneByExample(example);
        } catch (Exception e) {
            log.error("通过查询条件查找一条用户对公司主体收藏数据操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("通过查询条件查找一条用户对公司主体收藏数据操作过程错误");
        }


        return companyUserCollection;
    }

    /**
     * 通过查询条件查找所有用户对公司主体收藏数据 如果没有返回null
     *
     * @param example 查询条件
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    @Override
    public List<CompanyUserCollection> selectAllByExample(Example example) throws MyDefinitionException {
        if (example == null) {
            throw new MyDefinitionException("参数能为空");
        }

        List<CompanyUserCollection> companyUserCollections = null;
        try {
            companyUserCollections = companyUserCollectionMapper.selectByExample(example);
        } catch (Exception e) {
            log.error("通过查询条件查找所有用户对公司主体收藏数据操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("通过查询条件查找所有用户对公司主体收藏数据操作过程错误");
        }


        return companyUserCollections;
    }
}
