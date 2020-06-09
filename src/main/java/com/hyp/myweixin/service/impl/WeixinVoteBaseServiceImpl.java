package com.hyp.myweixin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.mapper.WeixinVoteBaseMapper;
import com.hyp.myweixin.pojo.modal.WeixinVoteBase;
import com.hyp.myweixin.pojo.vo.page.IndexWorksVO;
import com.hyp.myweixin.service.WeixinVoteBaseService;
import com.hyp.myweixin.service.WeixinVoteWorkService;
import com.hyp.myweixin.utils.MyEntityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/7 20:11
 * @Description: TODO
 */
@Service
@Slf4j
public class WeixinVoteBaseServiceImpl implements WeixinVoteBaseService {

    @Autowired
    private WeixinVoteBaseMapper weixinVoteBaseMapper;
    @Autowired
    private WeixinVoteWorkService weixinVoteWorkService;

    /**
     * 分页查询投票活动列表
     *
     * @param weixinVoteBase
     * @param pageInfo
     * @return
     */
    @Override
    public PageInfo getVoteWorkByPage(WeixinVoteBase weixinVoteBase, PageInfo pageInfo) {

        /*条件查询*/
        Example example = new Example(WeixinVoteBase.class);
        Example.Criteria criteria = example.createCriteria();
        example.orderBy("createTime").desc();

        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
        //TODO　weixinVoteBase用于条件查询
        List<WeixinVoteBase> weixinVoteBases = weixinVoteBaseMapper.selectByExample(example);
        // 如果这里需要返回VO，那么这里一定先把查询值放进去，让分页信息存储成功。然后再setList加入VO信息
        pageInfo = new PageInfo(weixinVoteBases);
        /*组装VO进行数据返回*/
        List<IndexWorksVO> indexWorksVOS = new ArrayList<>(5);
        for (WeixinVoteBase weixinVoteBase2 : weixinVoteBases) {
            Integer countWorkByVoteBaseId = weixinVoteWorkService.getCountWorkByVoteBaseId(weixinVoteBase2.getId());
            Integer countVoteByVoteBaseId = weixinVoteWorkService.getCountVoteByVoteBaseId(weixinVoteBase2.getId());
            if (countVoteByVoteBaseId == null) {
                countVoteByVoteBaseId = 0;
            }
            //IndexWorksVO indexWorksVO = new IndexWorksVO();
            //BeanUtils.copyProperties(weixinVoteBase2, indexWorksVO);
            // 使用实体转换类进行数据转换处理
            IndexWorksVO indexWorksVO = MyEntityUtil.entity2VM(weixinVoteBase2, IndexWorksVO.class);
            indexWorksVO.setVoteWorkVoteCount(countVoteByVoteBaseId);
            indexWorksVO.setVoteWorkJoinCount(countWorkByVoteBaseId);
            indexWorksVOS.add(indexWorksVO);
        }
        pageInfo.setList(indexWorksVOS);


        return pageInfo;
    }
}
