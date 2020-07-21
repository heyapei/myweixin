package com.hyp.myweixin.controller.wechat.voteactive;

import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.vo.page.activeeditor.ActiveEditFirstVO;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.VoteActiveService;
import com.hyp.myweixin.service.WeixinVoteBaseEditService;
import com.hyp.myweixin.utils.MyRequestVailDateUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/21 19:42
 * @Description: TODO
 */
@RestController
@RequestMapping("/vote/active/edit")
@Slf4j
public class VoteActiveEditController {

    @Autowired
    private MyRequestVailDateUtil myRequestVailDateUtil;
    @Autowired
    private SecretKeyPropertiesValue secretKeyPropertiesValue;
    @Autowired
    private VoteActiveService voteActiveService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private WeixinVoteBaseEditService weixinVoteBaseEditService;


    /**
     * 回显活动编辑第一页的内容
     * 要求:
     * 1. 判断是否为管理员
     *
     * @param activeId 活动ID
     * @param userId   用户ID
     * @return
     */
    @ApiOperation("回显活动编辑第一页的内容")
    @PostMapping("showFirstPage")
    public Result createActiveSexRegion(
            Integer activeId, Integer userId
    ) {
        /*鉴权*/
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }
        ActiveEditFirstVO activeEditFirstVOByActiveId = null;
        try {
            activeEditFirstVOByActiveId = weixinVoteBaseEditService.getActiveEditFirstVOByActiveId(activeId, userId);
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.SERVER_ERROR, e.getMessage());
        }
        return Result.buildResult(Result.Status.OK, activeEditFirstVOByActiveId);
    }


}
