package com.hyp.myweixin.service.impl;

import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.modal.WeixinVoteBase;
import com.hyp.myweixin.pojo.modal.WeixinVoteConf;
import com.hyp.myweixin.pojo.modal.WeixinVoteOrganisers;
import com.hyp.myweixin.pojo.modal.WeixinVoteWork;
import com.hyp.myweixin.pojo.query.voteuserwork.ActiveUserWorkQuery;
import com.hyp.myweixin.pojo.vo.page.PageEditWorkDetailVO;
import com.hyp.myweixin.pojo.vo.page.VoteDetailByWorkIdVO;
import com.hyp.myweixin.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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

    @Autowired
    private WeixinVoteWorkService weixinVoteWorkService;

    @Autowired
    private WeixinVoteConfService weixinVoteConfService;

    @Autowired
    private WeixinVoteOrganisersService weixinVoteOrganisersService;

    @Autowired
    private AdministratorsOptionService administratorsOptionService;

    /**
     * 创建活动
     * 要求用户为超级管理员/管理员
     *
     * @param activeId 活动ID
     * @param userId   用户ID
     * @return 新活动ID
     * @throws MyDefinitionException
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer copyActiveByActiveAndUserId(Integer activeId, Integer userId) throws MyDefinitionException {
        if (activeId == null || userId == null) {
            throw new MyDefinitionException("必须指定活动ID且用户为登录状态");
        }
        WeixinVoteBase weixinVoteBase = weixinVoteBaseService.getWeixinVoteBaseByWorkId(activeId);
        if (weixinVoteBase == null) {
            throw new MyDefinitionException("没有找到指定的活动信息");
        }

        /*判断是否为超级管理员 如果是就不做任何判断*/
        if (!administratorsOptionService.isSuperAdministrators(userId)) {
            if (!weixinVoteBase.getCreateSysUserId().equals(userId)) {
                throw new MyDefinitionException("您不是该活动的管理员");
            }
        }

        weixinVoteBase.setCreateTime(new Date());
        weixinVoteBase.setUpdateTime(new Date());
        weixinVoteBase.setVoteCountNum(0);
        weixinVoteBase.setViewCountNum(0);
        weixinVoteBase.setStatus(WeixinVoteBase.ActiveStatusEnum.UN_RELEASE.getCode());
        weixinVoteBase.setId(null);
        Integer integer = weixinVoteBaseService.saveVoteBase(weixinVoteBase);
        if (integer > 0) {
            WeixinVoteConf weixinVoteConf = weixinVoteConfService.getWeixinVoteConfByVoteWorkId(activeId);
            if (weixinVoteBase != null) {
                weixinVoteConf.setUpdateTime(new Date());
                weixinVoteConf.setActiveVoteBaseId(integer);
                weixinVoteConf.setCreateTime(new Date());
                weixinVoteConf.setId(null);
                weixinVoteConfService.saveWeixinVoteConf(weixinVoteConf);
            }
            WeixinVoteOrganisers weixinVoteOrganisers = weixinVoteOrganisersService.getWeixinVoteConfByVoteWorkId(activeId);
            if (weixinVoteOrganisers != null) {
                weixinVoteOrganisers.setVoteBaseId(integer);
                weixinVoteOrganisers.setId(null);
                weixinVoteOrganisersService.saveWeixinVoteOrganisers(weixinVoteOrganisers);
            }
        }
        return integer;
    }

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
            WeixinVoteBase weixinVoteBaseByWorkId = weixinVoteBaseService.getWeixinVoteBaseByWorkId(workId);
            pageEditWorkDetailVO = new PageEditWorkDetailVO();
            pageEditWorkDetailVO.setActiveCreateTime(weixinVoteBaseByWorkId.getCreateTime());
            pageEditWorkDetailVO.setActiveEndTime(voteWorkByWorkId.getActiveEndTime());
            pageEditWorkDetailVO.setActiveId(workId);
            pageEditWorkDetailVO.setActiveImg(voteWorkByWorkId.getActiveImg().replace(";", ""));
            pageEditWorkDetailVO.setActiveName(voteWorkByWorkId.getActiveName());
            pageEditWorkDetailVO.setActiveViewCount(voteWorkByWorkId.getActiveViewCount());
            pageEditWorkDetailVO.setActiveJoinCount(voteWorkByWorkId.getActiveJoinCount());

            long total = 0;
            try {
                ActiveUserWorkQuery activeUserWorkQuery = new ActiveUserWorkQuery();
                activeUserWorkQuery.setActiveId(workId);
                activeUserWorkQuery.setWorkStatus(WeixinVoteWork.VoteWorkStatusEnum.UN_REVIEW.getCode());
                activeUserWorkQuery.setUserId(voteWorkByWorkId.getSystemUserId());
                activeUserWorkQuery.setPageNum(1);
                activeUserWorkQuery.setPageSize(1);

                PageInfo<WeixinVoteWork> userWorkListByTypePage = weixinVoteWorkService.getUserWorkListByTypePage(activeUserWorkQuery);
                total = userWorkListByTypePage.getTotal();
            } catch (MyDefinitionException e) {

            }
            pageEditWorkDetailVO.setActiveUnREVIEWNum((int) total);
        }
        return pageEditWorkDetailVO;
    }
}
