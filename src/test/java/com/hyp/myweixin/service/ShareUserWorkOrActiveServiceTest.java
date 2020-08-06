package com.hyp.myweixin.service;

import com.hyp.myweixin.pojo.vo.page.activeeditor.ShareUserWorkVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/6 20:08
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ShareUserWorkOrActiveServiceTest {

    @Autowired
    private ShareUserWorkOrActiveService shareUserWorkOrActiveService;

    @Test
    public  void getShareUserWorkVOByUserWorkId() {


        ShareUserWorkVO shareUserWorkVOByUserWorkId = shareUserWorkOrActiveService.getShareUserWorkVOByUserWorkId(5);
        log.info("查询结果：{}", shareUserWorkVOByUserWorkId);
    }

    @Test
    public void getShareActiveVOByActiveId() {
    }
}