package com.hyp.myweixin.controller.qubaoming.active.manager;

import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.query.active.manager.ActiveManagerByPageQuery;
import com.hyp.myweixin.pojo.qubaoming.query.active.manager.ActiveManagerIndexQuery;
import com.hyp.myweixin.pojo.qubaoming.vo.active.manager.ActiveManagerIndexVO;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.qubaoming.ActiveManagerService;
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
 * @Date 2020/8/22 9:29
 * @Description: TODO
 */
@RestController
@RequestMapping("qubaoming/active/manager")
@Slf4j
public class ActiveManagerController {


    @Autowired
    private MyRequestVailDateUtil myRequestVailDateUtil;
    @Autowired
    private SecretKeyPropertiesValue secretKeyPropertiesValue;
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private ActiveManagerService activeManagerService;


    @ApiOperation(value = "获取活动管理首页数据", tags = {"趣报名活动管理"})
    @PostMapping("getActiveManagerIndex/activeId")
    public Result<Object> getActiveManagerIndexByActiveId(
            @ApiParam(name = "查询参数", value = "activeManagerIndexQuery", required = true)
            @Validated ActiveManagerIndexQuery activeManagerIndexQuery,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ObjectError next = bindingResult.getAllErrors().iterator().next();
            return Result.buildResult(Result.Status.SERVER_ERROR, next.getDefaultMessage());
        }
        /*鉴权*/
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            return Result.buildResult(Result.Status.UNAUTHORIZED, "密钥验证错误");
        }

        ActiveManagerIndexVO activeManagerIndexVOByActiveId = null;
        try {
            activeManagerIndexVOByActiveId = activeManagerService.getActiveManagerIndexVOByActiveId(activeManagerIndexQuery);
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.SERVER_ERROR, e.getMessage());
        }

        return Result.buildResult(Result.Status.OK, activeManagerIndexVOByActiveId);
    }


    @ApiOperation(value = "分页获取参加人的信息", tags = {"趣报名活动管理"})
    @PostMapping("getSignUpByPage/activeId")
    public Result<PageInfo<Object>> getSignUpByPageAndActiveId(
            @ApiParam(name = "查询参数", value = "activeManagerByPageQuery", required = true)
            @Validated ActiveManagerByPageQuery activeManagerByPageQuery,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ObjectError next = bindingResult.getAllErrors().iterator().next();
            return Result.buildResult(Result.Status.SERVER_ERROR, next.getDefaultMessage());
        }
        /*鉴权*/
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            return Result.buildResult(Result.Status.UNAUTHORIZED, "密钥验证错误");
        }
        PageInfo<Object> allSignUpByPageQuery = null;
        try {
            allSignUpByPageQuery = activeManagerService.getAllSignUpByPageQuery(activeManagerByPageQuery);
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.SERVER_ERROR, e.getMessage());

        }
        return Result.buildResult(Result.Status.OK, allSignUpByPageQuery);
    }

}
