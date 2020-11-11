package com.hyp.myweixin.service.qubaoming.impl;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.model.WechatCompany;
import com.hyp.myweixin.pojo.qubaoming.query.company.CompanyCreateQuery;
import com.hyp.myweixin.pojo.qubaoming.query.company.CompanyUpdateEmailQuery;
import com.hyp.myweixin.pojo.qubaoming.query.company.CompanyUpdateQuery;
import com.hyp.myweixin.service.AdministratorsOptionService;
import com.hyp.myweixin.service.qubaoming.QubaomingWeixinUserService;
import com.hyp.myweixin.service.qubaoming.WechatCompanyCreateService;
import com.hyp.myweixin.service.qubaoming.WechatCompanyService;
import com.hyp.myweixin.utils.MyEntityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/15 17:03
 * @Description: TODO
 */
@Slf4j
@Service
public class WechatCompanyCreateServiceImpl implements WechatCompanyCreateService {

    @Autowired
    private WechatCompanyService wechatCompanyService;

    @Autowired
    private QubaomingWeixinUserService qubaomingWeixinUserService;
    @Autowired
    private AdministratorsOptionService administratorsOptionService;




    /**
     * 更新公司信息
     *
     * @param companyUpdateQuery
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    @Override
    public Integer UpdateCompanyByCompanyUpdateQuery(CompanyUpdateQuery companyUpdateQuery) throws MyDefinitionException {

        if (companyUpdateQuery == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        try {
            qubaomingWeixinUserService.validateUserRight(companyUpdateQuery.getUserId());
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        WechatCompany wechatCompany = null;
        try {
            wechatCompany = wechatCompanyService.selectByPkId(companyUpdateQuery.getWechatCompanyId());
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }

        /*判断是否为超级管理员 如果是就不做任何判断*/
        if (!administratorsOptionService.isQuBaoMingSuperAdministrators(companyUpdateQuery.getUserId())) {
            if (!wechatCompany.getUserId().equals(companyUpdateQuery.getUserId())) {
                throw new MyDefinitionException("您无权修改当前内容");
            }
        }
        WechatCompany wechatCompany1 = null;
        try {
            wechatCompany1 = (WechatCompany) MyEntityUtil.entitySetDefaultValue(MyEntityUtil.entity2VM(companyUpdateQuery, WechatCompany.class));
        } catch (MyDefinitionException e) {
            log.error("更新数据转换为能插入数据库操作出现了错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("数据转换操作出现了错误");
        }
        wechatCompany1.setId(companyUpdateQuery.getWechatCompanyId());
        wechatCompany1.setCompanyCollectionNum(wechatCompany.getCompanyCollectionNum());
        wechatCompany1.setCompanyUsedNum(wechatCompany.getCompanyUsedNum());
        wechatCompany1.setCompanyShareNum(wechatCompany.getCompanyShareNum());
        wechatCompany1.setCompanyViewNum(wechatCompany.getCompanyViewNum());
        try {
            return wechatCompanyService.updateSelectiveWechatCompany(wechatCompany1);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }

    }

    /**
     * 创建公司信息
     *
     * @param companyCreateQuery
     * @return 主键
     * @throws MyDefinitionException
     */
    @Override
    public Integer createCompanyByCompanyCreateQuery(CompanyCreateQuery companyCreateQuery) throws MyDefinitionException {

        if (companyCreateQuery == null) {
            throw new MyDefinitionException("参数不能为空");
        }

        try {
            qubaomingWeixinUserService.validateUserRight(companyCreateQuery.getUserId());
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }

        WechatCompany wechatCompany;
        try {
            wechatCompany = MyEntityUtil.entity2VM(companyCreateQuery, WechatCompany.class);
            wechatCompany = (WechatCompany) MyEntityUtil.entitySetDefaultValue(wechatCompany);
        } catch (Exception e) {
            log.error("创建过程中数据转换错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("创建过程中数据转换错误");
        }
        if (wechatCompany == null) {
            log.error("创建过程中数据转换失败，错误原因：{}", "没能正确进行转换，转换的结果是空");
            throw new MyDefinitionException("创建过程中数据转换失败");
        }


        /*判断是不是已有默认值*/
        /*限制每个用户只能有一个公司主体*/
        List<WechatCompany> wechatCompanies = wechatCompanyService.selectListByUserId(companyCreateQuery.getUserId());
        if (!wechatCompanies.isEmpty()) {
            // wechatCompany.setCompanyShowOrder(WechatCompany.CompanyShowOrderEnum.DEFAULT_COMPANY.getCode());
            throw new MyDefinitionException("每位用户只能有一个公司主体信息，当前用户已有一个公司主体存在");
        }


        try {
            return wechatCompanyService.insertReturnPk(wechatCompany);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }

    }


    /**
     * 更新公司邮箱地址 要求必须登录 且用户是改公司主体的所有人
     *
     * @param companyUpdateEmailQuery
     * @return 影响行数
     * @throws MyDefinitionException
     */
    @Override
    public Integer updateEmailByCompanyUpdateEmailQuery(CompanyUpdateEmailQuery companyUpdateEmailQuery) throws MyDefinitionException {

        if (companyUpdateEmailQuery == null) {
            throw new MyDefinitionException("参数不能为空");
        }

        try {
            qubaomingWeixinUserService.validateUserRight(companyUpdateEmailQuery.getUserId());
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }


        WechatCompany wechatCompany = null;
        try {
            wechatCompany = wechatCompanyService.selectByPkId(companyUpdateEmailQuery.getCompanyId());
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }

        if (wechatCompany == null) {
            throw new MyDefinitionException("没有找到指定的公司主体");
        }

        /*判断是否为超级管理员 如果是就不做任何判断*/
        if (!administratorsOptionService.isQuBaoMingSuperAdministrators(companyUpdateEmailQuery.getUserId())) {
            if (!wechatCompany.getUserId().equals(companyUpdateEmailQuery.getUserId())) {
                throw new MyDefinitionException("您无权修改当前内容");
            }
        }

        wechatCompany.setCompanyEmail(companyUpdateEmailQuery.getCompanyEmail());
        Integer integer = null;
        try {
            integer = wechatCompanyService.updateSelectiveWechatCompany(wechatCompany);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }

        return integer;
    }
}
