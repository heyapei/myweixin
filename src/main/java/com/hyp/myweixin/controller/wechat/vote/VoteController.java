package com.hyp.myweixin.controller.wechat.vote;

import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.modal.*;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.UserNoOpenIdIdLog;
import com.hyp.myweixin.service.WeixinVoteUserWorkService;
import com.hyp.myweixin.service.WeixinVoteWorkService;
import com.hyp.myweixin.utils.MyIpMacUtil;
import com.hyp.myweixin.utils.MyRequestVailDateUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/6 16:58
 * @Description: TODO 用户对活动进行投票
 */
@RestController
@RequestMapping("/wechat/v1/vote")
@Slf4j
public class VoteController {


    @Autowired
    private MyRequestVailDateUtil myRequestVailDateUtil;
    @Autowired
    private SecretKeyPropertiesValue secretKeyPropertiesValue;
    @Autowired
    private UserNoOpenIdIdLog userNoOpenIdIdLog;
    @Autowired
    private WeixinVoteUserWorkService weixinVoteUserWorkService;
    @Autowired
    private MyIpMacUtil myIpMacUtil;
    @Autowired
    private WeixinVoteWorkService weixinVoteWorkService;


    @RequestMapping("/add")
    public Result addWechatInfo(WeixinVoteUser weixinVoteUser, HttpServletRequest httpServletRequest) {
        boolean b = myRequestVailDateUtil.validateSignMd5(httpServletRequest, secretKeyPropertiesValue.getMd5Key());
        log.info("验证结果：" + b);
        return null;
    }


    @ApiOperation("用户对某个workId进行了投票")
    @PostMapping("/uservoting")
    public Result userVoting(@Validated WeixinVoteUserWork weixinVoteUserWork, HttpServletRequest httpServletRequest) {
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }

        if (StringUtils.isBlank(weixinVoteUserWork.getAvatarUrl())) {
            return Result.buildResult(Result.Status.PARAM_NOT_COMPLETE);
        }
        if (!weixinVoteUserWork.getAvatarUrl().startsWith("https://")) {
            return Result.buildResult(Result.Status.PARAM_TYPE_BIND_ERROR);
        }
        if (StringUtils.isBlank(weixinVoteUserWork.getCity())) {
            return Result.buildResult(Result.Status.PARAM_NOT_COMPLETE);
        }
        if (StringUtils.isBlank(weixinVoteUserWork.getProvince())) {
            return Result.buildResult(Result.Status.PARAM_NOT_COMPLETE);
        }
        if (StringUtils.isBlank(weixinVoteUserWork.getCountry())) {
            return Result.buildResult(Result.Status.PARAM_NOT_COMPLETE);
        }
        if (StringUtils.isBlank(weixinVoteUserWork.getGender())) {
            return Result.buildResult(Result.Status.PARAM_NOT_COMPLETE);
        }
        if (StringUtils.isBlank(weixinVoteUserWork.getNickName())) {
            return Result.buildResult(Result.Status.PARAM_NOT_COMPLETE);
        }
        if (StringUtils.isBlank(weixinVoteUserWork.getOpenId())) {
            return Result.buildResult(Result.Status.PARAM_NOT_COMPLETE);
        }
        log.info("投票的请求参数是：{}", weixinVoteUserWork.toString());


        /*看剩余票数*/
        WeixinVoteWork voteWorkByUserWorkId = weixinVoteWorkService.getVoteWorkByUserWorkId(weixinVoteUserWork.getWorkId());
        Integer remainderByOpenIdTime = weixinVoteUserWorkService.
                getRemainderByOpenIdTime(voteWorkByUserWorkId.getActiveVoteBaseId(), weixinVoteUserWork.getOpenId());
        if (remainderByOpenIdTime <= 0) {
            return Result.buildResult(Result.Status.UNAUTHORIZED, "你已用完当前所有的投票次数", remainderByOpenIdTime);
        }

        /*投票数据限制*/

        String judgeVoteLegalResult = weixinVoteUserWorkService.judgeVoteLegal(weixinVoteUserWork);
        if (StringUtils.isNotBlank(judgeVoteLegalResult)) {
            return Result.buildResult(Result.Status.UNAUTHORIZED, judgeVoteLegalResult, remainderByOpenIdTime);
        }

        log.info("投票数据限制判断完成{}", judgeVoteLegalResult.toString());


        WeixinUserOptionLog weixinUserOptionLog = new WeixinUserOptionLog();
        weixinUserOptionLog.setOptionType(WeixinUserOptionConfig.typeEnum.VOTE_WEiXIN_VOTE_WORK.getType());
        weixinUserOptionLog.setOptionDesc(WeixinUserOptionConfig.typeEnum.VOTE_WEiXIN_VOTE_WORK.getMsg());
        weixinUserOptionLog.setOptionObject(weixinVoteUserWork.getWorkId() + "");
        log.info("微信用户操作数据：{}", weixinUserOptionLog.toString());
        userNoOpenIdIdLog.addUserOperationLog(weixinUserOptionLog, httpServletRequest);


        weixinVoteUserWork.setIp(myIpMacUtil.ipToLong(myIpMacUtil.getRealIP(httpServletRequest)));
        int i = weixinVoteUserWorkService.addUserVote(weixinVoteUserWork);
        if (i > 0) {
            return Result.buildResult(Result.Status.OK, "保存用户对作品：" + weixinVoteUserWork.getWorkId() + "进行投票，成功", remainderByOpenIdTime);
        } else {
            return Result.buildResult(Result.Status.SERVER_ERROR);
        }

    }

}
