package com.hyp.myweixin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.mapper.WeixinVoteBaseMapper;
import com.hyp.myweixin.pojo.modal.WeixinVoteBase;
import com.hyp.myweixin.pojo.modal.WeixinVoteUser;
import com.hyp.myweixin.pojo.query.voteactive.OwnerActiveQuery;
import com.hyp.myweixin.pojo.vo.page.activeeditor.ActiveWorkForOwnerVO;
import com.hyp.myweixin.service.AdministratorsOptionService;
import com.hyp.myweixin.service.UserActiveService;
import com.hyp.myweixin.service.WeixinVoteUserService;
import com.hyp.myweixin.service.WeixinVoteWorkService;
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
    private WeixinVoteWorkService weixinVoteWorkService;

    @Autowired
    private WeixinVoteUserService weixinVoteUserService;
    @Autowired
    private AdministratorsOptionService administratorsOptionService;


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
