package com.hyp.myweixin.service.qubaoming;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/18 19:57
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserEnrollActiveServiceTest {

    @Autowired
    private UserEnrollActiveService userEnrollActiveService;

    @Test
    public void userEnrollActive() {

        userEnrollActiveService.UserEnrollActive(1, 1);
    }
}