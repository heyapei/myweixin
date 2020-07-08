package com.hyp.myweixin.service.impl;

import com.hyp.myweixin.pojo.dto.WeixinVoteWorkDTO;
import com.hyp.myweixin.pojo.modal.WeixinVoteOrganisers;
import com.hyp.myweixin.pojo.query.voteactive.Page2OrgShowQuery;
import com.hyp.myweixin.pojo.query.voteactive.Page3RegulationQuery;
import com.hyp.myweixin.service.VoteActiveService;
import com.hyp.myweixin.utils.MyErrorList;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;


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
    public void createPage3Regulation() {
        Page3RegulationQuery page3RegulationQuery = new Page3RegulationQuery();
        page3RegulationQuery.setUserId(27);
        page3RegulationQuery.setVoteWorkId(42);
        page3RegulationQuery.setActiveStartTime("2020-06-01 12:11");
        page3RegulationQuery.setActiveEndTime("2020-06-02 12:11");
        page3RegulationQuery.setActiveConfRepeatVote(1);
        page3RegulationQuery.setActiveConfVoteType(12);
        page3RegulationQuery.setActiveConfSignUp(0);
        page3RegulationQuery.setActiveUploadStartTime("2020-06-02 12:11");
        page3RegulationQuery.setActiveUploadEndTime("2020-06-02 12:11");
        page3RegulationQuery.setActiveConfNeedWeixin(1);
        page3RegulationQuery.setActiveConfNeedPhone(1);

        MyErrorList page3Regulation = voteActiveService.createPage3Regulation(page3RegulationQuery);
        log.info("查询结果：{}", page3Regulation.toPlainString());

    }

    @Test
    public void createPage2AndImg() {
        Page2OrgShowQuery page2Query = new Page2OrgShowQuery();
        page2Query.setUserId(27);
        page2Query.setVoteWorkId(41);
        page2Query.setIsShowIndex(0);
        page2Query.setShareImg("活动分享图");
        page2Query.setHasOrganisers(1);
        WeixinVoteOrganisers weixinVoteOrganisers = new WeixinVoteOrganisers();
        weixinVoteOrganisers.setId(0);
        weixinVoteOrganisers.setVoteBaseId(41);
        weixinVoteOrganisers.setName("公司的名字");
        weixinVoteOrganisers.setLogoImg("公司的logo图片");
        weixinVoteOrganisers.setOrganisersDesc("");
        weixinVoteOrganisers.setPhone("");
        weixinVoteOrganisers.setAddress("");
        weixinVoteOrganisers.setCompany("");
        weixinVoteOrganisers.setType("");
        weixinVoteOrganisers.setJobMajor("");
        weixinVoteOrganisers.setBuildTime(new Date());
        weixinVoteOrganisers.setCorporate("");
        weixinVoteOrganisers.setWeixinQrCode("微信图片");
        Integer page2AndImg = voteActiveService.createPage2AndImg(page2Query);
        log.info("查询结果：{}", page2AndImg);
    }

    @Test
    public void createBaseVoteWork() {
        Integer baseVoteWork = voteActiveService.createBaseVoteWork(27);
        log.info("查询结果：{}", baseVoteWork);
    }

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