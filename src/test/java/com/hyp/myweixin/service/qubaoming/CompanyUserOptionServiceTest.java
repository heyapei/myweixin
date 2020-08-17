package com.hyp.myweixin.service.qubaoming;

import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.pojo.qubaoming.query.company.CompanyUserOptionQuery;
import com.hyp.myweixin.pojo.qubaoming.query.company.ShowUserCollectionPageQuery;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/17 22:19
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CompanyUserOptionServiceTest {

    @Autowired
    private CompanyUserOptionService companyUserOptionService;

    @Test
    public void addUserCollectionCompany() {
        CompanyUserOptionQuery companyUserOptionQuery = new CompanyUserOptionQuery();
        companyUserOptionQuery.setUserId(2);
        companyUserOptionQuery.setCompanyId(1);
        companyUserOptionQuery.setNowTime("");
        companyUserOptionQuery.setSign("");
        Integer integer = companyUserOptionService.addUserCollectionCompany(companyUserOptionQuery);
        log.info("查询结果：{}", integer);
    }

    @Test
    public void delUserCollectionCompany() {
        CompanyUserOptionQuery companyUserOptionQuery = new CompanyUserOptionQuery();
        companyUserOptionQuery.setUserId(2);
        companyUserOptionQuery.setCompanyId(1);
        companyUserOptionQuery.setNowTime("");
        companyUserOptionQuery.setSign("");
        Integer integer = companyUserOptionService.delUserCollectionCompany(companyUserOptionQuery);
        log.info("查询结果：{}", integer);
    }

    @Test
    public void showCollectionListByUserId() {
        ShowUserCollectionPageQuery showUserCollectionPageQuery = new ShowUserCollectionPageQuery();
        showUserCollectionPageQuery.setUserId(2);
        showUserCollectionPageQuery.setPageNum(1);
        showUserCollectionPageQuery.setPageSize(5);
        showUserCollectionPageQuery.setNowTime("");
        showUserCollectionPageQuery.setSign("");
        PageInfo pageInfo = companyUserOptionService.showCollectionListByUserId(showUserCollectionPageQuery);
        log.info("查询结果：{}",pageInfo);
    }
}