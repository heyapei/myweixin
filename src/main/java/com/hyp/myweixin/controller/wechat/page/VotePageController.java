package com.hyp.myweixin.controller.wechat.page;

import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.modal.WeixinVoteWork;
import com.hyp.myweixin.pojo.vo.page.VoteDetailByWorkIdVO;
import com.hyp.myweixin.pojo.vo.page.VoteDetailCompleteVO;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.WeixinVoteBaseService;
import com.hyp.myweixin.service.WeixinVoteWorkService;
import com.hyp.myweixin.utils.MyRequestVailDateUtil;
import io.swagger.annotations.ApiOperation;
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
        weixinVoteWork.setId(activeId);

        PageInfo voteWorkAllWorkByPage = weixinVoteWorkService.getVoteWorkAllWorkByPage(weixinVoteWork, pageInfo);
        return Result.buildResult(Result.Status.OK, voteWorkAllWorkByPage);
    }


    @ApiOperation(value = "根据workId查询活动下面的人气作品信息", tags = {"查询人气选手"})
    @PostMapping("/work/detail/hotwork")
    public Result getVoteWorkHotWork(HttpServletRequest request,
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
        weixinVoteWork.setId(activeId);

        PageInfo voteWorkAllWorkByPage = weixinVoteWorkService.getVoteWorkHotWorkByPage(weixinVoteWork, pageInfo);
        return Result.buildResult(Result.Status.OK, voteWorkAllWorkByPage);
    }


    @ApiOperation(value = "根据userWorkId查询用户作品信息")
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

}
