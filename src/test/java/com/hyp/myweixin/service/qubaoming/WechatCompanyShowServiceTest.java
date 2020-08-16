package com.hyp.myweixin.service.qubaoming;

import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.pojo.qubaoming.query.company.CompanyListShowQuery;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/16 16:42
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class WechatCompanyShowServiceTest {

    @Autowired
    private WechatCompanyShowService wechatCompanyShowService;

    @Test
    public void getCompanyListShowByCompanyListShowQuery() {

        CompanyListShowQuery companyListShowQuery = new CompanyListShowQuery();
        companyListShowQuery.setUserId(25);
        companyListShowQuery.setPageNum(1);
        companyListShowQuery.setPageSize(5);
        companyListShowQuery.setNowTime("");
        companyListShowQuery.setSign("");

        PageInfo<Object> companyListShowByCompanyListShowQuery = wechatCompanyShowService.getCompanyListShowByCompanyListShowQuery(companyListShowQuery);
        log.info("查询结果：{}", companyListShowByCompanyListShowQuery.toString());
    }
}