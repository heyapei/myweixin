package com.hyp.myweixin.service.qubaoming;

import com.hyp.myweixin.pojo.qubaoming.model.QubaomingWeixinUser;
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
 * @Date 2020/8/15 23:39
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class QubaomingWeixinUserServiceTest {


    @Autowired
    private QubaomingWeixinUserService qubaomingWeixinUserService;

    @Test
    public void getQubaomingWeixinUserByOpenId() {
        QubaomingWeixinUser qubaomingWeixinUserByOpenId = qubaomingWeixinUserService.getQubaomingWeixinUserByOpenId("123");
    }

    @Test
    public void insertReturnPk() {
    }

    @Test
    public void deleteByPk() {
    }

    @Test
    public void updateSelectiveQubaomingWeixinUserBase() {
    }

    @Test
    public void selectByPkId() {
    }
}