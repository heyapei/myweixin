package com.hyp.myweixin.controller.qubaoming.enroll;

import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.query.enroll.JudgeActiveSignUpQuery;
import com.hyp.myweixin.pojo.qubaoming.query.enroll.UserEnrollActiveQuery;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.qubaoming.UserEnrollActiveService;
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
 * @Date 2020/8/18 18:58
 * @Description: TODO
 */
@RestController
@RequestMapping("qubaoming/active/enroll")
@Slf4j
public class UserEnrollActiveController {

    @Autowired
    private MyRequestVailDateUtil myRequestVailDateUtil;
    @Autowired
    private SecretKeyPropertiesValue secretKeyPropertiesValue;
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private UserEnrollActiveService userEnrollActiveService;


    @ApiOperation(value = "通过用户ID和活动ID判断是否允许报名", tags = {"趣报名活动相关"})
    @PostMapping("judgeActiveSignUp/activeIdAndUserId")
    public Result<Object> judgeActiveSignUp(
            @ApiParam(name = "判断活动是否可以报名的参数", value = "judgeActiveSignUpQuery", required = true)
            @Validated JudgeActiveSignUpQuery judgeActiveSignUpQuery
            , BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ObjectError next = bindingResult.getAllErrors().iterator().next();
            return Result.buildResult(Result.Status.SERVER_ERROR, next.getDefaultMessage());
        }
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            return Result.buildResult(Result.Status.UNAUTHORIZED, "密钥验证错误");
        }
        try {
            userEnrollActiveService.ValidateUserSignActive(judgeActiveSignUpQuery.getUserId(), judgeActiveSignUpQuery.getActiveId());
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return Result.buildResult(Result.Status.OK, "允许报名");
    }


    @ApiOperation(value = "用户收藏活动", tags = {"趣报名活动相关"})
    @PostMapping("addEnroll/activeId")
    public Result<Object> enrollActiveUserId(
            @ApiParam(name = "收藏活动用参数", value = "userEnrollActiveQuery", required = true)
            @Validated UserEnrollActiveQuery userEnrollActiveQuery
            , BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ObjectError next = bindingResult.getAllErrors().iterator().next();
            return Result.buildResult(Result.Status.SERVER_ERROR, next.getDefaultMessage());
        }

        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            return Result.buildResult(Result.Status.UNAUTHORIZED, "密钥验证错误");
        }

        Integer affectRow = null;
        try {
            affectRow = userEnrollActiveService.UserEnrollActive(userEnrollActiveQuery.getUserId(), userEnrollActiveQuery.getActiveId());
            if (affectRow == null || affectRow <= 0) {
                return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, "未能成功收藏活动信息");
            }
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }


        return Result.buildResult(Result.Status.OK, affectRow);
    }


    @ApiOperation(value = "用户删除收藏活动", tags = {"趣报名活动相关"})
    @PostMapping("unEnroll/activeId")
    public Result<Object> unEnrollActiveUserId(
            @ApiParam(name = "删除收藏活动用参数", value = "userEnrollActiveQuery", required = true)
            @Validated UserEnrollActiveQuery userEnrollActiveQuery
            , BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            ObjectError next = bindingResult.getAllErrors().iterator().next();
            return Result.buildResult(Result.Status.SERVER_ERROR, next.getDefaultMessage());
        }

        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            return Result.buildResult(Result.Status.UNAUTHORIZED, "密钥验证错误");
        }

        Integer affectRow = null;
        try {
            affectRow = userEnrollActiveService.UserUnEnrollActive(userEnrollActiveQuery.getUserId(), userEnrollActiveQuery.getActiveId());
            if (affectRow == null || affectRow <= 0) {
                return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, "未能成功取消收藏的活动信息");
            }
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return Result.buildResult(Result.Status.OK, affectRow);
    }


}
