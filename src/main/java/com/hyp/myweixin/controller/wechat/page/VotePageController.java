package com.hyp.myweixin.controller.wechat.page;

import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.pojo.vo.page.VoteDetailByWorkIdVO;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.WeixinVoteBaseService;
import com.hyp.myweixin.utils.MyRequestVailDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/9 21:10
 * @Description: TODO
 */
@RestController
@RequestMapping("/page/vote")
@Slf4j
public class VotePageController {

    @Autowired
    private MyRequestVailDateUtil myRequestValidateUtil;
    @Autowired
    private SecretKeyPropertiesValue secretKeyPropertiesValue;
    @Autowired
    private WeixinVoteBaseService weixinVoteBaseService;


    @PostMapping("/work/detail")
    public Result getVoteWorkByPage(HttpServletRequest request,
                                    int workId) {

        VoteDetailByWorkIdVO voteWorkByWorkId = weixinVoteBaseService.getVoteWorkByWorkId(workId);
        if (voteWorkByWorkId == null) {
            return Result.buildResult(Result.Status.NOT_FOUND);
        }
        return Result.buildResult(Result.Status.OK, voteWorkByWorkId);
    }
}
