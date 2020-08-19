package com.hyp.myweixin.service.qubaoming.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.model.QuBaoMingCompanyUserCollection;
import com.hyp.myweixin.pojo.qubaoming.model.WechatCompany;
import com.hyp.myweixin.pojo.qubaoming.query.company.CompanyUserOptionQuery;
import com.hyp.myweixin.pojo.qubaoming.query.company.ShowUserCollectionPageQuery;
import com.hyp.myweixin.service.qubaoming.QuBaoMingCompanyUserCollectionService;
import com.hyp.myweixin.service.qubaoming.CompanyUserOptionService;
import com.hyp.myweixin.service.qubaoming.WechatCompanyService;
import com.hyp.myweixin.utils.MyEntityUtil;
import com.hyp.myweixin.utils.MySeparatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/17 22:06
 * @Description: TODO
 */
@Service
@Slf4j
public class CompanyUserOptionServiceImpl implements CompanyUserOptionService {
    @Autowired
    private QuBaoMingCompanyUserCollectionService companyUserCollectionService;
    @Autowired
    private WechatCompanyService wechatCompanyService;



    /**
     * 分页查询用户收藏的公司主体列表
     *
     * @param showUserCollectionPageQuery
     * @return 收藏的主键id
     * @throws MyDefinitionException
     */
    @Override
    public PageInfo showCollectionListByUserId(ShowUserCollectionPageQuery showUserCollectionPageQuery) throws MyDefinitionException {
        if (showUserCollectionPageQuery == null) {
            throw new MyDefinitionException("参数能为空");
        }

        Example example = new Example(QuBaoMingCompanyUserCollection.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", showUserCollectionPageQuery.getUserId());
        example.orderBy("createTime").desc();
        PageHelper.startPage(showUserCollectionPageQuery.getPageNum(), showUserCollectionPageQuery.getPageSize());
        List<QuBaoMingCompanyUserCollection> companyUserCollections = null;
        try {
            companyUserCollections = companyUserCollectionService.selectAllByExample(example);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        PageInfo pageInfoWeixinVoteBase = new PageInfo(companyUserCollections);
        List<WechatCompany> wechatCompanies = new ArrayList<>();
        for (QuBaoMingCompanyUserCollection companyUserCollection : companyUserCollections) {
            WechatCompany wechatCompany = null;
            try {
                wechatCompany = wechatCompanyService.selectByPkId(companyUserCollection.getCompanyId());
            } catch (MyDefinitionException e) {
                throw new MyDefinitionException(e.getMessage());
            }

            if (wechatCompany != null) {
                if (StringUtils.isNotBlank(wechatCompany.getLogoImg())) {
                    wechatCompany.setLogoImg(wechatCompany.getLogoImg().split(MySeparatorUtil.SEMICOLON_SEPARATOR)[0]);
                }
                if (StringUtils.isNotBlank(wechatCompany.getWeixinQrCode())) {
                    wechatCompany.setWeixinQrCode(wechatCompany.getWeixinQrCode().split(MySeparatorUtil.SEMICOLON_SEPARATOR)[0]);
                }
            }

            wechatCompanies.add(wechatCompany);
        }
        pageInfoWeixinVoteBase.setList(wechatCompanies);
        return pageInfoWeixinVoteBase;
    }

    /**
     * 添加用户对公司主体的收藏
     * 1. 首先检查公司是否存在
     * 2. 添加当前用户对公司的收藏（如果已经收藏了 就不再操作了）
     * 3. 更新公司数据表中的收藏数
     *
     * @param companyUserOptionQuery
     * @return 收藏的主键id
     * @throws MyDefinitionException
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer addUserCollectionCompany(CompanyUserOptionQuery companyUserOptionQuery) throws MyDefinitionException {

        if (companyUserOptionQuery == null) {
            throw new MyDefinitionException("参数能为空");
        }

        QuBaoMingCompanyUserCollection companyUserCollection = null;
        try {
            companyUserCollection = MyEntityUtil.entity2VM(companyUserOptionQuery, QuBaoMingCompanyUserCollection.class);
            companyUserCollection = (QuBaoMingCompanyUserCollection) MyEntityUtil.entitySetDefaultValue(companyUserCollection);
            companyUserCollection.setCreateTime(System.currentTimeMillis());
        } catch (MyDefinitionException e) {
            log.error("收藏数据转换和赋予默认值错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("收藏数据转换和赋予默认值错误");
        }

        Example example = new Example(QuBaoMingCompanyUserCollection.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", companyUserOptionQuery.getUserId());
        criteria.andEqualTo("companyId", companyUserOptionQuery.getCompanyId());
        QuBaoMingCompanyUserCollection companyUserCollection1 = companyUserCollectionService.selectOneByExample(example);
        if (companyUserCollection1 != null) {
            throw new MyDefinitionException("用户已收藏了该公司主体，无需再次收藏");
        }

        /*更新收藏数量*/
        Integer affectCol = null;
        try {
            affectCol = wechatCompanyService.increaseCollectionNum(companyUserOptionQuery.getCompanyId());
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        Integer pkId = null;
        if (affectCol != null && affectCol > 0) {
            /*插入收藏*/
            try {
                pkId = companyUserCollectionService.insertReturnPk(companyUserCollection);
            } catch (MyDefinitionException e) {
                throw new MyDefinitionException(e.getMessage());
            }
        }
        return pkId;
    }

    /**
     * 删除用户对公司主体的收藏
     * 1. 首先检查公司是否存在
     * 2. 添加当前用户对公司的收藏（如果已经收藏了 就不再操作了）
     * 3. 更新公司数据表中的收藏数
     *
     * @param companyUserOptionQuery
     * @return 收藏的主键id
     * @throws MyDefinitionException
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer delUserCollectionCompany(CompanyUserOptionQuery companyUserOptionQuery) throws MyDefinitionException {

        if (companyUserOptionQuery == null) {
            throw new MyDefinitionException("参数能为空");
        }
        QuBaoMingCompanyUserCollection companyUserCollection = null;
        try {
            companyUserCollection = MyEntityUtil.entity2VM(companyUserOptionQuery, QuBaoMingCompanyUserCollection.class);
            companyUserCollection = (QuBaoMingCompanyUserCollection) MyEntityUtil.entitySetDefaultValue(companyUserCollection);
        } catch (MyDefinitionException e) {
            log.error("收藏数据转换和赋予默认值错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("收藏数据转换和赋予默认值错误");
        }
        Example example = new Example(QuBaoMingCompanyUserCollection.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", companyUserOptionQuery.getUserId());
        criteria.andEqualTo("companyId", companyUserOptionQuery.getCompanyId());

        Integer affectRow = null;
        try {
            affectRow = companyUserCollectionService.deleteByExample(example);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        if (affectRow != null && affectRow > 0) {
            try {
                wechatCompanyService.decreaseCollectionNum(companyUserOptionQuery.getCompanyId());
            } catch (MyDefinitionException e) {
                throw new MyDefinitionException(e.getMessage());
            }
        } else {
            throw new MyDefinitionException("没能正确查询并删除对应的收藏数据，请重试");
        }

        return affectRow;
    }
}
