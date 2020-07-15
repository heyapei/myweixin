package com.hyp.myweixin.controller.wechat.page;

import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.modal.WeixinVoteUserWork;
import com.hyp.myweixin.pojo.modal.WeixinVoteWork;
import com.hyp.myweixin.pojo.modal.WeixinVoteWorkComment;
import com.hyp.myweixin.pojo.vo.page.VoteDetailByWorkIdVO;
import com.hyp.myweixin.pojo.vo.page.VoteDetailCompleteVO;
import com.hyp.myweixin.pojo.vo.page.VoteDetailTwoByWorkIdVO;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.WeixinVoteBaseService;
import com.hyp.myweixin.service.WeixinVoteUserWorkService;
import com.hyp.myweixin.service.WeixinVoteWorkCommentService;
import com.hyp.myweixin.service.WeixinVoteWorkService;
import com.hyp.myweixin.utils.MyRequestVailDateUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/9 21:10
 * @Description: TODO
 */
@RestController
@RequestMapping("/page/vote")
@Slf4j
public class VotePageController {

    @Autowired
    private MyRequestVailDateUtil myRequestValidateUtil;
    @Autowired
    private SecretKeyPropertiesValue secretKeyPropertiesValue;
    @Autowired
    private WeixinVoteBaseService weixinVoteBaseService;
    @Autowired
    private WeixinVoteWorkService weixinVoteWorkService;
    @Autowired
    private WeixinVoteUserWorkService weixinVoteUserWorkService;

    @Autowired
    private WeixinVoteWorkCommentService weixinVoteWorkCommentService;


    @ApiOperation("通过活动信息页面点入活动详情页，查询条件是活动ID")
    @PostMapping("/work/detail/votedetailtwo")
    public Result getVoteDetailTwoByWorkIdVOById(HttpServletRequest request,
                                                 int workId) {

        boolean b = myRequestValidateUtil.validateSignMd5Date(request, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }

        VoteDetailTwoByWorkIdVO voteDetailTwoByWorkIdVOById = null;
        try {
            voteDetailTwoByWorkIdVOById = weixinVoteBaseService.getVoteDetailTwoByWorkIdVOById(workId);
        } catch (MyDefinitionException e) {
            e.printStackTrace();
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        if (voteDetailTwoByWorkIdVOById == null) {
            return Result.buildResult(Result.Status.NOT_FOUND);
        }
        return Result.buildResult(Result.Status.OK, voteDetailTwoByWorkIdVOById);
    }


    @ApiOperation("根据workId查询活动的详情")
    @PostMapping("/work/detail")
    public Result getVoteWorkByPage(HttpServletRequest request,
                                    int workId) {

        boolean b = myRequestValidateUtil.validateSignMd5Date(request, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }

        VoteDetailByWorkIdVO voteWorkByWorkId = weixinVoteBaseService.getVoteWorkByWorkId(workId);
        if (voteWorkByWorkId == null) {
            return Result.buildResult(Result.Status.NOT_FOUND);
        }
        return Result.buildResult(Result.Status.OK, voteWorkByWorkId);
    }


    @ApiOperation(value = "根据workId查询活动下面的所有作品信息", tags = {"查询全部选手"})
    @PostMapping("/work/detail/allwork")
    public Result getVoteWorkAllWork(HttpServletRequest request,
                                     int activeId,
                                     @RequestParam(defaultValue = "1") int pageNo,
                                     @RequestParam(defaultValue = "5") int pageSize) {

        boolean b = myRequestValidateUtil.validateSignMd5Date(request, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }

        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageNum(pageNo);
        pageInfo.setPageSize(pageSize);
        WeixinVoteWork weixinVoteWork = new WeixinVoteWork();
        weixinVoteWork.setActiveVoteBaseId(activeId);
        weixinVoteWork.setVoteWorkShowOrder(0);
        weixinVoteWork.setVoteWorkOr(-1);
        weixinVoteWork.setVoteWorkCountNum(null);

        PageInfo voteWorkAllWorkByPage = weixinVoteWorkService.getVoteWorkAllWorkByPage(weixinVoteWork, pageInfo);
        return Result.buildResult(Result.Status.OK, voteWorkAllWorkByPage);
    }


    @ApiOperation(value = "根据workId查询活动下面的人气作品信息", tags = {"查询人气选手"})
    @PostMapping("/work/detail/hotwork")
    public Result getVoteWorkHotWork(HttpServletRequest request,
                                     int activeId,
                                     @RequestParam(defaultValue = "1") int pageNoNum,
                                     @RequestParam(defaultValue = "5") int pageSize) {

        boolean b = myRequestValidateUtil.validateSignMd5Date(request, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageNum(pageNoNum);
        pageInfo.setPageSize(pageSize);

        WeixinVoteWork weixinVoteWork = new WeixinVoteWork();
        weixinVoteWork.setId(activeId);

        PageInfo voteWorkAllWorkByPage = weixinVoteWorkService.getVoteWorkHotWorkByPage(weixinVoteWork, pageInfo);
        return Result.buildResult(Result.Status.OK, voteWorkAllWorkByPage);
    }


    @ApiOperation(value = "根据userWorkId查询用户作品详细信息")
    @PostMapping("/work/detail/userwork")
    public Result getVoteWorkHotWork(HttpServletRequest request,
                                     Integer userWorkId) {
        boolean b = myRequestValidateUtil.validateSignMd5Date(request, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }
        VoteDetailCompleteVO weixinVoteWorkByUserWorkId = weixinVoteWorkService.getWeixinVoteWorkByUserWorkId(userWorkId);
        return Result.buildResult(Result.Status.OK, weixinVoteWorkByUserWorkId);
    }


    @ApiOperation(value = "获取作品的点赞人的信息")
    @PostMapping("/work/detail/userworkvoteuser")
    public Result getUserWorkVoteUser(HttpServletRequest request,
                                      Integer userWorkId,
                                      @RequestParam(defaultValue = "1") int pageNo,
                                      @RequestParam(defaultValue = "5") int pageSize) {
        boolean b = myRequestValidateUtil.validateSignMd5Date(request, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }


        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageNum(pageNo);
        pageInfo.setPageSize(pageSize);
        WeixinVoteUserWork weixinVoteUserWork = new WeixinVoteUserWork();
        weixinVoteUserWork.setWorkId(userWorkId);
        PageInfo weixinVoteUserWorkByPage = weixinVoteUserWorkService.getWeixinVoteUserWorkByPage(weixinVoteUserWork, pageInfo);
        PageInfo weixinVoteUserWorkSimpleVOByPageInfo = weixinVoteUserWorkService.getWeixinVoteUserWorkSimpleVOByPageInfo(weixinVoteUserWorkByPage);
        return Result.buildResult(Result.Status.OK, weixinVoteUserWorkSimpleVOByPageInfo);
        //return null;
    }


    @ApiOperation(value = "对具体作品添加评论")
    @PostMapping("/work/detail/addcomment")
    public Result addComment(HttpServletRequest request,
                             Integer voteUserId,
                             Integer voteWorkId,
                             String workComment) {


        if (StringUtils.isBlank(workComment)) {
            return Result.buildResult(Result.Status.PARAM_NOT_COMPLETE, "评论不可以为空");
        }
        if (voteUserId == null) {
            return Result.buildResult(Result.Status.PARAM_NOT_COMPLETE, "用户ID不可以为空");
        }
        if (voteWorkId == null) {
            return Result.buildResult(Result.Status.PARAM_NOT_COMPLETE, "作品ID不可以为空");
        }


        boolean b = myRequestValidateUtil.validateSignMd5Date(request, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }


        WeixinVoteWorkComment weixinVoteWorkComment = new WeixinVoteWorkComment();
        weixinVoteWorkComment.setVoteUserId(voteUserId);
        weixinVoteWorkComment.setWorkComment(workComment);
        weixinVoteWorkComment.setVoteWorkId(voteWorkId);
        int i = weixinVoteWorkCommentService.addWeixinVoteWorkComment(weixinVoteWorkComment);
        if (i <= 0) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, "保存用户评论错误");
        } else {
            return Result.buildResult(Result.Status.OK, "保存用户评论成功", i);
        }
    }


    @ApiOperation(value = "查询具体作品的评论")
    @PostMapping("/work/detail/userworkcomment")
    public Result getUserWorkComment(HttpServletRequest request,
                                     Integer voteWorkId,
                                     @RequestParam(defaultValue = "1") int pageNo,
                                     @RequestParam(defaultValue = "5") int pageSize) {


        if (voteWorkId == null) {
            return Result.buildResult(Result.Status.PARAM_NOT_COMPLETE, "作品ID不可以为空");
        }

        boolean b = myRequestValidateUtil.validateSignMd5Date(request, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }

        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageNum(pageNo);
        pageInfo.setPageSize(pageSize);
        WeixinVoteWorkComment weixinVoteWorkComment = new WeixinVoteWorkComment();
        weixinVoteWorkComment.setVoteWorkId(voteWorkId);
        PageInfo weixinVoteWorkCommentVOByPageInfo = weixinVoteWorkCommentService.getWeixinVoteWorkCommentVOByPageInfo(weixinVoteWorkComment, pageInfo);
        if (weixinVoteWorkCommentVOByPageInfo != null) {
            return Result.buildResult(Result.Status.OK, weixinVoteWorkCommentVOByPageInfo);
        }
        return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR);
    }


    @ApiOperation(value = "查询当前作品的差距信息")
    @PostMapping("/work/detail/userworkdiff")
    public Result getUserWorkDiff(HttpServletRequest request,
                                  @RequestParam Integer userWorkId) {
        boolean b = myRequestValidateUtil.validateSignMd5Date(request, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }
        return Result.buildResult(Result.Status.OK, weixinVoteWorkService.getUserWorkDiff(userWorkId));
        //return null;
    }


    @ApiOperation(value = "获取当前活动下的作品的排行榜")
    @PostMapping("/work/detail/activeworkrank")
    public Result getActiveWorkRank(HttpServletRequest request,
                                    Integer voteWorkId,
                                    @RequestParam(defaultValue = "1") int pageNo,
                                    @RequestParam(defaultValue = "5") int pageSize) {
        boolean b = myRequestValidateUtil.validateSignMd5Date(request, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }
        if (voteWorkId == null) {
            return Result.buildResult(Result.Status.PARAM_NOT_COMPLETE);
        }
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageNum(pageNo);
        pageInfo.setPageSize(pageSize);
        return Result.buildResult(Result.Status.OK, weixinVoteBaseService.getActiveWorkRank(voteWorkId, pageInfo));
    }


}
