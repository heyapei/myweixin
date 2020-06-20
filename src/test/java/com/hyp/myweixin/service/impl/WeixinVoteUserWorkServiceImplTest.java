package com.hyp.myweixin.service.impl;

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