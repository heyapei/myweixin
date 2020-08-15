package com.hyp.myweixin.service.qubaoming.impl;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.modal.WeixinVoteUser;
import com.hyp.myweixin.pojo.qubaoming.model.WechatCompany;
import com.hyp.myweixin.pojo.qubaoming.query.company.CompanyCreateQuery;
import com.hyp.myweixin.service.WeixinVoteUserService;
import com.hyp.myweixin.service.qubaoming.QubaomingWeixinUserService;
import com.hyp.myweixin.service.qubaoming.WechatCompanyCreateService;
import com.hyp.myweixin.service.qubaoming.WechatCompanyService;
import com.hyp.myweixin.utils.MyEntityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        try {
            return wechatCompanyService.insertReturnPk(wechatCompany);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }

    }
}
