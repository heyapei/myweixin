package com.hyp.myweixin.controller.qubaoming.active;

import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.query.active.ActiveShareImgQuery;
import com.hyp.myweixin.pojo.vo.result.Result;
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
 * @Date 2020/8/31 13:44
 * @Description: TODO
 */
@RestController
@RequestMapping("qubaoming/active/share")
@Slf4j
public class ActiveShareController {


    @Autowired
    private MyRequestVailDateUtil myRequestVailDateUtil;
    @Autowired
    private SecretKeyPropertiesValue secretKeyPropertiesValue;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private com.hyp.myweixin.service.qubaoming.QubaomingActiveShowService QubaomingActiveShowService;

    @ApiOperation(value = "获取分享图", tags = {"趣报名分享相关"})
    @PostMapping("getActiveShareImg/activeId")
    public Result<Object> getActiveShareImg(
            @ApiParam(name = "获取指定活动的分享图", value = "activeShareImgQuery", required = true)
            @Validated ActiveShareImgQuery activeShareImgQuery, BindingResult bindingResult) {
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
            return Result.buildResult(Result.Status.OK, "成功获取分享图", QubaomingActiveShowService.getActiveShareImgByActiveId(activeShareImgQuery.getActiveId()));
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @ApiOperation(value = "增加分享量", tags = {"趣报名分享相关"})
    @PostMapping("addActiveShareNum/activeId")
    public Result<Object> addActiveShareNum(
            @ApiParam(name = "增加分享量参数", value = "activeShareImgQuery", required = true)
            @Validated ActiveShareImgQuery activeShareImgQuery, BindingResult bindingResult) {
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
            return Result.buildResult(Result.Status.OK, "增加分享成功", QubaomingActiveShowService.addActiveShareNumByActiveId(activeShareImgQuery.getActiveId()));
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


}
