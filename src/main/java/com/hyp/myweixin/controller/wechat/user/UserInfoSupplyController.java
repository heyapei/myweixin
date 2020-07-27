package com.hyp.myweixin.controller.wechat.user;

import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.query.user.supply.AddUserSupplyQuery;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.UserInfoSupplyService;
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
 * @Date 2020/7/27 20:42
 * @Description: TODO
 */
@RestController
@RequestMapping("/user/info/supply")
@Slf4j
public class UserInfoSupplyController {


    @Autowired
    private MyRequestVailDateUtil myRequestValidateUtil;
    @Autowired
    private SecretKeyPropertiesValue secretKeyPropertiesValue;
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private UserInfoSupplyService userInfoSupplyService;

    @ApiOperation("添加用户补充的数据")
    @PostMapping("add")
    public Result addWechatInfo(
            @ApiParam(name = "保存用户补充信息数据", required = true)
            @Validated AddUserSupplyQuery addUserSupplyQuery) {

        boolean b = myRequestValidateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }
        Integer pkId = null;
        try {
            userInfoSupplyService.addWeixinUserInfoSupply(addUserSupplyQuery);
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.BAD_REQUEST, e.getMessage());
        }

        if (pkId == null) {
            return Result.buildResult(Result.Status.BAD_REQUEST, "未能成功保存用户补充的数据");
        }

        return Result.buildResult(Result.Status.OK, pkId);
    }

}
