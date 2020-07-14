package com.hyp.myweixin.controller.wechat.voteactive;

import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.query.voteuserwork.SaveVoteUserQuery;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.WeixinVoteWorkService;
import com.hyp.myweixin.utils.MyRequestVailDateUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
        return Result.buildResult(Result.Status.OK, weixinVoteWorkReturnPK);
    }


}
