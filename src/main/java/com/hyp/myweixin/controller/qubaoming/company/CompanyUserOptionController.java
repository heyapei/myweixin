package com.hyp.myweixin.controller.qubaoming.company;

import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.model.WechatCompany;
import com.hyp.myweixin.pojo.qubaoming.query.company.CompanyUserOptionQuery;
import com.hyp.myweixin.pojo.qubaoming.query.company.ShowUserCollectionPageQuery;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.qubaoming.CompanyUserOptionService;
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
 * @Date 2020/8/17 21:46
 * @Description: TODO
 */
@RestController
@RequestMapping("qubaoming/company/collection")
@Slf4j
public class CompanyUserOptionController {

    @Autowired
    private MyRequestVailDateUtil myRequestVailDateUtil;
    @Autowired
    private SecretKeyPropertiesValue secretKeyPropertiesValue;
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private CompanyUserOptionService companyUserOptionService;





    @ApiOperation(value = "删除用户收藏公司数据", tags = {"趣报名公司主体相关"})
    @PostMapping("delCollection")
    public Result<Object> delCollectionCompany(
            @ApiParam(name = "用户对公司主体操作参数，用于收藏/取消收藏", value = "companyUserOptionQuery", required = true)
            @Validated CompanyUserOptionQuery companyUserOptionQuery
            , BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ObjectError next = bindingResult.getAllErrors().iterator().next();
            return Result.buildResult(Result.Status.SERVER_ERROR, next.getDefaultMessage());
        }

        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            return Result.buildResult(Result.Status.UNAUTHORIZED, "密钥验证错误");
        }
        Integer pkId = null;
        try {
            pkId = companyUserOptionService.delUserCollectionCompany(companyUserOptionQuery);
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return Result.buildResult(Result.Status.OK, pkId);
    }


    @ApiOperation(value = "添加用户收藏公司数据", tags = {"趣报名公司主体相关"})
    @PostMapping("addCollection")
    public Result<Object> addCollectionCompany(
            @ApiParam(name = "用户对公司主体操作参数，用于收藏/取消收藏", value = "companyUserOptionQuery", required = true)
            @Validated CompanyUserOptionQuery companyUserOptionQuery
            , BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ObjectError next = bindingResult.getAllErrors().iterator().next();
            return Result.buildResult(Result.Status.SERVER_ERROR, next.getDefaultMessage());
        }

        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            return Result.buildResult(Result.Status.UNAUTHORIZED, "密钥验证错误");
        }


        Integer pkId = null;
        try {
            pkId = companyUserOptionService.addUserCollectionCompany(companyUserOptionQuery);
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return Result.buildResult(Result.Status.OK, pkId);
    }


}
