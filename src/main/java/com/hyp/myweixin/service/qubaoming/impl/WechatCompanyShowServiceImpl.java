package com.hyp.myweixin.service.qubaoming.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.model.WechatCompany;
import com.hyp.myweixin.pojo.qubaoming.query.company.CompanyListShowQuery;
import com.hyp.myweixin.pojo.qubaoming.vo.company.CompanyShowVO;
import com.hyp.myweixin.service.AdministratorsOptionService;
import com.hyp.myweixin.service.qubaoming.WechatCompanyService;
import com.hyp.myweixin.service.qubaoming.WechatCompanyShowService;
import com.hyp.myweixin.utils.MyEntityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/16 16:34
 * @Description: TODO
 */
@Slf4j
@Service
public class WechatCompanyShowServiceImpl implements WechatCompanyShowService {

    @Autowired
    private WechatCompanyService wechatCompanyService;

    private static final String SEMICOLON_SEPARATOR = ";";

    @Autowired
    private AdministratorsOptionService administratorsOptionService;

    /**
     * 查看用户名下公司主体详细信息
     *
     * @param userId
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public CompanyShowVO getCompanyShowVOByUserId(Integer userId) throws MyDefinitionException {
        if (userId == null) {
            throw new MyDefinitionException("参数不能为空");
        }

        WechatCompany wechatCompany = null;
        try {
            List<WechatCompany> wechatCompanies = wechatCompanyService.selectListByUserId(userId);
            if (wechatCompanies != null && wechatCompanies.size() > 0) {
                wechatCompany = wechatCompanies.get(0);
            } else {
                throw new MyDefinitionException("当前用户名下没有任何公司主体信息");
            }
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        if (wechatCompany == null) {
            throw new MyDefinitionException("没有找到指定的公司主体数据");
        }

        CompanyShowVO companyShowVO = null;
        try {
            companyShowVO = MyEntityUtil.entity2VM(wechatCompany, CompanyShowVO.class);
            if (StringUtils.isNotBlank(companyShowVO.getLogoImg())) {
                companyShowVO.setLogoImg(companyShowVO.getLogoImg().split(SEMICOLON_SEPARATOR)[0]);
            }
            if (StringUtils.isNotBlank(companyShowVO.getWeixinQrCode())) {
                companyShowVO.setWeixinQrCode(companyShowVO.getWeixinQrCode().split(SEMICOLON_SEPARATOR)[0]);
            }

            if (StringUtils.isNotBlank(wechatCompany.getCompanyEmail())) {
                companyShowVO.setCompanyEmails(wechatCompany.getCompanyEmail().split(SEMICOLON_SEPARATOR));
            }

        } catch (Exception e) {
            log.error("公司数据转换视图错误，错误理由：{}", e.toString());
            throw new MyDefinitionException("未能将公司主体数据转换为可查看的数据");
        }
        return companyShowVO;
    }

    /**
     * 查看用户名下公司主体详细信息
     *
     * @param companyId
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public CompanyShowVO getCompanyShowVOByCompanyId(Integer companyId) throws MyDefinitionException {

        if (companyId == null) {
            throw new MyDefinitionException("参数不能为空");
        }

        WechatCompany wechatCompany = null;
        try {
            wechatCompany = wechatCompanyService.selectByPkId(companyId);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        if (wechatCompany == null) {
            throw new MyDefinitionException("没有找到指定的公司主体数据");
        }

        CompanyShowVO companyShowVO = null;
        try {
            companyShowVO = MyEntityUtil.entity2VM(wechatCompany, CompanyShowVO.class);
            if (StringUtils.isNotBlank(companyShowVO.getLogoImg())) {
                companyShowVO.setLogoImg(companyShowVO.getLogoImg().split(SEMICOLON_SEPARATOR)[0]);
            }
            if (StringUtils.isNotBlank(companyShowVO.getWeixinQrCode())) {
                companyShowVO.setWeixinQrCode(companyShowVO.getWeixinQrCode().split(SEMICOLON_SEPARATOR)[0]);
            }

            if (StringUtils.isNotBlank(wechatCompany.getCompanyEmail())) {
                companyShowVO.setCompanyEmails(wechatCompany.getCompanyEmail().split(SEMICOLON_SEPARATOR));
            }


        } catch (Exception e) {
            log.error("公司数据转换视图错误，错误理由：{}", e.toString());
            throw new MyDefinitionException("未能将公司主体数据转换为可查看的数据");
        }
        return companyShowVO;
    }

    /**
     * 通过CompanyListShowQuery查询条件分页查询个人所属的公司列表
     * 有排序
     *
     * @param companyListShowQuery
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public PageInfo<Object> getCompanyListShowByCompanyListShowQuery(CompanyListShowQuery companyListShowQuery) throws MyDefinitionException {

        if (companyListShowQuery == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        Example example = new Example(WechatCompany.class);
        Example.Criteria criteria = example.createCriteria();
        if (!administratorsOptionService.isQuBaoMingSuperAdministrators(companyListShowQuery.getUserId())) {
            criteria.andEqualTo("userId", companyListShowQuery.getUserId());
        }
        example.orderBy("companyShowOrder").desc();
        example.orderBy("companyUsedNum").desc();
        example.orderBy("companyCollectionNum").desc();
        example.orderBy("companyShareNum").desc();
        example.orderBy("companyViewNum").desc();

        PageHelper.startPage(companyListShowQuery.getPageNum(), companyListShowQuery.getPageSize());
        PageInfo pageInfo = null;
        try {
            List<WechatCompany> wechatCompanies = wechatCompanyService.selectListByExample(example);
            List<WechatCompany> wechatCompaniesTemp = new ArrayList<>();
            for (WechatCompany companyShowVO : wechatCompanies) {

                if (StringUtils.isNotBlank(companyShowVO.getLogoImg())) {
                    companyShowVO.setLogoImg(companyShowVO.getLogoImg().split(SEMICOLON_SEPARATOR)[0]);
                }
                if (StringUtils.isNotBlank(companyShowVO.getWeixinQrCode())) {
                    companyShowVO.setWeixinQrCode(companyShowVO.getWeixinQrCode().split(SEMICOLON_SEPARATOR)[0]);
                }
                wechatCompaniesTemp.add(companyShowVO);
            }

            List<CompanyShowVO> companyShowVOS = MyEntityUtil.entity2VMList(wechatCompaniesTemp, CompanyShowVO.class);

            pageInfo = new PageInfo(companyShowVOS);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        return pageInfo;
    }
}
