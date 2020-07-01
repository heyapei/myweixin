package com.hyp.myweixin.service.impl;
import java.util.Date;

import com.hyp.myweixin.pojo.dto.WeixinVoteWorkDTO;
import com.hyp.myweixin.service.VoteActiveService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/1 18:17
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class VoteActiveServiceImplTest {

    @Autowired
    private VoteActiveService voteActiveService;

    @Test
    public void createVoteWork() {
        WeixinVoteWorkDTO weixinVoteWorkDTO = new WeixinVoteWorkDTO();
        weixinVoteWorkDTO.setActiveImg("activeImg");
        weixinVoteWorkDTO.setActiveName("activeName");
        weixinVoteWorkDTO.setActiveDesc("activeDesc");
        weixinVoteWorkDTO.setActiveDescImg("activeDescImg");
        weixinVoteWorkDTO.setActiveReward("activeReward");
        weixinVoteWorkDTO.setActiveRewardImg("activeRewardImg");
        weixinVoteWorkDTO.setActiveStartTime(new Date());
        weixinVoteWorkDTO.setActiveEndTime(new Date());
        weixinVoteWorkDTO.setActivePublic(0);
        weixinVoteWorkDTO.setCreateSysUserId(0);
        weixinVoteWorkDTO.setActiveVoteBaseId(0);
        weixinVoteWorkDTO.setActiveConfMusicId(0);
        weixinVoteWorkDTO.setActiveConfRepeatVote(0);
        weixinVoteWorkDTO.setActiveConfVoteType(0);
        weixinVoteWorkDTO.setActiveConfSignUp(0);
        weixinVoteWorkDTO.setActiveConfVerify(0);
        weixinVoteWorkDTO.setActiveConfNumHide(0);
        weixinVoteWorkDTO.setActiveConfUserHide(0);
        weixinVoteWorkDTO.setActiveConfRankHide(0);
        weixinVoteWorkDTO.setUpdateTime(new Date());
        weixinVoteWorkDTO.setActiveUploadStartTime(new Date());
        weixinVoteWorkDTO.setActiveUploadEndTime(new Date());
        weixinVoteWorkDTO.setActiveConfSex("setActiveConfSex");
        weixinVoteWorkDTO.setActiveConfRegion("setActiveConfRegion");
        weixinVoteWorkDTO.setActiveConfNeedWeixin(0);
        weixinVoteWorkDTO.setActiveConfNeedPhone(0);
        weixinVoteWorkDTO.setActiveConfShareImg("setActiveConfShareImg");
        weixinVoteWorkDTO.setName("setName");
        weixinVoteWorkDTO.setLogoImg("setLogoImg");
        weixinVoteWorkDTO.setPhone("setPhone");
        weixinVoteWorkDTO.setWeixinQrCode("setWeixinQrCode");

        Integer voteWork = voteActiveService.createVoteWork(weixinVoteWorkDTO);
        log.info("查询结果：{}", voteWork);
    }

    @Test
    public void saveSingleRes() {
    }
}