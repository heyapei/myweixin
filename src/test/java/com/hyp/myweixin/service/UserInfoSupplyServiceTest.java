package com.hyp.myweixin.service;

import com.hyp.myweixin.pojo.modal.WeixinUserInfoSupply;
import com.hyp.myweixin.pojo.query.user.supply.AddUserSupplyQuery;
import com.hyp.myweixin.pojo.vo.page.user.UserInfoSupplyDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/27 21:49
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserInfoSupplyServiceTest {

    @Autowired
    private UserInfoSupplyService userInfoSupplyService;

    @Test
    public void addWeixinUserInfoSupply() {
        AddUserSupplyQuery addUserSupplyQuery = AddUserSupplyQuery.init();
        addUserSupplyQuery.setUserId(27);
        Integer integer = userInfoSupplyService.addWeixinUserInfoSupply(addUserSupplyQuery);
        log.info("查询结果：{}", integer);
    }

    @Test
    public void updateWeixinUserInfoSupplyByAddUserSupplyQuery() {
        AddUserSupplyQuery addUserSupplyQuery = AddUserSupplyQuery.init();
        addUserSupplyQuery.setUserId(27);
        addUserSupplyQuery.setEmail("heyapei@hotmail.com");
        Integer integer = userInfoSupplyService.updateWeixinUserInfoSupplyByAddUserSupplyQuery(addUserSupplyQuery);
        log.info("查询结果：{}", integer);
    }

    @Test
    public void getUserInfoSupplyDetailVOByUserId() {
        UserInfoSupplyDetailVO userInfoSupplyDetailVOByUserId = userInfoSupplyService.getUserInfoSupplyDetailVOByUserId(27);
        log.info("查询结果：{}", userInfoSupplyDetailVOByUserId.toString());
    }

    @Test
    public void testAddWeixinUserInfoSupply() {
    }

    @Test
    public void deleteByPkId() {
    }

    @Test
    public void updateSelectiveByPkId() {
    }

    @Test
    public void selectByPkId() {
    }

    @Test
    public void selectByUserId() {
        WeixinUserInfoSupply weixinUserInfoSupply = userInfoSupplyService.selectByUserId(25);
        log.info("查找结果：{}", weixinUserInfoSupply.toString());
    }
}