package com.hyp.myweixin.controller.wechat.voteactive;

import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.dto.WeixinVoteWorkDTO;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.VoteActiveService;
import com.hyp.myweixin.utils.MyRequestVailDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
