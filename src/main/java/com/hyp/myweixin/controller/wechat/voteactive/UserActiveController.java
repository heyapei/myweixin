package com.hyp.myweixin.controller.wechat.voteactive;

import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.query.voteactive.OwnerActiveQuery;
import com.hyp.myweixin.pojo.vo.page.activeeditor.ActiveWorkForOwnerVO;
import com.hyp.myweixin.pojo.vo.page.activeeditor.ActiveWorkOverviewForOwnerVO;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.UserActiveService;
import com.hyp.myweixin.utils.MyRequestVailDateUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/26 17:09
 * @Description: TODO
 */
@RestController
@RequestMapping("/my/show/detail")
@Slf4j
public class UserActiveController {


    @Autowired
    private MyRequestVailDateUtil myRequestVailDateUtil;
    @Autowired
    private SecretKeyPropertiesValue secretKeyPropertiesValue;
    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private UserActiveService userActiveService;


    /**
     * 查询个人活动相关数据
     *
     * @param userId 用户ID
     * @return
     */
    @ApiOperation("查询个人活动相关数据")
    @PostMapping("owner/overview")
    public Result<ActiveWorkOverviewForOwnerVO> getActiveWorkOverviewForOwnerVOByUserId(@PathVariable(required = true)
                                                                                                Integer userId) {
        /*鉴权*/
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            return Result.buildResult(Result.Status.UNAUTHORIZED, "密钥验证错误");
        }
        ActiveWorkOverviewForOwnerVO activeWorkOverviewForOwnerVOByUserId = null;


        try {
            activeWorkOverviewForOwnerVOByUserId = userActiveService.getActiveWorkOverviewForOwnerVOByUserId(userId);
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return Result.buildResult(Result.Status.OK, activeWorkOverviewForOwnerVOByUserId);
    }


    /**
     * 查询自己发起的作品
     * 如果是管理员则查询所有数据
     *
     * @param ownerActiveQuery
     * @return
     */
    @ApiOperation("分页查询自己发起的作品")
    @PostMapping("owner/active")
    public Result<PageInfo<ActiveWorkForOwnerVO>> createActiveSexRegion(@Validated OwnerActiveQuery ownerActiveQuery) {
        /*鉴权*/
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            return Result.buildResult(Result.Status.UNAUTHORIZED, "密钥验证错误");
        }
        PageInfo<ActiveWorkForOwnerVO> activeWorkForOwnerVOListByUserId = null;
        try {
            activeWorkForOwnerVOListByUserId = userActiveService.getActiveWorkForOwnerVOListByUserId(ownerActiveQuery);
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return Result.buildResult(Result.Status.OK, activeWorkForOwnerVOListByUserId);
    }


}
