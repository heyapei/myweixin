package com.hyp.myweixin.controller.wechat.userwork;

import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.query.userwork.UpdateUserWorkQuery;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.WeixinVoteWorkService;
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

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/27 20:22
 * @Description: TODO
 */
@RestController
@RequestMapping("/user/work")
@Slf4j
public class UserWorkController {

    @Autowired
    private MyRequestVailDateUtil myRequestVailDateUtil;
    @Autowired
    private SecretKeyPropertiesValue secretKeyPropertiesValue;
    @Autowired
    private WeixinVoteWorkService weixinVoteWorkService;
    @Autowired
    private HttpServletRequest httpServletRequest;


    /**
     * 删除作品操作要求是管理员权限
     *
     * @param userWorkId 作品ID
     * @param userId     用户ID
     * @return
     */
    @ApiOperation("通过活动ID获取活动的分享图")
    @PostMapping("delete/userWorkId")
    public Result deleteUserWorkById(
            @ApiParam(name = "作品ID", value = "userWorkId", required = true)
            @RequestParam(required = true) Integer userWorkId,
            @ApiParam(name = "用户ID", value = "userId", required = true)
            @RequestParam(required = true) Integer userId) {
        /*鉴权*/
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            return Result.buildResult(Result.Status.UNAUTHORIZED, "密钥验证错误");
        }

        try {
            Integer affectRow = weixinVoteWorkService.deleteUserWorkByWorkIdAdmin(userWorkId, userId);
            if (affectRow > 0) {
                return Result.buildResult(Result.Status.OK);
            } else {
                return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, "删除作品失败");
            }
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }


    /**
     * 根据作品ID更新作品信息
     * 1. 要求是管理员权限
     *
     * @return
     */
    @ApiOperation("根据作品ID更新作品信息")
    @PostMapping("update/userWorkId")
    public Result updateUserWork(
            @ApiParam(name = "更新用参数", value = "updateUserWorkQuery", required = true)
            @Validated UpdateUserWorkQuery updateUserWorkQuery,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            //取一条错误信息
            ObjectError next = bindingResult.getAllErrors().iterator().next();
            //后边可以自己返回错误信息也可以自定义
            return Result.buildResult(Result.Status.BAD_REQUEST, next.getDefaultMessage());
        }
        /*鉴权*/
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            return Result.buildResult(Result.Status.UNAUTHORIZED, "密钥验证错误");
        }
        try {
            Integer affectRow = weixinVoteWorkService.updateWeixinVoteWorkAdmin(updateUserWorkQuery);
            if (affectRow > 0) {
                return Result.buildResult(Result.Status.OK);
            } else {
                return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, "更新作品失败");
            }
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


}
