package com.hyp.myweixin.controller.wechat.voteactive;

import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.query.activeedit.ActiveEditFourthQuery;
import com.hyp.myweixin.pojo.query.activeedit.ActiveEditSecondQuery;
import com.hyp.myweixin.pojo.query.activeedit.ActiveEditThirdQuery;
import com.hyp.myweixin.pojo.vo.page.activeeditor.ActiveEditFirstVO;
import com.hyp.myweixin.pojo.vo.page.activeeditor.ActiveEditFourthVO;
import com.hyp.myweixin.pojo.vo.page.activeeditor.ActiveEditSecondVO;
import com.hyp.myweixin.pojo.vo.page.activeeditor.ActiveEditThirdVO;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.VoteActiveService;
import com.hyp.myweixin.service.WeixinVoteBaseEditService;
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
     * 更新活动编辑第四页的内容
     * 要求:
     * 1. 判断是否为管理员
     * <p>
     * 逻辑：
     * 1. 这里不需要管理任何数据是否存在因为第二步中已经完成了基础的初始化
     *
     * @return
     */
    @ApiOperation("更新活动编辑第四页的内容")
    @PostMapping("editFourthPage")
    public Result editFourthPage(
            @ApiParam(name = "更新活动第四页信息的参数", value = "activeEditFourthQuery", required = true)
            @Validated
                    ActiveEditFourthQuery activeEditFourthQuery
    ) {
        /*鉴权*/
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }
        try {
            Integer integer = weixinVoteBaseEditService.editActiveEditFourthQuery(activeEditFourthQuery);
            if (integer <= 0) {
                return Result.buildResult(Result.Status.SERVER_ERROR, "更新活动第四页信息失败");
            }
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.SERVER_ERROR, e.getMessage());
        }
        return Result.buildResult(Result.Status.OK, "更新活动第四页信息成功");
    }


    /**
     * 回显活动编辑第四页的内容
     * 要求:
     * 1. 判断是否为管理员
     *
     * @param activeId 活动ID
     * @param userId   用户ID
     * @return
     */
    @ApiOperation("回显活动编辑第四页的内容")
    @PostMapping("showFourthPage")
    public Result showFourthPage(
            @ApiParam(name = "活动ID", value = "activeId", required = true)
                    Integer activeId,
            @ApiParam(name = "用户ID", value = "userId", required = true)
                    Integer userId
    ) {
        /*鉴权*/
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }
        ActiveEditFourthVO activeEditFourthVOByActiveId = null;
        try {
            activeEditFourthVOByActiveId = weixinVoteBaseEditService.getActiveEditFourthVOByActiveId(activeId, userId);
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.SERVER_ERROR, e.getMessage());
        }
        return Result.buildResult(Result.Status.OK, activeEditFourthVOByActiveId);
    }


    /**
     * 更新活动编辑第三页的内容
     * 要求:
     * 1. 判断是否为管理员
     * <p>
     * 逻辑：
     * 1. 这里不需要管理任何数据是否存在因为第二步中已经完成了基础的初始化
     *
     * @return
     */
    @ApiOperation("更新活动编辑第三页的内容")
    @PostMapping("editThirdPage")
    public Result editThirdPage(
            @ApiParam(name = "更新活动第三页信息的参数", value = "activeEditThirdQuery", required = true)
            @Validated
                    ActiveEditThirdQuery activeEditThirdQuery
    ) {
        /*鉴权*/
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }

        try {
            Integer integer = weixinVoteBaseEditService.editActiveEditThirdQuery(activeEditThirdQuery);
            if (integer <= 0) {
                return Result.buildResult(Result.Status.SERVER_ERROR, "更新活动第三页信息失败");
            }
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.SERVER_ERROR, e.getMessage());
        }
        return Result.buildResult(Result.Status.OK, "更新活动第三页信息成功");
    }


    /**
     * 回显活动编辑第三页的内容
     * 要求:
     * 1. 判断是否为管理员
     *
     * @param activeId 活动ID
     * @param userId   用户ID
     * @return
     */
    @ApiOperation("回显活动编辑第三页的内容")
    @PostMapping("showThirdPage")
    public Result showThirdPage(
            @ApiParam(name = "活动ID", value = "activeId", required = true)
                    Integer activeId,
            @ApiParam(name = "用户ID", value = "userId", required = true)
                    Integer userId
    ) {
        /*鉴权*/
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }
        ActiveEditThirdVO activeEditThirdVOByActiveId = null;
        try {
            activeEditThirdVOByActiveId = weixinVoteBaseEditService.getActiveEditThirdVOByActiveId(activeId, userId);
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.SERVER_ERROR, e.getMessage());
        }
        return Result.buildResult(Result.Status.OK, activeEditThirdVOByActiveId);
    }


    /**
     * 更新活动编辑第二页的内容
     * 要求:
     * 1. 判断是否为管理员
     * <p>
     * 逻辑：
     * 1. 需要注意的是 如果能到这里说明已经有了活动的主键
     * 2. 如果没有配置项表格需要直接创建
     * 3. 如果还没有主办方信息直接创建
     *
     * @return
     */
    @ApiOperation("更新活动编辑第二页的内容")
    @PostMapping("editSecondPage")
    public Result editSecondPage(
            @ApiParam(name = "更新活动第二页信息的参数", value = "activeEditSecondQuery", required = true)
            @Validated
                    ActiveEditSecondQuery activeEditSecondQuery
    ) {
        /*鉴权*/
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }

        try {
            Integer integer = weixinVoteBaseEditService.editActiveEditSecondQuery(activeEditSecondQuery);
            if (integer <= 0) {
                return Result.buildResult(Result.Status.SERVER_ERROR, "更新活动第二页信息失败");
            }
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.SERVER_ERROR, e.getMessage());
        }
        return Result.buildResult(Result.Status.OK, "更新活动第二页信息成功");
    }

    /**
     * 回显活动编辑第二页的内容
     * 要求:
     * 1. 判断是否为管理员
     *
     * @param activeId 活动ID
     * @param userId   用户ID
     * @return
     */
    @ApiOperation("回显活动编辑第二页的内容")
    @PostMapping("showSecondPage")
    public Result showSecondPage(
            Integer activeId, Integer userId
    ) {
        /*鉴权*/
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }
        ActiveEditSecondVO activeEditSecondVO = null;
        try {
            activeEditSecondVO = weixinVoteBaseEditService.getActiveEditSecondVOByActiveId(activeId, userId);
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.SERVER_ERROR, e.getMessage());
        }
        return Result.buildResult(Result.Status.OK, activeEditSecondVO);
    }


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
    public Result showFirstPage(
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

    /**
     * 保存对应的数据 图片上传完成后直接写入数据库对应的活动中去
     * <p>
     * 该过程通过type进行判断的 判断上传类型 然后向数据库中做存储覆盖
     * 要求:
     * 1. 判断是否为管理员
     *
     * @param userId 用户的ID
     * @return
     */
    @ApiOperation("编辑（更新）活动编辑第一页的内容")
    @PostMapping("editFirstPage")
    public Result editFirstPage(
            @ApiParam(name = "用户ID，用于判断是否为管理员", value = "userId", required = true)
                    int userId,
            @ApiParam(name = "活动ID，用于标记更新的活动", value = "voteWorkId", required = true)
                    int voteWorkId,
            @ApiParam(name = "文字类型，用于保存当前提交上来的文字信息", value = "activeText", required = true)
                    String activeText,
            @ApiParam(name = "图片信息，用于图片数据 使用半角分号(;)进行拼接", value = "activeImg", required = true)
                    String activeImg,
            @ApiParam(name = "数据类型，用于分辨当前提交上来的数据的类型，也是通过该数据类型判断需要更新那些字段数据", value = "type", required = true)
                    String type
    ) {
        /*鉴权*/
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }
        Integer baseVoteWorkSavePageAndImg = weixinVoteBaseEditService.editBaseVoteWorkSavePageAndImg(userId, voteWorkId, type, activeText, activeImg);
        if (baseVoteWorkSavePageAndImg == null || baseVoteWorkSavePageAndImg <= 0) {
            return Result.buildResult(Result.Status.DATA_IS_WRONG,
                    "数据未能保存成功，原因如下：当前用户不是管理员/" +
                            "上传的文件数据没有指定文件类型");
        }
        return Result.buildResult(Result.Status.OK);
    }


}
