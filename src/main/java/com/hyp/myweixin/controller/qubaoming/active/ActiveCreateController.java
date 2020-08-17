package com.hyp.myweixin.controller.qubaoming.active;

import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.query.active.ActiveCreateFirstQuery;
import com.hyp.myweixin.pojo.qubaoming.query.active.ActiveCreateSecondQuery;
import com.hyp.myweixin.pojo.qubaoming.query.active.ActiveCreateThirdQuery;
import com.hyp.myweixin.pojo.qubaoming.vo.active.ValidateUnCompleteByActiveUserIdVO;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.qubaoming.QubaomingActiveCreateService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/15 15:30
 * @Description: TODO
 */
@RestController
@RequestMapping("qubaoming/active/create")
@Slf4j
public class ActiveCreateController {


    @Autowired
    private MyRequestVailDateUtil myRequestVailDateUtil;
    @Autowired
    private SecretKeyPropertiesValue secretKeyPropertiesValue;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private QubaomingActiveCreateService qubaomingActiveCreateService;


    @ApiOperation(value = "保存第三页中活动公司相关内容", tags = {"趣报名活动创建"})
    @PostMapping("thirdPage/activeId")
    public Result<Object> createActiveThird(
            @Validated ActiveCreateThirdQuery activeCreateSecondQuery, BindingResult bindingResult) {
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
            Integer activeSecond = qubaomingActiveCreateService.createActiveThird(activeCreateSecondQuery);

            if (activeSecond != null && activeSecond > 0) {
                return Result.buildResult(Result.Status.OK, activeSecond);
            } else {
                return Result.buildResult(Result.Status.SERVER_ERROR, "未能成功配置公司主体信息，活动上线失败");
            }
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }


    @ApiOperation(value = "保存第二页中活动配置相关内容", tags = {"趣报名活动创建"})
    @PostMapping("secondPage/activeId")
    public Result<Object> createActiveSecond(
            @Validated ActiveCreateSecondQuery activeCreateSecondQuery, BindingResult bindingResult) {
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
            Integer activeSecond = qubaomingActiveCreateService.createActiveSecond(activeCreateSecondQuery);

            if (activeSecond != null && activeSecond > 0) {
                return Result.buildResult(Result.Status.OK, activeSecond);
            } else {
                return Result.buildResult(Result.Status.SERVER_ERROR, "未能成功创建配置信息");
            }
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }


    @ApiOperation(value = "第一页信息和第二页详情描述和图片的创建", tags = {"趣报名活动创建"})
    @PostMapping("firstPage/activeId")
    public Result<ValidateUnCompleteByActiveUserIdVO> createActiveFirst(
            @Validated ActiveCreateFirstQuery activeCreateFirstQuery, BindingResult bindingResult) {
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
            Integer activeFirst = qubaomingActiveCreateService.createActiveFirst(activeCreateFirstQuery);
            if (activeFirst != null && activeFirst > 0) {
                return Result.buildResult(Result.Status.OK);
            } else {
                return Result.buildResult(Result.Status.SERVER_ERROR, "未能更新成功当前内容" + activeCreateFirstQuery.getType());
            }
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @ApiOperation(value = "获取是否存在有未创建完成的活动", tags = {"趣报名活动创建"})
    @PostMapping("validate/unComplete/activeId")
    public Result<ValidateUnCompleteByActiveUserIdVO> validateUnCompleteByActiveUserId(
            @ApiParam(name = "用户ID", value = "activeUserId", required = true)
            @RequestParam(required = true)
            @Validated @NotNull Integer activeUserId) {

        /*鉴权*/
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            return Result.buildResult(Result.Status.UNAUTHORIZED, "密钥验证错误");
        }
        if (activeUserId == null) {
            return Result.buildResult(Result.Status.BAD_REQUEST, "用户必须先登录并授权");
        }
        ValidateUnCompleteByActiveUserIdVO validateUnCompleteByActiveUserIdVO = null;
        try {
            validateUnCompleteByActiveUserIdVO = qubaomingActiveCreateService.validateUnCompleteByActiveUserId(activeUserId);
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return Result.buildResult(Result.Status.OK, validateUnCompleteByActiveUserIdVO);
    }


}
