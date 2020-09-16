package com.hyp.myweixin.service.qubaoming;

import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.pojo.qubaoming.query.active.manager.ActiveManagerByPageQuery;
import com.hyp.myweixin.pojo.qubaoming.query.active.manager.ActiveManagerIndexQuery;
import com.hyp.myweixin.pojo.qubaoming.vo.active.manager.ActiveManagerIndexVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/22 10:06
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ActiveManagerServiceTest {

    @Autowired
    private ActiveManagerService activeManagerService;
    @Test
    public void   getActiveManagerIndexVOByActiveId() {

        ActiveManagerIndexQuery activeManagerIndexQuery = new ActiveManagerIndexQuery();
        activeManagerIndexQuery.setUserId(1);
        activeManagerIndexQuery.setActiveId(1);
        activeManagerIndexQuery.setNowTime("");

        ActiveManagerIndexVO activeManagerIndexVOByActiveId = activeManagerService.getActiveManagerIndexVOByActiveId(activeManagerIndexQuery);
        log.info("查询结果：{}",activeManagerIndexVOByActiveId);
    }


    @Test
    public void getAllSignUpByPageQuery() {
        ActiveManagerByPageQuery activeManagerByPageQuery = new ActiveManagerByPageQuery();
        activeManagerByPageQuery.setUserId(2);
        activeManagerByPageQuery.setActiveId(1);
        activeManagerByPageQuery.setPageNum(1);
        activeManagerByPageQuery.setPageSize(5);
        activeManagerByPageQuery.setNowTime("");
        activeManagerByPageQuery.setSign("");

        PageInfo<Object> allSignUpByPageQuery = activeManagerService.getAllSignUpByPageQuery(activeManagerByPageQuery);

        log.info("查询结果：" + allSignUpByPageQuery);
    }
}