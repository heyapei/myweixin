package com.hyp.myweixin.service.impl;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.vo.page.PageEditWorkDetailVO;
import com.hyp.myweixin.pojo.vo.page.VoteDetailByWorkIdVO;
import com.hyp.myweixin.service.VoteActiveDetailService;
import com.hyp.myweixin.service.WeixinVoteBaseService;
import com.hyp.myweixin.service.WeixinVoteUserWorkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/13 20:45
 * @Description: TODO
 */
@Slf4j
@Service
public class VoteActiveDetailServiceImpl implements VoteActiveDetailService {


    @Autowired
    private WeixinVoteBaseService weixinVoteBaseService;
    @Autowired
    private WeixinVoteUserWorkService weixinVoteUserWorkService;


    /**
     * 通过活动id获取活动编辑页的第一页内容
     *
     * @param workId 活动ID
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public PageEditWorkDetailVO getPageEditWorkDetailVOByWorkId(Integer workId) throws MyDefinitionException {

        PageEditWorkDetailVO pageEditWorkDetailVO = null;
        if (workId == null) {
            throw new MyDefinitionException("活动ID为空不可以进行查询操作");
        }

        VoteDetailByWorkIdVO voteWorkByWorkId = null;
        try {
            voteWorkByWorkId = weixinVoteBaseService.getVoteWorkByWorkId(workId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("通过活动id获取活动编辑页的第一页内容错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("通过活动id获取活动编辑页的第一页内容错误");
        }
        if (voteWorkByWorkId == null) {
            return null;
        } else {
            pageEditWorkDetailVO = new PageEditWorkDetailVO();
            pageEditWorkDetailVO.setActiveCreateTime(voteWorkByWorkId.getActiveStartTime());
            pageEditWorkDetailVO.setActiveEndTime(voteWorkByWorkId.getActiveEndTime());
            pageEditWorkDetailVO.setActiveId(workId);
            pageEditWorkDetailVO.setActiveImg(voteWorkByWorkId.getActiveImg().replace(";", ""));
            pageEditWorkDetailVO.setActiveName(voteWorkByWorkId.getActiveName());
            pageEditWorkDetailVO.setActiveViewCount(voteWorkByWorkId.getActiveViewCount());
            pageEditWorkDetailVO.setActiveJoinCount(voteWorkByWorkId.getActiveJoinCount());
        }
        return pageEditWorkDetailVO;
    }
}
