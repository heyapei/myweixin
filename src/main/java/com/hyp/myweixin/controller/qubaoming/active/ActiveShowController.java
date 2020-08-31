package com.hyp.myweixin.controller.qubaoming.active;

import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.query.active.ActiveDetailShowQuery;
import com.hyp.myweixin.pojo.qubaoming.query.active.ActiveShowByCompanyIdQuery;
import com.hyp.myweixin.pojo.qubaoming.query.active.ShowActiveByPageQuery;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.qubaoming.QubaomingActiveShowService;
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
 * @author 何亚培
 * @version V1.0
 * @Date 2020/8/18 8:37
 * @Description: TODO
 */

@RestController
@RequestMapping("qubaoming/active/show")
@Slf4j
public class ActiveShowController {


    @Autowired
    private MyRequestVailDateUtil myRequestVailDateUtil;
    @Autowired
    private SecretKeyPropertiesValue secretKeyPropertiesValue;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private QubaomingActiveShowService QubaomingActiveShowService;


    @ApiOperation(value = "根据公司ID查询公司下的活动信息 分页查询", tags = {"趣报名活动查看相关"})
    @PostMapping("getActiveList/companyId/page")
    public Result<Object> getActiveListByCompanyIdAndPage(
            @ApiParam(name = "分页信息", value = "activeShowByCompanyIdQuery", required = true)
            @Validated ActiveShowByCompanyIdQuery activeShowByCompanyIdQuery, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ObjectError next = bindingResult.getAllErrors().iterator().next();
            return Result.buildResult(Result.Status.SERVER_ERROR, next.getDefaultMessage());
        }

        /*鉴权*/
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            return Result.buildResult(Result.Status.UNAUTHORIZED, "密钥验证错误");
        }

        try {
            return Result.buildResult(Result.Status.OK,
                    QubaomingActiveShowService.getActiveListByCompanyId(
                            activeShowByCompanyIdQuery));
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @ApiOperation(value = "查看活动详情", tags = {"趣报名活动查看相关"})
    @PostMapping("activeDetail/activeId")
    public Result<Object> getActiveDetailById(
            @ApiParam(name = "查看活动详情的参数", value = "activeDetailShowQuery", required = true)
            @Validated ActiveDetailShowQuery activeDetailShowQuery, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ObjectError next = bindingResult.getAllErrors().iterator().next();
            return Result.buildResult(Result.Status.SERVER_ERROR, next.getDefaultMessage());
        }

        /*鉴权*/
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            return Result.buildResult(Result.Status.UNAUTHORIZED, "密钥验证错误");
        }

        try {
            return Result.buildResult(Result.Status.OK, QubaomingActiveShowService.getActiveShowDetailByActiveId(activeDetailShowQuery.getUserId(), activeDetailShowQuery.getActiveId()));
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }


    @ApiOperation(value = "查看热门活动 分页查询", tags = {"趣报名活动查看相关"})
    @PostMapping("hotActive/page")
    public Result<Object> showHotActiveByPage(
            @ApiParam(name = "分页信息", value = "showHotActiveByPageQuery", required = true)
            @Validated ShowActiveByPageQuery showHotActiveByPageQuery, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ObjectError next = bindingResult.getAllErrors().iterator().next();
            return Result.buildResult(Result.Status.SERVER_ERROR, next.getDefaultMessage());
        }

        /*鉴权*/
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            return Result.buildResult(Result.Status.UNAUTHORIZED, "密钥验证错误");
        }

        try {
            return Result.buildResult(Result.Status.OK, QubaomingActiveShowService.getHotActiveByShowActiveByPageQuery(showHotActiveByPageQuery));
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @ApiOperation(value = "查看所有活动 分页查询", tags = {"趣报名活动查看相关"})
    @PostMapping("allActive/page")
    public Result<Object> showAllActiveByPage(
            @ApiParam(name = "分页信息（本来只是想用于热门活动查询来的 现在发现都可以用。。哈哈哈）", value = "showHotActiveByPageQuery", required = true)
            @Validated ShowActiveByPageQuery showHotActiveByPageQuery, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ObjectError next = bindingResult.getAllErrors().iterator().next();
            return Result.buildResult(Result.Status.SERVER_ERROR, next.getDefaultMessage());
        }

        /*鉴权*/
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            return Result.buildResult(Result.Status.UNAUTHORIZED, "密钥验证错误");
        }

        try {
            return Result.buildResult(Result.Status.OK, QubaomingActiveShowService.getAllActiveByShowActiveByPageQuery(showHotActiveByPageQuery));
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


}
