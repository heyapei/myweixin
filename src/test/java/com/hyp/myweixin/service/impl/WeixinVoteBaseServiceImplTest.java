package com.hyp.myweixin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.mapper.WeixinVoteBaseMapper;
import com.hyp.myweixin.pojo.modal.WeixinVoteBase;
import com.hyp.myweixin.pojo.modal.WeixinVoteWork;
import com.hyp.myweixin.pojo.vo.page.ActiveWorkRankVO;
import com.hyp.myweixin.pojo.vo.page.IndexWorksVO;
import com.hyp.myweixin.pojo.vo.page.VoteDetailByWorkIdVO;
import com.hyp.myweixin.service.WeixinVoteBaseService;
import com.hyp.myweixin.service.WeixinVoteWorkService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
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
    @Autowired
    private WeixinVoteWorkService weixinVoteWorkService;

    @Test
    public void testGetActiveWorkRank() {

        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageNum(1);
        pageInfo.setPageSize(5);
        WeixinVoteWork weixinVoteWork = new WeixinVoteWork();
        weixinVoteWork.setId(1);
        weixinVoteWork.setVoteWorkShowOrder(-1);
        log.info("查询数据：{}",weixinVoteWork.toString());

        ActiveWorkRankVO activeWorkRank = weixinVoteBaseService.getActiveWorkRank(1, pageInfo);
        System.out.println("查询出来的数据" + activeWorkRank.toString());

    }
    @Test
    public void getVoteWorkByWorkId() {


        VoteDetailByWorkIdVO voteWorkByWorkId = weixinVoteBaseService.getVoteWorkByWorkId(1);
        System.out.println("查询出来的数据" + voteWorkByWorkId.toString());

    }

    @Test
    public void updateVoteBaseViewNum() {


        weixinVoteBaseService.updateVoteBaseViewNum(2);

    }


    @Test
    public void getVoteWorkByPage() {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setPageNum(2);
        pageInfo.setPageSize(2);
        PageInfo voteWorkByPage = weixinVoteBaseService.getVoteWorkByPage(null, pageInfo);
        //log.info("分页查询出来的数据：" + voteWorkByPage.toString());

        List<WeixinVoteBase> list = voteWorkByPage.getList();
        List<IndexWorksVO> indexWorksVOS = new ArrayList<>(5);
        for (WeixinVoteBase weixinVoteBase : list) {
            Integer countWorkByVoteBaseId = weixinVoteWorkService.getCountWorkByVoteBaseId(weixinVoteBase.getId());
            Integer countVoteByVoteBaseId = weixinVoteWorkService.getCountVoteByVoteBaseId(weixinVoteBase.getId());
            IndexWorksVO indexWorksVO = new IndexWorksVO();
            BeanUtils.copyProperties(weixinVoteBase, indexWorksVO);
            indexWorksVO.setVoteWorkVoteCount(countVoteByVoteBaseId);
            indexWorksVO.setVoteWorkJoinCount(countWorkByVoteBaseId);
            System.out.println("输出1：" + weixinVoteBase.toString());
            System.out.println("输出2：" + indexWorksVO.toString());
            indexWorksVOS.add(indexWorksVO);
        }
        voteWorkByPage.setList(indexWorksVOS);
        log.info("pageINfo数据：" + voteWorkByPage.toString());
        log.info("实体类数据" + voteWorkByPage.getList().toString());

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