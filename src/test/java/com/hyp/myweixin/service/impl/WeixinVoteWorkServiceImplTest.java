package com.hyp.myweixin.service.impl;

import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.pojo.modal.WeixinVoteWork;
import com.hyp.myweixin.pojo.query.voteuserwork.SaveVoteUserQuery;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.WeixinVoteWorkService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/8 17:49
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class WeixinVoteWorkServiceImplTest {

    @Autowired
    private WeixinVoteWorkService weixinVoteWorkService;

    @Test
    public void createWeixinVoteWorkReturnPK() {
        SaveVoteUserQuery saveVoteUserQuery = new SaveVoteUserQuery();
        saveVoteUserQuery.setUserId(25);
        saveVoteUserQuery.setActiveId(76);
        saveVoteUserQuery.setVoteWorkUserName("");
        saveVoteUserQuery.setVoteWorkName("");
        saveVoteUserQuery.setVoteWorkDesc("");
        saveVoteUserQuery.setUserPhone("");
        saveVoteUserQuery.setUserWeixin("");
        saveVoteUserQuery.setVoteWorkImgS("");
        Result weixinVoteWorkReturnPK = weixinVoteWorkService.
                createWeixinVoteWorkReturnPK(saveVoteUserQuery);
        log.info("查询结果：" + weixinVoteWorkReturnPK.toString());
    }

    @Test
    public void saveWeixinVoteWorkReturnPK() {
        WeixinVoteWork weixinVoteWork = WeixinVoteWork.init();
        Integer saveWeixinVoteWorkReturnPK = weixinVoteWorkService.
                saveWeixinVoteWorkReturnPK(weixinVoteWork);
        log.info("查询结果：" + saveWeixinVoteWorkReturnPK);
    }

    @Test
    public void getUserWorkCountByActiveId() {
        Integer userWorkCountByActiveId = weixinVoteWorkService.getUserWorkCountByActiveId(500);
        log.info("查询结果：" + userWorkCountByActiveId);
    }

    @Test
    public void getCountWorkByVoteBaseId() {
        Integer countWorkByVoteBaseId = weixinVoteWorkService.getCountWorkByVoteBaseId(1);
        log.info("查询结果：" + countWorkByVoteBaseId);
    }

    @Test
    public void getCountVoteByVoteBaseId() {
        Integer countWorkByVoteBaseId = weixinVoteWorkService.getCountVoteByVoteBaseId(1);
        log.info("查询结果：" + countWorkByVoteBaseId);
    }

    @Test
    public void getVoteWorkAllWorkByPage() {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageNum(1);
        pageInfo.setPageSize(5);
        PageInfo voteWorkAllWorkByPage = weixinVoteWorkService.getVoteWorkAllWorkByPage(null, pageInfo);
        log.info("数据查询{}", voteWorkAllWorkByPage.toString());

    }


    @Test
    public void getWeixinVoteWorkByUserWorkId() {
        log.info("数据查询{}", weixinVoteWorkService.getWeixinVoteWorkByUserWorkId(1).toString());

    }

    @Test
    public void updateVoteWorkViewNum() {
        log.info("数据查询{}", weixinVoteWorkService.updateVoteWorkViewNum(1));
    }
}