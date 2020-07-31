package com.hyp.myweixin.controller.wechat.voteactive;

import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.vo.page.PageEditWorkDetailVO;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.VoteActiveDetailService;
import com.hyp.myweixin.utils.MyRequestVailDateUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/13 20:38
 * @Description: TODO
 */
@RestController
@RequestMapping("/page/active/")
@Slf4j
public class VoteActiveDetailController {

    @Autowired
    private MyRequestVailDateUtil myRequestValidateUtil;
    @Autowired
    private SecretKeyPropertiesValue secretKeyPropertiesValue;
    @Autowired
    private VoteActiveDetailService voteActiveDetailService;


    @ApiOperation("复制活动数据，要求用户登录且是当前活动管理员")
    @PostMapping("/detail/page/edit/copyActive/activeId")
    public Result copyActive(HttpServletRequest request,
                             @ApiParam(name = "复制用的活动ID", value = "activeId", required = true)
                             @RequestParam(required = true) Integer activeId,
                             @ApiParam(name = "当前用户ID", value = "userId", required = true)
                             @RequestParam(required = true) Integer userId) {
        boolean b = myRequestValidateUtil.validateSignMd5Date(request, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }
        Integer newActiveId = 0;
        try {
            newActiveId = voteActiveDetailService.copyActiveByActiveAndUserId(activeId, userId);
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.SERVER_ERROR, e.getMessage());
        }
        return Result.buildResult(Result.Status.OK, newActiveId);
    }


    @ApiOperation("用户管理活动首屏页面的活动相关数据，查询条件是活动ID")
    @PostMapping("/detail/page/edit/workdetail")
    public Result getpageById(HttpServletRequest request,
                              int workId) {

        boolean b = myRequestValidateUtil.validateSignMd5Date(request, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }

        PageEditWorkDetailVO pageEditWorkDetailVO = null;
        try {
            pageEditWorkDetailVO = voteActiveDetailService.getPageEditWorkDetailVOByWorkId(workId);
        } catch (MyDefinitionException e) {
            e.printStackTrace();
            return Result.buildResult(Result.Status.SERVER_ERROR, e.getMessage());
        }
        if (pageEditWorkDetailVO == null) {
            return Result.buildResult(Result.Status.NOT_FOUND, "没有通过当前ID找到相关的活动数据");
        }
        return Result.buildResult(Result.Status.OK, pageEditWorkDetailVO);
    }

}
