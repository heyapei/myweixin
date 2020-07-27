package com.hyp.myweixin.service;

import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.pojo.query.voteactive.OwnerActiveQuery;
import com.hyp.myweixin.pojo.vo.page.activeeditor.ActiveWorkForOwnerVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/26 20:45
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserActiveServiceTest {

    @Autowired
    private UserActiveService userActiveService;

    @Test
    public void getActiveWorkForOwnerVOListByUserId() {
        OwnerActiveQuery ownerActiveQuery = new OwnerActiveQuery();
        ownerActiveQuery.setUserId(27);
        ownerActiveQuery.setPageSize(10);
        ownerActiveQuery.setPageNum(1);
        ownerActiveQuery.setActiveStatus(-1);
        PageInfo<ActiveWorkForOwnerVO> activeWorkForOwnerVOListByUserId = userActiveService.getActiveWorkForOwnerVOListByUserId(ownerActiveQuery);
        log.info("查询结果:{}", activeWorkForOwnerVOListByUserId.toString());
    }
}