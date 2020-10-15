package com.hyp.myweixin.controller.qubaoming.message;

import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.pojo.qubaoming.query.message.UserSubmitMessageQuery;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.qubaoming.UserSubmitMessageService;
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
 * @Date 2020/9/26 11:41
 * @Description: TODO 订阅信息
 */
@RestController
@RequestMapping("qubaoming/info/notice")
@Slf4j
public class UserMessageController {

    @Autowired
    private MyRequestVailDateUtil myRequestVailDateUtil;
    @Autowired
    private SecretKeyPropertiesValue secretKeyPropertiesValue;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private UserSubmitMessageService userSubmitMessageService;


    @ApiOperation(value = "趣报名通知信息订阅", tags = {"趣报名通知信息"})
    @PostMapping("submit/infoType")
    public Result<Object> getActiveListByCompanyIdAndPage(
            @ApiParam(name = "分页信息", value = "activeShowByCompanyIdQuery", required = true)
            @Validated UserSubmitMessageQuery userSubmitMessageQuery,
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

        // 需要的参数 用户信息 模板编号 formId

        userSubmitMessageService.addQubaomingMessageSubmit(userSubmitMessageQuery);

        return Result.buildResult(Result.Status.OK);
    }


}
