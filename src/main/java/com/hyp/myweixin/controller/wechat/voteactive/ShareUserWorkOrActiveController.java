package com.hyp.myweixin.controller.wechat.voteactive;

import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.vo.page.activeeditor.ShareActiveVO;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.ShareUserWorkOrActiveService;
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
 * @Date 2020/7/29 22:25
 * @Description: TODO
 */
@RestController
@RequestMapping("/share/workOrActive")
@Slf4j
public class ShareUserWorkOrActiveController {

    @Autowired
    private MyRequestVailDateUtil myRequestVailDateUtil;
    @Autowired
    private SecretKeyPropertiesValue secretKeyPropertiesValue;
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private ShareUserWorkOrActiveService shareUserWorkOrActiveService;


    /**
     * 通过活动ID获取活动的分享图
     *
     * @param activeId 活动ID
     * @return
     */
    @ApiOperation("通过活动ID获取活动的分享图")
    @PostMapping("getActiveShare/activeId")
    public Result<ShareActiveVO> getActiveWorkOverviewForOwnerVOByUserId(
            @ApiParam(name = "活动ID", value = "activeId", required = true)
            @RequestParam(required = true) Integer activeId) {
        /*鉴权*/
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            return Result.buildResult(Result.Status.UNAUTHORIZED, "密钥验证错误");
        }

        ShareActiveVO shareActiveVOByActiveId = null;
        try {
            shareActiveVOByActiveId = shareUserWorkOrActiveService.getShareActiveVOByActiveId(activeId);
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return Result.buildResult(Result.Status.OK, shareActiveVOByActiveId);
    }


}
