package com.hyp.myweixin.service.qubaoming;

import com.hyp.myweixin.pojo.qubaoming.query.active.ActiveCreateThirdQuery;
import com.hyp.myweixin.pojo.qubaoming.vo.active.ValidateUnCompleteByActiveUserIdVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/15 16:03
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class QubaomingActiveCreateServiceTest {

    @Autowired
    private QubaomingActiveCreateService qubaomingActiveCreateService;

    @Test
    public void validateUnCompleteByActiveUserId() {
        ValidateUnCompleteByActiveUserIdVO validateUnCompleteByActiveUserIdVO =
                qubaomingActiveCreateService.validateUnCompleteByActiveUserId(1000000);

    }

    @Test
    public void createActiveThird() {

        ActiveCreateThirdQuery th = new ActiveCreateThirdQuery();
        th.setUserId(2);
        th.setActiveId(1);
        th.setCompanyId(1);
        th.setNowTime("");
        th.setSign("");

        Integer activeThird = qubaomingActiveCreateService.createActiveThird(th);
        log.info("查询结果：{}", activeThird);
    }
}