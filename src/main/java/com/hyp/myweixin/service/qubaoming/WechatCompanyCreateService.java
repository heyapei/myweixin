package com.hyp.myweixin.service.qubaoming;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.query.company.CompanyCreateQuery;
import com.hyp.myweixin.pojo.qubaoming.query.company.CompanyUpdateQuery;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/15 17:02
 * @Description: TODO
 */
public interface WechatCompanyCreateService {


    /**
     * 更新公司信息
     *
     * @param companyUpdateQuery
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    Integer UpdateCompanyByCompanyUpdateQuery(CompanyUpdateQuery companyUpdateQuery) throws MyDefinitionException;



    /**
     * 创建公司信息
     *
     * @param companyCreateQuery
     * @return 主键
     * @throws MyDefinitionException
     */
    Integer createCompanyByCompanyCreateQuery(CompanyCreateQuery companyCreateQuery) throws MyDefinitionException;



}
