package com.hyp.myweixin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.mapper.WeixinVoteUserWorkMapper;
import com.hyp.myweixin.mapper.WeixinVoteWorkMapper;
import com.hyp.myweixin.pojo.modal.WeixinVoteUser;
import com.hyp.myweixin.pojo.modal.WeixinVoteUserWork;
import com.hyp.myweixin.pojo.modal.WeixinVoteWork;
import com.hyp.myweixin.pojo.vo.page.WeixinVoteUserWorkSimpleVO;
import com.hyp.myweixin.service.WeixinVoteBaseService;
import com.hyp.myweixin.service.WeixinVoteUserService;
import com.hyp.myweixin.service.WeixinVoteUserWorkService;
import com.hyp.myweixin.service.WeixinVoteWorkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/14 19:06
 * @Description: TODO
 */
@Service
@Slf4j
public class WeixinVoteUserWorkServiceImpl implements WeixinVoteUserWorkService {

    @Autowired
    private WeixinVoteUserWorkMapper weixinVoteUserWorkMapper;
    @Autowired
    private WeixinVoteBaseService weixinVoteBaseService;
    @Autowired
    private WeixinVoteWorkService weixinVoteWorkService;
    @Autowired
    private WeixinVoteUserService weixinVoteUserService;

    /**
     * 保存用户对某个作品的投票记录
     *
     * @param weixinVoteUserWork
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addUserVote(WeixinVoteUserWork weixinVoteUserWork) {
        if (weixinVoteUserWork == null) {
            throw new MyDefinitionException(500, "服务类发现参数错误");
        }
        Integer workId = weixinVoteUserWork.getWorkId();
        /**
         * 先查询是不是有这个作品
         */
        WeixinVoteWork voteWorkByUserWorkId = weixinVoteWorkService.getVoteWorkByUserWorkId(workId);
        if (voteWorkByUserWorkId == null) {
            log.error("未能通过workId查询到作品，查询的workId：{}", workId);
            throw new MyDefinitionException(404, "未能通过workId查询到作品");
        }

        /**
         * 更新活动的投票数
         */
        int i = weixinVoteBaseService.updateVoteBaseVoteNum(voteWorkByUserWorkId.getActiveVoteBaseId());
        if (i <= 0) {
            log.error("更新活动的投票数量错误，查询的workId：{}", voteWorkByUserWorkId.getActiveVoteBaseId());
            throw new MyDefinitionException(404, "未能通过活动ID查询到活动的数据，查询的活动ID：" + voteWorkByUserWorkId.getActiveVoteBaseId());
        }
        /**
         * 添加作品的被投票数量
         */
        int i1 = weixinVoteWorkService.updateVoteWorkVoteNum(workId);
        if (i1 <= 0) {
            log.error("更新作品的投票数量错误，查询的userWorkId：{}", workId);
            throw new MyDefinitionException(404, "未能通过作品ID查询到用户作品的数据，查询的作品ID：" + workId);
        }

        /**
         * 添加投票记录
         */
        try {
            weixinVoteUserWorkMapper.insertUseGeneratedKeys(weixinVoteUserWork);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("保存用户投票信息错误，错误原因：{}", e.toString());
        }

        return weixinVoteUserWork.getId();
    }

    /**
     * 保存用户对某个作品的投票记录
     *
     * @param workId
     * @return
     */
    @Override
    public List<WeixinVoteUserWork> getWeixinVoteUserWorkByWorkId(Integer workId) {

        Example example = new Example(WeixinVoteUserWork.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workId", workId);
        List<WeixinVoteUserWork> weixinVoteUserWorks = weixinVoteUserWorkMapper.selectByExample(example);
        return weixinVoteUserWorks;
    }

    /**
     * 保存用户对某个作品的投票记录
     *
     * @param weixinVoteUserWork
     * @param pageInfo
     * @return
     */
    @Override
    public PageInfo getWeixinVoteUserWorkByPage(WeixinVoteUserWork weixinVoteUserWork, PageInfo pageInfo) {


        Example example = new Example(WeixinVoteUserWork.class);
        example.orderBy("createTime").desc();
        Example.Criteria criteria = example.createCriteria();
        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
        //TODO　weixinVoteWork用于条件查询
        if (weixinVoteUserWork != null) {
            criteria.andEqualTo("workId", weixinVoteUserWork.getWorkId());
        }
        List<WeixinVoteUserWork> weixinVoteWorks = weixinVoteUserWorkMapper.selectByExample(example);
        // 如果这里需要返回VO，那么这里一定先把查询值放进去，让分页信息存储成功。然后再setList加入VO信息
        pageInfo = new PageInfo(weixinVoteWorks);

        return pageInfo;
    }

    /**
     * 获取投票人的头像信息
     *
     * @param pageInfo
     * @return
     */
    @Override
    public PageInfo getWeixinVoteUserWorkSimpleVOByPageInfo(PageInfo pageInfo) {

        List<WeixinVoteUserWork> list = pageInfo.getList();
        List<WeixinVoteUserWorkSimpleVO> list1 = new ArrayList<>();
        for (WeixinVoteUserWork voteUserWork : list) {
            String openId = voteUserWork.getOpenId();
            WeixinVoteUser userByOpenId = weixinVoteUserService.getUserByOpenId(openId);
            if (userByOpenId != null) {
                WeixinVoteUserWorkSimpleVO weixinVoteUserWorkSimpleVO = new WeixinVoteUserWorkSimpleVO();
                weixinVoteUserWorkSimpleVO.setAvatarUrl(voteUserWork.getAvatarUrl());
                list1.add(weixinVoteUserWorkSimpleVO);
            }
        }
        pageInfo.setList(list1);
        return pageInfo;
    }
}
