package com.hyp.myweixin.service.qubaoming.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.model.*;
import com.hyp.myweixin.pojo.qubaoming.query.pcenter.UserEnrollQuery;
import com.hyp.myweixin.pojo.qubaoming.vo.pcenter.PCenterActiveVO;
import com.hyp.myweixin.service.qubaoming.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/31 14:19
 * @Description: TODO
 */
@Slf4j
@Service
public class PersonCenterServiceImpl implements PersonCenterService {

    @Autowired
    private QubaomingActiveUserCollectionService qubaomingActiveUserCollectionService;
    @Autowired
    private QubaomingActiveBaseService qubaomingActiveBaseService;

    @Autowired
    private QuBaoMingActiveConfigService quBaoMingActiveConfigService;
    @Autowired
    private WechatCompanyService wechatCompanyService;

    @Autowired
    private QubaomingUserSignUpService qubaomingUserSignUpService;

    /**
     * 分页查询用户报名列表
     *
     * @param userEnrollQuery 查询条件
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public PageInfo getUserSignUpPageInfo(UserEnrollQuery userEnrollQuery) throws MyDefinitionException {

        if (userEnrollQuery == null) {
            throw new MyDefinitionException("参数不能为空");
        }

        Example example = new Example(QubaomingUserSignUp.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userEnrollQuery.getUserId());
        example.orderBy("createTime").desc();
        PageHelper.startPage(userEnrollQuery.getPageNum(), userEnrollQuery.getPageSize());

        List<QubaomingUserSignUp> qubaomingUserSignUpList = null;
        try {
            qubaomingUserSignUpList = qubaomingUserSignUpService.selectQubaomingUserSignUpByExample(example);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        PageInfo pageInfo = null;
        if (qubaomingUserSignUpList!=null) {

            pageInfo = new PageInfo(qubaomingUserSignUpList);
            List<PCenterActiveVO> pCenterActiveVOS = null;
            try {
                pCenterActiveVOS = qubaomingUserSignUpListToPCenterActiveVOList(qubaomingUserSignUpList);
            } catch (Exception e) {
                throw new MyDefinitionException(e.getMessage());
            }
            pageInfo.setList(pCenterActiveVOS);
        }
        return pageInfo;
    }


    /**
     * 数据转换
     *
     * @param qubaomingUserSignUpList
     * @return
     */
    private List<PCenterActiveVO> qubaomingUserSignUpListToPCenterActiveVOList(List<QubaomingUserSignUp> qubaomingUserSignUpList) throws MyDefinitionException {
        List<PCenterActiveVO> qubaomingActiveBaseList = new ArrayList<>();
        for (QubaomingUserSignUp qubaomingUserSignUp : qubaomingUserSignUpList) {
            PCenterActiveVO pCenterActiveVO = new PCenterActiveVO();
            QubaomingActiveBase qubaomingActiveBase = null;
            try {
                qubaomingActiveBase = qubaomingActiveBaseService.selectByPkId(qubaomingUserSignUp.getActiveId());
            } catch (MyDefinitionException e) {
                throw new MyDefinitionException(e.getMessage());
            }
            if (qubaomingActiveBase == null) {
                throw new MyDefinitionException("没有找到指定的活动信息");
            }
            pCenterActiveVO.setActiveId(qubaomingActiveBase.getId());
            pCenterActiveVO.setActiveName(qubaomingActiveBase.getActiveName());
            QubaomingActiveConfig qubaomingActiveConfig = quBaoMingActiveConfigService.selectOneByActiveId(qubaomingActiveBase.getId());
            if (qubaomingActiveConfig != null) {
                pCenterActiveVO.setActiveAddress(qubaomingActiveConfig.getActiveAddress());
                pCenterActiveVO.setActiveStartTime(qubaomingActiveConfig.getActiveStartTime());
                pCenterActiveVO.setActiveEndTime(qubaomingActiveConfig.getActiveEndTime());
            }

            WechatCompany wechatCompany = wechatCompanyService.selectOneByUserId(qubaomingActiveBase.getActiveUserId());
            if (wechatCompany != null) {
                pCenterActiveVO.setActiveCompanyName(wechatCompany.getCompanyName());
            }
            qubaomingActiveBaseList.add(pCenterActiveVO);
        }

        return qubaomingActiveBaseList;
    }

    /**
     * 分页查询用户收藏列表
     *
     * @param userEnrollQuery 查询条件
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public PageInfo getUserEnrollPageInfo(UserEnrollQuery userEnrollQuery) throws MyDefinitionException {

        if (userEnrollQuery == null) {
            throw new MyDefinitionException("参数不能为空");
        }

        Example example = new Example(QubaomingActiveUserCollection.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userEnrollQuery.getUserId());
        example.orderBy("createTime").desc();
        PageHelper.startPage(userEnrollQuery.getPageNum(), userEnrollQuery.getPageSize());
        PageInfo pageInfo = null;
        List<QubaomingActiveUserCollection> qubaomingActiveUserCollectionList = null;
        try {
            qubaomingActiveUserCollectionList = qubaomingActiveUserCollectionService.selectQubaomingActiveUserCollectionByExample(example);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        if (qubaomingActiveUserCollectionList != null) {
            pageInfo = new PageInfo(qubaomingActiveUserCollectionList);
            List<PCenterActiveVO> pCenterActiveVOS = null;
            try {
                pCenterActiveVOS = qubaomingActiveUserCollectionListToPCenterActiveVOList(qubaomingActiveUserCollectionList);
            } catch (Exception e) {
                throw new MyDefinitionException(e.getMessage());
            }
            pageInfo.setList(pCenterActiveVOS);
        }

        return pageInfo;
    }


    /**
     * 数据转换
     *
     * @param qubaomingActiveUserCollectionList
     * @return
     */
    private List<PCenterActiveVO> qubaomingActiveUserCollectionListToPCenterActiveVOList(List<QubaomingActiveUserCollection> qubaomingActiveUserCollectionList) throws MyDefinitionException {
        List<PCenterActiveVO> qubaomingActiveBaseList = new ArrayList<>();
        for (QubaomingActiveUserCollection qubaomingActiveUserCollection : qubaomingActiveUserCollectionList) {
            PCenterActiveVO pCenterActiveVO = new PCenterActiveVO();
            QubaomingActiveBase qubaomingActiveBase = null;
            try {
                qubaomingActiveBase = qubaomingActiveBaseService.selectByPkId(qubaomingActiveUserCollection.getActiveId());
            } catch (MyDefinitionException e) {
                throw new MyDefinitionException(e.getMessage());
            }
            if (qubaomingActiveBase == null) {
                throw new MyDefinitionException("没有找到指定的活动信息");
            }
            pCenterActiveVO.setActiveId(qubaomingActiveBase.getId());
            pCenterActiveVO.setActiveName(qubaomingActiveBase.getActiveName());
            QubaomingActiveConfig qubaomingActiveConfig = quBaoMingActiveConfigService.selectOneByActiveId(qubaomingActiveBase.getId());
            if (qubaomingActiveConfig != null) {
                pCenterActiveVO.setActiveAddress(qubaomingActiveConfig.getActiveAddress());
                pCenterActiveVO.setActiveStartTime(qubaomingActiveConfig.getActiveStartTime());
                pCenterActiveVO.setActiveEndTime(qubaomingActiveConfig.getActiveEndTime());
            }

            WechatCompany wechatCompany = wechatCompanyService.selectOneByUserId(qubaomingActiveBase.getActiveUserId());
            if (wechatCompany != null) {
                pCenterActiveVO.setActiveCompanyName(wechatCompany.getCompanyName());
            }
            qubaomingActiveBaseList.add(pCenterActiveVO);
        }

        return qubaomingActiveBaseList;
    }
}
