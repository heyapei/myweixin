package com.hyp.myweixin.controller.qubaoming.company;

import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.query.company.CompanyCreateQuery;
import com.hyp.myweixin.pojo.qubaoming.query.company.CompanyUpdateQuery;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.qubaoming.WechatCompanyCreateService;
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
 * @Date 2020/8/15 16:46
 * @Description: TODO
 */
@RestController
@RequestMapping("qubaoming/company/create")
@Slf4j
public class CompanyCreateController {

    @Autowired
    private MyRequestVailDateUtil myRequestVailDateUtil;
    @Autowired
    private SecretKeyPropertiesValue secretKeyPropertiesValue;
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private WechatCompanyCreateService wechatCompanyCreateService;


    @ApiOperation(value = "更新公司主体信息", tags = {"趣报名公司主体相关"})
    @PostMapping("updateCompany/companyId")
    public Result<Object> updateCompanyByCompanyId(
            @ApiParam(name = "创建用数据", value = "companyCreateQuery", required = true)
            @Validated CompanyUpdateQuery companyUpdateQuery, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ObjectError next = bindingResult.getAllErrors().iterator().next();
            return Result.buildResult(Result.Status.SERVER_ERROR, next.getDefaultMessage());
        }
        /*鉴权*/
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            return Result.buildResult(Result.Status.UNAUTHORIZED, "密钥验证错误");
        }
        Integer companyByCompanyCreateQuery = null;
        try {
            companyByCompanyCreateQuery = wechatCompanyCreateService.UpdateCompanyByCompanyUpdateQuery(companyUpdateQuery);
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return Result.buildResult(Result.Status.OK, companyByCompanyCreateQuery);
    }


    @ApiOperation(value = "创建公司主体信息", tags = {"趣报名公司主体相关"})
    @PostMapping("addCompany")
    public Result<Object> addCompany(
            @ApiParam(name = "创建用数据", value = "companyCreateQuery", required = true)
            @Validated CompanyCreateQuery companyCreateQuery, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ObjectError next = bindingResult.getAllErrors().iterator().next();
            return Result.buildResult(Result.Status.SERVER_ERROR, next.getDefaultMessage());
        }
        /*鉴权*/
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            return Result.buildResult(Result.Status.UNAUTHORIZED, "密钥验证错误");
        }
        Integer companyByCompanyCreateQuery = null;
        try {
            companyByCompanyCreateQuery = wechatCompanyCreateService.createCompanyByCompanyCreateQuery(companyCreateQuery);
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return Result.buildResult(Result.Status.OK, companyByCompanyCreateQuery);
    }


}
