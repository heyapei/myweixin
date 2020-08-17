package com.hyp.myweixin.controller.qubaoming.company;

import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.model.WechatCompany;
import com.hyp.myweixin.pojo.qubaoming.query.company.CompanyDetailShowByUserIdQuery;
import com.hyp.myweixin.pojo.qubaoming.query.company.CompanyDetailShowQuery;
import com.hyp.myweixin.pojo.qubaoming.query.company.CompanyListShowQuery;
import com.hyp.myweixin.pojo.qubaoming.query.company.ShowUserCollectionPageQuery;
import com.hyp.myweixin.pojo.qubaoming.vo.company.CompanyShowVO;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.qubaoming.CompanyUserOptionService;
import com.hyp.myweixin.service.qubaoming.WechatCompanyShowService;
import com.hyp.myweixin.utils.MyRequestVailDateUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/16 16:22
 * @Description: TODO
 */
@RestController
@RequestMapping("qubaoming/company/show")
@Slf4j
public class CompanyShowController {

    @Autowired
    private MyRequestVailDateUtil myRequestVailDateUtil;
    @Autowired
    private SecretKeyPropertiesValue secretKeyPropertiesValue;
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private WechatCompanyShowService wechatCompanyShowService;

    @Autowired
    private CompanyUserOptionService companyUserOptionService;


    @ApiOperation(value = "获取用户收藏的公司列表 分页查询", tags = {"趣报名公司主体相关"})
    @PostMapping("collectionList/userId")
    public Result<PageInfo<WechatCompany>> showCollectionListByUserId(
            @ApiParam(name = "用户对公司的收藏列表", value = "showUserCollectionPageQuery", required = true)
            @Validated ShowUserCollectionPageQuery showUserCollectionPageQuery
            , BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ObjectError next = bindingResult.getAllErrors().iterator().next();
            return Result.buildResult(Result.Status.SERVER_ERROR, next.getDefaultMessage());
        }

        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            return Result.buildResult(Result.Status.UNAUTHORIZED, "密钥验证错误");
        }
        PageInfo<WechatCompany> pageInfo = null;
        try {
            pageInfo = companyUserOptionService.showCollectionListByUserId(showUserCollectionPageQuery);
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return Result.buildResult(Result.Status.OK, pageInfo);
    }



    @ApiOperation(value = "查看用户下的公司主体详细信息", tags = {"趣报名公司主体相关"})
    @PostMapping("getCompanyDetailShow/userId")
    public Result<CompanyShowVO> getCompanyListShow(
            @ApiParam(name = "查询用数据", value = "companyDetailShowByUserIdQuery", required = true)
            @Validated CompanyDetailShowByUserIdQuery companyDetailShowByUserIdQuery
            , BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ObjectError next = bindingResult.getAllErrors().iterator().next();
            return Result.buildResult(Result.Status.SERVER_ERROR, next.getDefaultMessage());
        }

        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            return Result.buildResult(Result.Status.UNAUTHORIZED, "密钥验证错误");
        }

        CompanyShowVO companyShowVOByCompanyId = null;
        try {
            companyShowVOByCompanyId = wechatCompanyShowService.getCompanyShowVOByUserId(companyDetailShowByUserIdQuery.getUserId());
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return Result.buildResult(Result.Status.OK, companyShowVOByCompanyId);
    }


    @ApiOperation(value = "查看指定的公司主体详细信息", tags = {"趣报名公司主体相关"})
    @PostMapping("getCompanyDetailShow")
    public Result<CompanyShowVO> getCompanyListShow(
            @ApiParam(name = "查询用数据", value = "companyDetailShowQuery", required = true)
            @Validated CompanyDetailShowQuery companyDetailShowQuery
            , BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ObjectError next = bindingResult.getAllErrors().iterator().next();
            return Result.buildResult(Result.Status.SERVER_ERROR, next.getDefaultMessage());
        }

        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            return Result.buildResult(Result.Status.UNAUTHORIZED, "密钥验证错误");
        }

        CompanyShowVO companyShowVOByCompanyId = null;
        try {
            companyShowVOByCompanyId = wechatCompanyShowService.getCompanyShowVOByCompanyId(companyDetailShowQuery.getWechatCompanyId());
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return Result.buildResult(Result.Status.OK, companyShowVOByCompanyId);
    }


    @ApiOperation(value = "获取个人名下公司主体列表分页查询", tags = {"趣报名公司主体相关"})
    @PostMapping("getCompanyListShow")
    public Result<PageInfo<Object>> getCompanyListShow(
            @ApiParam(name = "创建用数据", value = "companyCreateQuery", required = true)
            @Validated CompanyListShowQuery companyListShowQuery, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ObjectError next = bindingResult.getAllErrors().iterator().next();
            return Result.buildResult(Result.Status.SERVER_ERROR, next.getDefaultMessage());
        }

        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            return Result.buildResult(Result.Status.UNAUTHORIZED, "密钥验证错误");
        }

        PageInfo<Object> companyListShowByCompanyListShowQuery = null;
        try {
            companyListShowByCompanyListShowQuery = wechatCompanyShowService.getCompanyListShowByCompanyListShowQuery(companyListShowQuery);
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        if (companyListShowByCompanyListShowQuery == null) {
            return Result.buildResult(Result.Status.UNAUTHORIZED, "未能找到任何公司主体数据，请立刻创建您的公司数据");
        }
        return Result.buildResult(Result.Status.OK, companyListShowByCompanyListShowQuery);
    }


}
