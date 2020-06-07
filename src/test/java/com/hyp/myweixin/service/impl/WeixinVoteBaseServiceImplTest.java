package com.hyp.myweixin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.mapper.WeixinVoteBaseMapper;
import com.hyp.myweixin.pojo.modal.WeixinVoteBase;
import com.hyp.myweixin.service.WeixinVoteBaseService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/7 20:24
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class WeixinVoteBaseServiceImplTest {

    @Autowired
    private WeixinVoteBaseService weixinVoteBaseService;

    @Test
    public void getVoteWorkByPage() {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageNum(1);
        pageInfo.setPageSize(2);
        PageInfo voteWorkByPage = weixinVoteBaseService.getVoteWorkByPage(null, pageInfo);
        //log.info("分页查询出来的数据：" + voteWorkByPage.toString());
        log.info("实体类数据" + voteWorkByPage.getList());

    }


    @Autowired
    private WeixinVoteBaseMapper weixinVoteBaseMapper;

    @Test
    public void getVoteWorkByPage1() {

        /*条件查询*/
        Example example = new Example(WeixinVoteBase.class);
        Example.Criteria criteria = example.createCriteria();
        example.orderBy("createTime").desc();

        PageHelper.startPage(1, 2);
        //TODO　weixinVoteBase用于条件查询
        List<WeixinVoteBase> weixinVoteBases = weixinVoteBaseMapper.selectByExample(example);
        //log.info("这里呢？" + weixinVoteBases.toString());

        //log.info("这里没有查询出来数据：" + weixinVoteBases.toString());
        PageInfo<WeixinVoteBase> pageInfo = new PageInfo(weixinVoteBases);
        log.info("这样查一下" + pageInfo.toString());
    }
}