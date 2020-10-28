package com.hyp.myweixin.service.qubaoming;

import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

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

    @Test
    public void getSignUpUserInfoByActiveIdPage() {
        PageInfo<Object> signUpUserInfoByActiveIdPage = userEnrollActiveService.getSignUpUserInfoByActiveIdPage(7, 1, 20);
        List<Object> list = signUpUserInfoByActiveIdPage.getList();
        for (Object o : list) {
            System.out.println(o.toString());
        }

    }


}