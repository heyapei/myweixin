package com.hyp.myweixin.service.impl;

import com.github.pagehelper.PageInfo;
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