package com.hyp.myweixin.service.qubaoming.impl;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.mapper.qubaoming.WechatCompanyMapper;
import com.hyp.myweixin.pojo.qubaoming.model.WechatCompany;
import com.hyp.myweixin.service.qubaoming.WechatCompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/15 15:41
 * @Description: TODO
 */
@Slf4j
@Service
public class WechatCompanyServiceImpl implements WechatCompanyService {

    @Autowired
    private WechatCompanyMapper wechatCompanyMapper;


    /**
     * 减少公司主体收藏数
     *
     * @param companyId 公司主体ID
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    @Override
    public Integer decreaseCollectionNum(Integer companyId) throws MyDefinitionException {
        if (companyId == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        WechatCompany wechatCompany = null;
        try {
            wechatCompany = selectByPkId(companyId);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        if (wechatCompany == null) {
            throw new MyDefinitionException("没有找到指定的公司主体");
        }
        wechatCompany.setCompanyCollectionNum(wechatCompany.getCompanyCollectionNum() - 1);
        Integer integer = null;
        try {
            integer = updateSelectiveWechatCompany(wechatCompany);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }

        return integer;
    }

    /**
     * 新增公司主体收藏数
     *
     * @param companyId 公司主体ID
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    @Override
    public Integer increaseCollectionNum(Integer companyId) throws MyDefinitionException {
        if (companyId == null) {
            throw new MyDefinitionException("参数不能为空");
        }

        WechatCompany wechatCompany = null;
        try {
            wechatCompany = selectByPkId(companyId);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        if (wechatCompany == null) {
            throw new MyDefinitionException("没有找到指定的公司主体");
        }
        wechatCompany.setCompanyCollectionNum(wechatCompany.getCompanyCollectionNum() + 1);
        Integer integer = null;
        try {
            integer = updateSelectiveWechatCompany(wechatCompany);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }

        return integer;
    }


    /**
     * 通过用户ID查询用户下的主办方信息
     *
     * @param userId 用户ID
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    @Override
    public WechatCompany selectOneByUserId(Integer userId) throws MyDefinitionException {
        if (userId == null) {
            throw new MyDefinitionException("参数不能为空");
        }

        Example example = new Example(WechatCompany.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        example.orderBy("companyShowOrder").desc();
        example.orderBy("companyUsedNum").desc();
        example.orderBy("companyCollectionNum").desc();
        example.orderBy("companyShareNum").desc();
        example.orderBy("companyViewNum").desc();
        WechatCompany wechatCompanies = null;
        try {
            wechatCompanies = wechatCompanyMapper.selectOneByExample(example);
        } catch (Exception e) {
            log.error("通过用户ID查询条件进行查询主办方信息操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("查询用户下唯一主办方信息操作过程错误");
        }
        return wechatCompanies;
    }

    /**
     * 通过Example查询条件进行查询
     *
     * @param example 查询条件
     * @return 列表数据
     * @throws MyDefinitionException
     */
    @Override
    public List<WechatCompany> selectListByExample(Example example) throws MyDefinitionException {

        if (example == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        List<WechatCompany> wechatCompanies = null;
        try {
            wechatCompanies = wechatCompanyMapper.selectByExample(example);
        } catch (Exception e) {
            log.error("通过Example查询条件进行查询主办方信息操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("通过查询条件查询主办方信息操作过程错误");
        }
        return wechatCompanies;
    }

    /**
     * 通过用户ID查询用户下的主办方信息
     *
     * @param userId 用户ID
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    @Override
    public List<WechatCompany> selectListByUserId(Integer userId) throws MyDefinitionException {
        if (userId == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        Example example = new Example(WechatCompany.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        example.orderBy("companyShowOrder").desc();
        example.orderBy("companyUsedNum").desc();
        example.orderBy("companyCollectionNum").desc();
        example.orderBy("companyShareNum").desc();
        example.orderBy("companyViewNum").desc();
        List<WechatCompany> wechatCompanies = null;
        try {
            wechatCompanies = wechatCompanyMapper.selectByExample(example);
        } catch (Exception e) {
            log.error("通过用户ID查询用户下的主办方信息操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("查询用户下的主办方信息操作过程错误");
        }
        return wechatCompanies;
    }

    /**
     * 创建主办方基础信息 并返回主键信息 要求参数必须是完整的数据
     *
     * @param wechatCompany
     * @return 主键
     * @throws MyDefinitionException
     */
    @Override
    public Integer insertReturnPk(WechatCompany wechatCompany) throws MyDefinitionException {
        if (wechatCompany == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        Integer pkId = null;
        try {
            pkId = null;
            int i = wechatCompanyMapper.insertUseGeneratedKeys(wechatCompany);
            if (i > 0) {
                pkId = wechatCompany.getId();
            }
        } catch (Exception e) {
            log.error("创建主办方信息并返回主键信息操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("创建主办方信息操作过程错误");
        }
        return pkId;
    }

    /**
     * 根据主键删除主办方数据
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
            i = wechatCompanyMapper.deleteByPrimaryKey(pkId);
        } catch (Exception e) {
            log.error("根据主键删除主办方数据操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("删除主办方数据操作过程错误");
        }
        return i;
    }

    /**
     * 更新有值的主办方数据信息 要求必须有主键信息
     *
     * @param wechatCompany
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    @Override
    public Integer updateSelectiveWechatCompany(WechatCompany wechatCompany) throws MyDefinitionException {
        if (wechatCompany == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        int i = 0;
        try {
            i = wechatCompanyMapper.updateByPrimaryKeySelective(wechatCompany);
        } catch (Exception e) {
            log.error("更新有值的主办方数据信息操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("更新主办方数据信息操作过程错误");
        }
        return i;
    }

    /**
     * 通过主键查找主办方数据 如果没有返回null
     *
     * @param pkId 主键
     * @return 完整的实体类
     * @throws MyDefinitionException
     */
    @Override
    public WechatCompany selectByPkId(Integer pkId) throws MyDefinitionException {
        if (pkId == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        WechatCompany wechatCompany = null;
        try {
            wechatCompany = wechatCompanyMapper.selectByPrimaryKey(pkId);
        } catch (Exception e) {
            log.error("通过主键查找主办方数据操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("查找主办方数据操作过程错误");
        }
        return wechatCompany;
    }
}
