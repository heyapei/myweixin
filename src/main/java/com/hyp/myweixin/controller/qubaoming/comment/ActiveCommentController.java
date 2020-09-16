package com.hyp.myweixin.controller.qubaoming.comment;

import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingActiveComment;
import com.hyp.myweixin.pojo.qubaoming.query.comment.ActiveCommentPageQuery;
import com.hyp.myweixin.pojo.qubaoming.query.comment.AddActiveCommentQuery;
import com.hyp.myweixin.pojo.qubaoming.vo.comment.ActiveCommentVO;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.qubaoming.QubaomingActiveCommentService;
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
 * @Date 2020/8/22 12:59
 * @Description: TODO
 */
@RestController
@RequestMapping("qubaoming/active/comment")
@Slf4j
public class ActiveCommentController {

    @Autowired
    private QubaomingActiveCommentService qubaomingActiveCommentService;
    @Autowired
    private MyRequestVailDateUtil myRequestVailDateUtil;
    @Autowired
    private SecretKeyPropertiesValue secretKeyPropertiesValue;
    @Autowired
    private HttpServletRequest httpServletRequest;


    @ApiOperation(value = "分页查询趣报名评论内容", tags = {"趣报名评论相关"})
    @PostMapping("getCommentByPage/activeId")
    public Result<PageInfo<ActiveCommentVO>> getCommentPageByActiveId(
            @ApiParam(name = "分页查询评论参数参数", value = "activeCommentPageQuery", required = true)
            @Validated ActiveCommentPageQuery activeCommentPageQuery,
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
        try {
            PageInfo<ActiveCommentVO> pageInfoActiveCommentByActiveCommentPageQuery = qubaomingActiveCommentService.
                    getPageInfoActiveCommentByActiveCommentPageQuery(activeCommentPageQuery);
            return Result.buildResult(Result.Status.OK, pageInfoActiveCommentByActiveCommentPageQuery);
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @ApiOperation(value = "参加趣报名评论内容", tags = {"趣报名评论相关"})
    @PostMapping("addComment/activeIdAndUserId")
    public Result<Object> addCommentByActiveIdAndUserId(
            @ApiParam(name = "添加评论参数参数", value = "addActiveCommentQuery", required = true)
            @Validated AddActiveCommentQuery addActiveCommentQuery,
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
        try {
            Integer integer = qubaomingActiveCommentService.addActiveCommentByUserIdActiveIdContent(addActiveCommentQuery.getUserId(), addActiveCommentQuery.getActiveId(), addActiveCommentQuery.getCommentContent());
            if (integer != null && integer > 0) {
                return Result.buildResult(Result.Status.OK);
            } else {
                return Result.buildResult(Result.Status.SERVER_ERROR, "未能成功保存当前评论");
            }
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


}
