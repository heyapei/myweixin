package com.hyp.myweixin.service.impl;

import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.pojo.modal.WeixinVoteUserWork;
import com.hyp.myweixin.pojo.modal.WeixinVoteWork;
import com.hyp.myweixin.pojo.vo.page.WeixinVoteUserWorkDiffVO;
import com.hyp.myweixin.pojo.vo.page.activeeditor.UserUploadRightVO;
import com.hyp.myweixin.service.WeixinVoteUserWorkService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

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
    public void getRemainderByOpenIdTime() {

        Integer remainderByOpenIdTime = weixinVoteUserWorkService.
                getRemainderByOpenIdTime(1, "1231");
        log.info("查询结果：{}", remainderByOpenIdTime.toString());
    }

    @Test
    public void judgeUserUploadRight() {
        UserUploadRightVO userUploadRightVO = weixinVoteUserWorkService.
                judgeUserUploadRight(25, 76);
        log.info("查询结果：{}", userUploadRightVO.toString());

        /*log.info("查询结果：{}",weixinVoteUserWorkService.
                judgeUserUploadRight(25, 77));*/
    }

    @Test
    public void getWeiXinVoteWorkListByUserId() {

        List<WeixinVoteWork> weiXinVoteWorkListByUserId = weixinVoteWorkService.
                getWeiXinVoteWorkListByUserId(27, 76);
        log.info("查询结果：{}", weiXinVoteWorkListByUserId.toString());
    }

    @Test
    public void getVoteWorkAllWorkByPage() {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageNum(1);
        pageInfo.setPageSize(1);
        WeixinVoteWork weixinVoteWork = new WeixinVoteWork();
        weixinVoteWork.setActiveVoteBaseId(1);
        weixinVoteWork.setVoteWorkShowOrder(0);
        weixinVoteWork.setVoteWorkOr(-1);
        weixinVoteWork.setVoteWorkCountNum(null);

        PageInfo voteWorkAllWorkByPage = weixinVoteWorkService.getVoteWorkAllWorkByPage(weixinVoteWork, pageInfo);

        log.info("查询结果：{}", voteWorkAllWorkByPage.toString());

    }

    @Test
    public void getWeixinVoteUserWorkNumByOpenIdTime() {
        Integer weixinVoteUserWorkNumByOpenIdTime = weixinVoteUserWorkService.getWeixinVoteUserWorkNumByOpenIdTime(1, "!@", 1, new Date(), new Date());
        log.info("查询结果：{}", weixinVoteUserWorkNumByOpenIdTime);
    }

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
        Date as = new Date(new Date().getTime() - 24 * 60 * 60 * 1000);
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