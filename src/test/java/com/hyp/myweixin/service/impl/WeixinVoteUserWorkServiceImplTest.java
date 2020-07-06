package com.hyp.myweixin.service.impl;
import java.util.Date;

import com.hyp.myweixin.pojo.modal.WeixinVoteUserWork;
import com.hyp.myweixin.pojo.vo.page.WeixinVoteUserWorkDiffVO;
import com.hyp.myweixin.service.WeixinVoteUserWorkService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/14 19:58
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class WeixinVoteUserWorkServiceImplTest {
    @Autowired
    private WeixinVoteUserWorkService weixinVoteUserWorkService;
    @Autowired
    private WeixinVoteWorkServiceImpl weixinVoteWorkService;

    @Test
    public void justVoteLegal() {
        WeixinVoteUserWork weixinVoteUserWork = new WeixinVoteUserWork();
        weixinVoteUserWork.setId(0);
        weixinVoteUserWork.setAvatarUrl("");
        weixinVoteUserWork.setNickName("");
        weixinVoteUserWork.setGender("1");
        weixinVoteUserWork.setCity("");
        weixinVoteUserWork.setProvince("");
        weixinVoteUserWork.setCountry("");
        Date as = new Date(new Date().getTime()-24*60*60*1000);
        weixinVoteUserWork.setCreateTime(as);
        weixinVoteUserWork.setUpdateTime(new Date());
        weixinVoteUserWork.setWorkId(8);
        weixinVoteUserWork.setOpenId("");
        weixinVoteUserWork.setIp(0L);
        weixinVoteUserWork.setExt1("");
        weixinVoteUserWork.setExt2("");
        weixinVoteUserWork.setExt3("");
        weixinVoteUserWork.setExt4("");
        weixinVoteUserWork.setExt5("");
        weixinVoteUserWork.setOpenId("oF9lL5K5F_q_SLf8XEsmTJ101Tv4");


        String s = weixinVoteUserWorkService.judgeVoteLegal(weixinVoteUserWork);
        log.info("查询出来的数据：" + s);

    }


    @Test
    public void addUserVote() {
        WeixinVoteUserWork weixinVoteUserWork = new WeixinVoteUserWork();
        weixinVoteUserWork.setAvatarUrl("1");
        weixinVoteUserWork.setWorkId(1);

        int i = weixinVoteUserWorkService.addUserVote(weixinVoteUserWork);
        log.info("查询出来的数据：" + i);
    }




    @Test
    public void getUserWorkDiff() {
        WeixinVoteUserWorkDiffVO userWorkDiff = weixinVoteWorkService.getUserWorkDiff(14);
        log.info("查询结果：" + userWorkDiff.toString());

    }
}