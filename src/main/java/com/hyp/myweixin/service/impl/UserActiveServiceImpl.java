package com.hyp.myweixin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.mapper.WeixinVoteBaseMapper;
import com.hyp.myweixin.pojo.modal.WeixinVoteBase;
import com.hyp.myweixin.pojo.modal.WeixinVoteUser;
import com.hyp.myweixin.pojo.modal.WeixinVoteUserWork;
import com.hyp.myweixin.pojo.modal.WeixinVoteWork;
import com.hyp.myweixin.pojo.query.voteactive.OwnerActiveQuery;
import com.hyp.myweixin.pojo.vo.page.activeeditor.ActiveWorkForOwnerVO;
import com.hyp.myweixin.pojo.vo.page.activeeditor.ActiveWorkOverviewForOwnerVO;
import com.hyp.myweixin.service.*;
import com.hyp.myweixin.utils.MyEntityUtil;
import com.hyp.myweixin.utils.MyEnumUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/26 20:20
 * @Description: TODO
 */
@Slf4j
@Service
public class UserActiveServiceImpl implements UserActiveService {


    @Autowired
    private WeixinVoteBaseMapper weixinVoteBaseMapper;
    @Autowired
    private WeixinVoteBaseService weixinVoteBaseService;
    @Autowired
    private WeixinVoteWorkService weixinVoteWorkService;

    @Autowired
    private WeixinVoteUserService weixinVoteUserService;
    @Autowired
    private WeixinVoteUserWorkService weixinVoteUserWorkService;
    @Autowired
    private AdministratorsOptionService administratorsOptionService;


    /**
     * 获取用户活动的大致信息
     *
     * @param userId 用户ID
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public ActiveWorkOverviewForOwnerVO getActiveWorkOverviewForOwnerVOByUserId(Integer userId) throws MyDefinitionException {

        if (userId == null) {
            throw new MyDefinitionException("当前查询操作要求用户必须为登录状态");
        }

        WeixinVoteUser weixinVoteUser = weixinVoteUserService.getUserById(userId);
        if (weixinVoteUser == null) {
            throw new MyDefinitionException("没有知道到当前用户信息");
        }
        ActiveWorkOverviewForOwnerVO activeWorkOverviewForOwnerVO = ActiveWorkOverviewForOwnerVO.init();
        activeWorkOverviewForOwnerVO.setUserId(weixinVoteUser.getId());
        activeWorkOverviewForOwnerVO.setNickName(weixinVoteUser.getNickName());
        activeWorkOverviewForOwnerVO.setAvatarUrl(weixinVoteUser.getAvatarUrl());

        /*查询创建了多少活动了*/
        OwnerActiveQuery ownerActiveQuery = new OwnerActiveQuery();
        ownerActiveQuery.setUserId(userId);
        ownerActiveQuery.setPageSize(1);
        ownerActiveQuery.setPageNum(1);
        ownerActiveQuery.setActiveStatus(null);
        PageInfo<ActiveWorkForOwnerVO> activeWorkForOwnerVOListByUserId = getActiveWorkForOwnerVOListByUserId(ownerActiveQuery);
        if (activeWorkForOwnerVOListByUserId != null) {
            activeWorkOverviewForOwnerVO.setLaunchActiveNum((int) activeWorkForOwnerVOListByUserId.getTotal());
        }

        /*查询创建了提交了多少作品*/
        try {
            List<WeixinVoteWork> weixinVoteWorkListByUserId = weixinVoteWorkService.getWeixinVoteWorkListByUserId(userId);
            if (weixinVoteWorkListByUserId != null) {
                activeWorkOverviewForOwnerVO.setJoinUserWorkNum(weixinVoteWorkListByUserId.size());
            }
        } catch (MyDefinitionException e) {
            //
        }


        /*查询点赞了多少的作品*/
        try {
            List<WeixinVoteUserWork> userJoinActiveWorkList = weixinVoteUserWorkService.getUserJoinActiveWorkList(userId, true);
            if (userJoinActiveWorkList != null) {
                activeWorkOverviewForOwnerVO.setVoteUserWorkNum(userJoinActiveWorkList.size());
            }
        } catch (MyDefinitionException e) {
            //
        }

        return activeWorkOverviewForOwnerVO;
    }

    /**
     * 查询用户名下所有的活动
     * 分页查询
     *
     * @param ownerActiveQuery
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public PageInfo<ActiveWorkForOwnerVO> getActiveWorkForOwnerVOListByUserId(OwnerActiveQuery ownerActiveQuery) throws MyDefinitionException {

        WeixinVoteUser userById = weixinVoteUserService.getUserById(ownerActiveQuery.getUserId());
        if (userById == null) {
            throw new MyDefinitionException("没有知道到当前用户信息");
        }

        /*条件查询*/
        Example example = new Example(WeixinVoteBase.class);
        Example.Criteria criteria = example.createCriteria();
        if (ownerActiveQuery != null) {
            if (ownerActiveQuery.getActiveStatus() != null && ownerActiveQuery.getActiveStatus() >= 0) {
                criteria.andEqualTo("status", ownerActiveQuery.getActiveStatus());
            }
            if (!administratorsOptionService.isSuperAdministrators(ownerActiveQuery.getUserId())) {
                if (ownerActiveQuery.getUserId() != null) {
                    criteria.andEqualTo("createSysUserId", ownerActiveQuery.getUserId());
                }
            }
        }
        example.orderBy("activeShowOrder").desc();
        example.orderBy("voteCountNum").desc();
        example.orderBy("activeStartTime").desc();
        PageHelper.startPage(ownerActiveQuery.getPageNum(), ownerActiveQuery.getPageSize());
        List<WeixinVoteBase> weixinVoteBases = null;
        try {
            weixinVoteBases = weixinVoteBaseMapper.selectByExample(example);
        } catch (Exception e) {
            log.error("查询用户名下所有的活动操作过程失败,失败原因:{}", e.toString());
            throw new MyDefinitionException("查询用户名下所有的活动操作过程失败");
        }

        PageInfo pageInfoWeixinVoteBase = new PageInfo(weixinVoteBases);
        /*组装VO进行数据返回*/
        List<ActiveWorkForOwnerVO> activeWorkForOwnerVOList = new ArrayList<>(11);
        for (WeixinVoteBase weixinVoteBase : weixinVoteBases) {
            ActiveWorkForOwnerVO activeWorkForOwnerVO = MyEntityUtil.entity2VM(weixinVoteBase, ActiveWorkForOwnerVO.class);

            try {
                WeixinVoteBase.ActiveStatusEnum activeStatusDesc = MyEnumUtil.getByIntegerTypeCode(WeixinVoteBase.ActiveStatusEnum.class, "getCode", weixinVoteBase.getStatus());
                activeWorkForOwnerVO.setActiveStatus(activeStatusDesc.getMsg());
                WeixinVoteBase.ActivePublicEnum activePublicEnum = MyEnumUtil.getByIntegerTypeCode(WeixinVoteBase.ActivePublicEnum.class, "getCode", weixinVoteBase.getActivePublic());
                activeWorkForOwnerVO.setActiveIsPublic(activePublicEnum.getMsg());
            } catch (MyDefinitionException e) {
                activeWorkForOwnerVO.setActiveStatus("未能查询");
                activeWorkForOwnerVO.setActiveIsPublic("未能查询");
            }

            Integer countWorkByVoteBaseId = weixinVoteWorkService.getCountWorkByVoteBaseId(weixinVoteBase.getId());
            activeWorkForOwnerVO.setVoteWorkNum(countWorkByVoteBaseId);
            activeWorkForOwnerVO.setActiveId(weixinVoteBase.getId());
            activeWorkForOwnerVOList.add(activeWorkForOwnerVO);
        }
        pageInfoWeixinVoteBase.setList(activeWorkForOwnerVOList);
        return pageInfoWeixinVoteBase;
    }
}
