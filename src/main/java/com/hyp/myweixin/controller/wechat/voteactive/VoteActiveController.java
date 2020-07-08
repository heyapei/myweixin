package com.hyp.myweixin.controller.wechat.voteactive;

import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.dto.WeixinVoteWorkDTO;
import com.hyp.myweixin.pojo.query.voteactive.Page2OrgShowQuery;
import com.hyp.myweixin.pojo.query.voteactive.Page3RegulationQuery;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.VoteActiveService;
import com.hyp.myweixin.utils.MyErrorList;
import com.hyp.myweixin.utils.MyRequestVailDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/27 20:22
 * @Description: TODO
 */
@RestController
@RequestMapping("/vote/active")
@Slf4j
public class VoteActiveController {

    @Autowired
    private MyRequestVailDateUtil myRequestVailDateUtil;
    @Autowired
    private SecretKeyPropertiesValue secretKeyPropertiesValue;
    @Autowired
    private VoteActiveService voteActiveService;


    /**
     * 保存第三页的内容 如果以前没有则新建 如果有则更新
     *
     * @param httpServletRequest
     * @param page3RegulationQuery 保存用实体
     * @return
     */
    @PostMapping("create/base/savaactiveregulation")
    public Result createActiveRegulation(HttpServletRequest httpServletRequest,
                                         @Validated Page3RegulationQuery page3RegulationQuery
    ) {
        /*鉴权*/
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }
        MyErrorList page3Regulation = voteActiveService.createPage3Regulation(page3RegulationQuery);
        if (page3Regulation.noErrors()) {
            return Result.buildResult(Result.Status.OK);
        } else {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, page3Regulation.toPlainString());
        }
    }


    /**
     * 保存第二页的内容 如果以前没有则新建 如果有则更新
     *
     * @param httpServletRequest
     * @param page2OrgShowQuery  保存用实体
     * @return
     */
    @PostMapping("create/base/saveorgshow")
    public Result createOrgShow(HttpServletRequest httpServletRequest,
                                @Validated Page2OrgShowQuery page2OrgShowQuery
    ) {

        /*鉴权*/
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }
        Integer page2AndImg = voteActiveService.createPage2AndImg(page2OrgShowQuery);
        if (page2AndImg == null || page2AndImg <= 0) {
            return Result.buildResult(Result.Status.DATA_IS_WRONG,
                    "数据未能保存成功，原因如下：创建人信息没有发现/" +
                            "活动没有发现/" +
                            "活动不属于当前用户/" +
                            "创建、保存配置项错误/" +
                            "创建、保存公司信息错误");

        }
        return Result.buildResult(Result.Status.OK);
    }


    /**
     * 这个过程中 要检查是否还有未创建完成的数据
     *
     * @param httpServletRequest
     * @param userId             用户的ID
     * @return
     */
    @PostMapping("create/base/votework")
    public Result createBaseVoteWork(HttpServletRequest httpServletRequest,
                                     int userId) {
        /*鉴权*/
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }

        Integer baseVoteWork = voteActiveService.createBaseVoteWork(userId);
        if (baseVoteWork <= 0) {
            return Result.buildResult(Result.Status.PARAM_NOT_COMPLETE);
        }
        return Result.buildResult(Result.Status.OK, baseVoteWork);
    }

    /**
     * 保存对应的数据 图片上传完成后直接写入数据库对应的活动中去
     *
     * @param httpServletRequest
     * @param userId             用户的ID
     * @return
     */
    @PostMapping("create/base/savepageandimg")
    public Result createBaseVoteWorkSavePageAndImg(HttpServletRequest httpServletRequest,
                                                   int userId,
                                                   int voteWorkId,
                                                   String activeText,
                                                   String activeImg,
                                                   String type
    ) {

        /*鉴权*/
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }
        Integer baseVoteWorkSavePageAndImg = voteActiveService.createBaseVoteWorkSavePageAndImg(userId, voteWorkId, type, activeText, activeImg);
        if (baseVoteWorkSavePageAndImg == null || baseVoteWorkSavePageAndImg <= 0) {
            return Result.buildResult(Result.Status.DATA_IS_WRONG,
                    "数据未能保存成功，原因如下：用户没有未创建完成的活动/" +
                            "该不属于该用户/" +
                            "上传的文件数据没有指定文件类型");
        }
        return Result.buildResult(Result.Status.OK);
    }


    /**
     * 基本上可以弃用了 没有必要了
     *
     * @param httpServletRequest
     * @param weixinVoteWorkDTO
     * @return
     */
    @Deprecated
    @PostMapping("create/votework")
    public Result createVoteWork(HttpServletRequest httpServletRequest, @Valid WeixinVoteWorkDTO weixinVoteWorkDTO) {
        /*鉴权*/
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }

        if (weixinVoteWorkDTO == null) {
            return Result.buildResult(Result.Status.ERROR);
        }
        Integer voteWork = voteActiveService.createVoteWork(weixinVoteWorkDTO);
        if (voteWork == null || voteWork <= 0) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR);
        } else {
            return Result.buildResult(Result.Status.OK);
        }
    }

    @RequestMapping("file/upload")
    public Result fileUpload(@RequestParam("file") MultipartFile file, String type) {
        Result coversImg = voteActiveService.saveSingleRes(file, type);
        log.info("保存数据：{}", coversImg.toString());
        return coversImg;
    }


}
