package com.hyp.myweixin.controller.qubaoming.pcenter;

import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.query.pcenter.UserEnrollQuery;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.qubaoming.PersonCenterService;
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
 * @Date 2020/8/31 14:05
 * @Description: TODO
 */
@RestController
@RequestMapping("qubaoming/person/center")
@Slf4j
public class PersonCenterController {

    @Autowired
    private MyRequestVailDateUtil myRequestVailDateUtil;
    @Autowired
    private SecretKeyPropertiesValue secretKeyPropertiesValue;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private PersonCenterService personCenterService;

    @ApiOperation(value = "用户已收藏列表", tags = {"趣报名用户中心相关"})
    @PostMapping("userEnroll/userId")
    public Result<Object> getUserEnrollPageInfo(
            @ApiParam(name = "收藏参数", value = "userEnrollQuery", required = true)
            @Validated UserEnrollQuery userEnrollQuery, BindingResult bindingResult) {
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
            return Result.buildResult(Result.Status.OK, personCenterService.getUserEnrollPageInfo(userEnrollQuery));
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @ApiOperation(value = "用户已报名列表", tags = {"趣报名用户中心相关"})
    @PostMapping("userSignUp/userId")
    public Result<Object> getActiveShareImg(
            @ApiParam(name = "报名列表参数", value = "activeShareImgQuery", required = true)
            @Validated UserEnrollQuery userEnrollQuery, BindingResult bindingResult) {
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
            return Result.buildResult(Result.Status.OK, personCenterService.getUserSignUpPageInfo(userEnrollQuery));
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


}
