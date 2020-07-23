package com.hyp.myweixin.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/23 19:45
 * @Description: TODO
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class AdministratorsOptionServiceTest {

    @Autowired
    private AdministratorsOptionService administratorsOptionService;

    @Test
    public void isSuperAdministrators() {
        boolean superAdministrators = administratorsOptionService.isSuperAdministrators(25);
        log.info("查询结果：{}", superAdministrators);
    }
}