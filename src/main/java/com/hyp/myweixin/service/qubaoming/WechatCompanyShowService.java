package com.hyp.myweixin.service.qubaoming;

import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.query.company.CompanyListShowQuery;
import com.hyp.myweixin.pojo.qubaoming.vo.company.CompanyShowVO;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/16 16:32
 * @Description: TODO
 */
public interface WechatCompanyShowService {


    /**
     * 查看用户名下公司主体详细信息
     * @param userId
     * @return
     * @throws MyDefinitionException
     */
    CompanyShowVO getCompanyShowVOByUserId( Integer userId) throws MyDefinitionException;



    /**
     * 查看指定的公司主体详细信息
     * @param companyId
     * @return
     * @throws MyDefinitionException
     */
    CompanyShowVO getCompanyShowVOByCompanyId( Integer companyId) throws MyDefinitionException;


    /**
     * 通过CompanyListShowQuery查询条件分页查询个人所属的公司列表
     * 有排序
     * @param companyListShowQuery
     * @return
     * @throws MyDefinitionException
     */
    PageInfo<Object> getCompanyListShowByCompanyListShowQuery(CompanyListShowQuery companyListShowQuery) throws MyDefinitionException;

}
