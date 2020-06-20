package com.hyp.myweixin.controller.wechat.user;


import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.config.weixin.WeChatPropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.modal.WeixinUserOptionConfig;
import com.hyp.myweixin.pojo.modal.WeixinUserOptionLog;
import com.hyp.myweixin.pojo.modal.WeixinVoteUser;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.UserNoOpenIdIdLog;
import com.hyp.myweixin.service.WeixinVoteBaseService;
import com.hyp.myweixin.service.WeixinVoteUserService;
import com.hyp.myweixin.service.WeixinVoteWorkService;
import com.hyp.myweixin.utils.MyRequestVailDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/5/28 18:44
 * @Description: TODO
 */
@RestController
@RequestMapping("/wechat/v1/user")
@Slf4j
public class UserController {

    @Autowired
    private WeixinVoteUserService weixinVoteUserService;
    @Autowired
    private MyRequestVailDateUtil myRequestValidateUtil;
    @Autowired
    private SecretKeyPropertiesValue secretKeyPropertiesValue;

    @Autowired
    private UserNoOpenIdIdLog userNoOpenIdIdLog;

    @Autowired
    private WeixinVoteBaseService weixinVoteBaseService;


    @PostMapping("/add")
    public Result addWechatInfo(WeixinVoteUser weixinVoteUser, HttpServletRequest httpServletRequest) {
        log.info("其中对应的weixin数据：" + weixinVoteUser.toString());
        boolean b = myRequestValidateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }
        if (weixinVoteUser.getOpenId() == null || StringUtils.isBlank(weixinVoteUser.getOpenId())) {
            throw new MyDefinitionException(
                    Integer.parseInt(Result.Status.PARAM_IS_BLANK.getCode()),
                    "微信用户的openId不可以为空");
        }
        WeixinVoteUser userByOpenId = weixinVoteUserService.getUserByOpenId(weixinVoteUser.getOpenId());
        if (userByOpenId != null) {
            /*return Result.buildResult(Result.Status.OK,
                    "该微信用户已经是我们的第" + userByOpenId.getId()
                            + "位用户，无需重复保存");*/
            Integer integer = weixinVoteUserService.updateWeixinUserByOpenId(weixinVoteUser);
            if (integer != null && integer > 0) {
                return Result.buildResult(Result.Status.OK,
                        "更新第" + weixinVoteUser.getId() + "位用户信息成功", weixinVoteUser.getId());
            } else {
                return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR,
                        "更新第" + weixinVoteUser.getId() + "位用户信息失败", weixinVoteUser.getId());
            }
        }

        int i = weixinVoteUserService.addWechatInfo(weixinVoteUser);
        if (i <= 0) {
            throw new MyDefinitionException("未能正确保存用户数据");
        }
        return Result.buildResult(Result.Status.OK,
                "欢迎" + weixinVoteUser.getNickName()
                        + "！我们的第" + i + "用户！", weixinVoteUser.getId());
    }


    /**
     * 记录用户进入小程序 记录用户的地区位置信息 ip地址信息 使用设备类型信息 进入小程序的时间 操作的类型 等信息
     *
     * @param httpServletRequest
     */
    @RequestMapping("userin")
    public Result userIn(HttpServletRequest httpServletRequest) {
        String userAgent = httpServletRequest.getHeader("User-Agent");
        System.out.println(userAgent);
        boolean b = myRequestValidateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }
        WeixinUserOptionLog weixinUserOptionLog = new WeixinUserOptionLog();
        weixinUserOptionLog.setOptionType(WeixinUserOptionConfig.typeEnum.INTO_WEiXIN_VOTE.getType());
        weixinUserOptionLog.setOptionDesc(WeixinUserOptionConfig.typeEnum.INTO_WEiXIN_VOTE.getMsg());
        userNoOpenIdIdLog.addUserOperationLog(weixinUserOptionLog, httpServletRequest);
        return Result.buildResult(Result.Status.OK, "保存用户操作日志，日志类型：" + WeixinUserOptionConfig.typeEnum.INTO_WEiXIN_VOTE.getMsg());
    }

    /**
     * 记录用户进入小程序 记录用户的地区位置信息 ip地址信息 使用设备类型信息 进入小程序的时间 操作的类型 等信息
     *
     * @param httpServletRequest
     */
    @PostMapping("viewwork")
    public Result userViewVoteWork(HttpServletRequest httpServletRequest, Integer voteWorkId) {
        boolean b = myRequestValidateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }


        WeixinUserOptionLog weixinUserOptionLog = new WeixinUserOptionLog();
        weixinUserOptionLog.setOptionType(WeixinUserOptionConfig.typeEnum.VIEW_WEiXIN_VOTE_WORK.getType());
        weixinUserOptionLog.setOptionDesc(WeixinUserOptionConfig.typeEnum.VIEW_WEiXIN_VOTE_WORK.getMsg());
        weixinUserOptionLog.setOptionObject(voteWorkId + "");
        userNoOpenIdIdLog.addUserOperationLog(weixinUserOptionLog, httpServletRequest);

        int i = weixinVoteBaseService.updateVoteBaseViewNum(voteWorkId);
        if (i > 0) {
            return Result.buildResult(Result.Status.OK, "保存用户浏览操作日志，日志类型："
                    + WeixinUserOptionConfig.typeEnum.VIEW_WEiXIN_VOTE_WORK.getMsg()
                    + "，查看的活动ID：" + voteWorkId);
        } else {
            return Result.buildResult(Result.Status.SERVER_ERROR);
        }
    }


    @Autowired
    private WeixinVoteWorkService weixinVoteWorkService;

    /**
     * 记录用户进入小程序 记录用户的地区位置信息 ip地址信息 使用设备类型信息 进入小程序的时间 操作的类型 等信息
     *
     * @param httpServletRequest
     */
    @PostMapping("vieuserwwork")
    public Result userViewUserWork(HttpServletRequest httpServletRequest, Integer userWorkId) {
        boolean b = myRequestValidateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }

        WeixinUserOptionLog weixinUserOptionLog = new WeixinUserOptionLog();
        weixinUserOptionLog.setOptionType(WeixinUserOptionConfig.typeEnum.INTO_WEiXIN_VOTE_USER_WORK.getType());
        weixinUserOptionLog.setOptionDesc(WeixinUserOptionConfig.typeEnum.INTO_WEiXIN_VOTE_USER_WORK.getMsg());
        weixinUserOptionLog.setOptionObject(userWorkId + "");
        userNoOpenIdIdLog.addUserOperationLog(weixinUserOptionLog, httpServletRequest);

        int i = weixinVoteWorkService.updateVoteWorkViewNum(userWorkId);
        if (i > 0) {
            return Result.buildResult(Result.Status.OK, "保存用户浏览操作日志，日志类型："
                    + WeixinUserOptionConfig.typeEnum.INTO_WEiXIN_VOTE_USER_WORK.getMsg()
                    + "，查看的作品ID：" + userWorkId);
        } else {
            return Result.buildResult(Result.Status.SERVER_ERROR);
        }
    }


    /*下面是一个测试*/


    @Autowired
    private WeChatPropertiesValue propertiesValue;

    /**
     * 麻痹呀 突然又自己好了
     *
     * @return
     */
    @GetMapping("testWeixin")
    public String sendTextMail2(HttpServletRequest httpServletRequest) {

        boolean b = myRequestValidateUtil.validateSignMd5(httpServletRequest, secretKeyPropertiesValue.getMd5Key());
        log.info("验证结果：" + b);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }
        String appid = propertiesValue.getAppid();
        log.info("微信：ci" + appid + "32");

        return "AAAAA";
    }
}