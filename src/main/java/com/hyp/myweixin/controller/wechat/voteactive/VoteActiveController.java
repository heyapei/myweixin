package com.hyp.myweixin.controller.wechat.voteactive;

import com.hyp.myweixin.pojo.dto.WeixinVoteWorkDTO;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.VoteActiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

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
    private VoteActiveService voteActiveService;


    @RequestMapping("create")
    public Result createVoteWork(HttpServletRequest httpServletRequest, WeixinVoteWorkDTO weixinVoteWorkDTO) {
        /*鉴权*/


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
