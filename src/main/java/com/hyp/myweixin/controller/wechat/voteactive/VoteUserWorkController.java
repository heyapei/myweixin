package com.hyp.myweixin.controller.wechat.voteactive;

import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.modal.WeixinVoteWork;
import com.hyp.myweixin.pojo.query.voteuserwork.ActiveUserWorkQuery;
import com.hyp.myweixin.pojo.query.voteuserwork.SaveVoteUserQuery;
import com.hyp.myweixin.pojo.query.voteuserwork.UpdateUserWorkStatusQuery;
import com.hyp.myweixin.pojo.vo.page.activeeditor.UserUploadRightVO;
import com.hyp.myweixin.pojo.vo.page.activeeditor.UserWorkDetailVO;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.WeixinVoteUserWorkService;
import com.hyp.myweixin.service.WeixinVoteWorkService;
import com.hyp.myweixin.utils.MyRequestVailDateUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/14 18:56
 * @Description: TODO
 */
@RestController
@RequestMapping("/vote/userwork")
@Slf4j
public class VoteUserWorkController {

    @Autowired
    private MyRequestVailDateUtil myRequestValidateUtil;
    @Autowired
    private SecretKeyPropertiesValue secretKeyPropertiesValue;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private WeixinVoteWorkService weixinVoteWorkService;
    @Autowired
    private WeixinVoteUserWorkService weixinVoteUserWorkService;


    @ApiOperation("用户上传作品前判断需要什么信息以及是否允许上传")
    @PostMapping("/judgeUserUploadRight")
    public Result judgeUserUploadRight(@ApiParam(name = "活动ID", value = "activeId", required = true)
                                               Integer activeId,
                                       @ApiParam(name = "用户ID", value = "userId", required = true)
                                               Integer userId) {
        boolean b = myRequestValidateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }
        UserUploadRightVO userUploadRightVO = null;
        try {
            userUploadRightVO = weixinVoteUserWorkService.judgeUserUploadRight(userId, activeId);
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.BAD_REQUEST, e.getMessage());
        }

        return Result.buildResult(Result.Status.OK, userUploadRightVO);
    }


    @ApiOperation("管理员查看作品的详情信息")
    @PostMapping("/getUserWorkDetailByWorkId")
    public Result getUserWorkDetailByWorkId(@ApiParam(name = "用户ID", value = "userId", required = true)
                                                    Integer userId,
                                            @ApiParam(name = "作品ID", value = "workId", required = true)
                                                    Integer workId) {
        boolean b = myRequestValidateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }

        UserWorkDetailVO userWorkDetailByWorkId = null;
        try {
            userWorkDetailByWorkId = weixinVoteUserWorkService.getUserWorkDetailByWorkId(userId, workId);
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.BAD_REQUEST, e.getMessage());
        }
        return Result.buildResult(Result.Status.OK, userWorkDetailByWorkId);
    }


    @ApiOperation("管理员操作活动状态，更新作品的状态")
    @PostMapping("/updateUserWorkStatus")
    public Result updateUserWorkStatus(@ApiParam(name = "更新作品的状态", value = "", required = true)
                                       @Validated UpdateUserWorkStatusQuery updateUserWorkStatusQuery) {
        boolean b = myRequestValidateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }

        Integer rowAffect = null;
        try {
            rowAffect = weixinVoteWorkService.updateUserWorkStatus(updateUserWorkStatusQuery);
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.UNAUTHORIZED, "更新作品状态失败," + e.getMessage());
        }
        if (rowAffect == null) {
            return Result.buildResult(Result.Status.UNAUTHORIZED, "更新作品状态失败");
        }
        return Result.buildResult(Result.Status.OK, "更新作品状态成功");
    }


    @ApiOperation("管理员查看当前活动下的作品数据")
    @PostMapping("/getUserWorkListByType")
    public Result getUserWorkListByType(@ApiParam(name = "查询活动下的作品数据", value = "", required = true)
                                        @Validated ActiveUserWorkQuery activeUserWorkQuery) {
        boolean b = myRequestValidateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }

        PageInfo<WeixinVoteWork> userWorkListByTypePage = weixinVoteWorkService.getUserWorkListByTypePage(activeUserWorkQuery);

        return Result.buildResult(Result.Status.OK, userWorkListByTypePage);
    }


    @ApiOperation("保存用户在当前活动下提交的作品")
    @PostMapping("/saveVoteUserWork")
    public Result saveVoteUserWork(@ApiParam(name = "作品创建数据", value = "", required = true)
                                           SaveVoteUserQuery saveVoteUserQuery) {
        boolean b = myRequestValidateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }
        if (saveVoteUserQuery == null) {
            return Result.buildResult(Result.Status.SERVER_ERROR, "请求参数不能为空");
        }
        Result weixinVoteWorkReturnPK = null;
        try {
            weixinVoteWorkReturnPK = weixinVoteWorkService.createWeixinVoteWorkReturnPK(saveVoteUserQuery);
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.SERVER_ERROR, e.getMessage());
        }
        //log.info("创建结果：{}", weixinVoteWorkReturnPK.toString());
        if (!weixinVoteWorkReturnPK.getStatus().equals(Result.Status.OK.getCode())) {
            return weixinVoteWorkReturnPK;
        } else {
            return Result.buildResult(Result.Status.OK, weixinVoteWorkReturnPK.getData());
        }


    }


}
