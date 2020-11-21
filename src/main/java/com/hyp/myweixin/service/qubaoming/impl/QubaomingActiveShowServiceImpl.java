package com.hyp.myweixin.service.qubaoming.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.model.*;
import com.hyp.myweixin.pojo.qubaoming.query.active.ActiveShowByCompanyIdQuery;
import com.hyp.myweixin.pojo.qubaoming.query.active.ShowActiveByPageQuery;
import com.hyp.myweixin.pojo.qubaoming.vo.active.ActiveByShowActiveCompleteVO;
import com.hyp.myweixin.pojo.qubaoming.vo.active.ActiveDetailShowVO;
import com.hyp.myweixin.pojo.qubaoming.vo.active.ActiveShareImgVO;
import com.hyp.myweixin.pojo.qubaoming.vo.active.ActiveShowByCompanyIdVO;
import com.hyp.myweixin.service.qubaoming.*;
import com.hyp.myweixin.service.smallwechatapi.WeixinSmallContentDetectionApiService;
import com.hyp.myweixin.utils.MyEntityUtil;
import com.hyp.myweixin.utils.MySeparatorUtil;
import com.hyp.myweixin.utils.dateutil.MyDateStyle;
import com.hyp.myweixin.utils.dateutil.MyDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2020/8/18 14:27
 * @Description: TODO
 */
@Service
@Slf4j
public class QubaomingActiveShowServiceImpl implements QubaomingActiveShowService {


    @Autowired
    private QubaomingActiveBaseService qubaomingActiveBaseService;

    @Autowired
    private WechatCompanyService wechatCompanyService;

    @Autowired
    private QuBaoMingActiveConfigService qubaomingActiveConfigService;

    @Autowired
    private QubaomingActiveUserCollectionService qubaomingActiveUserCollectionService;

    @Autowired
    private QuBaoMingCompanyUserCollectionService quBaoMingCompanyUserCollectionService;


    @Autowired
    private QubaomingUserSignUpService qubaomingUserSignUpService;

    @Autowired
    private QuBaoMingActiveConfigService quBaoMingActiveConfigService;

    @Autowired
    private WeixinSmallContentDetectionApiService weixinSmallContentDetectionApiService;


    /**
     * 通过公司id查询活动列表
     *
     * @param activeShowByCompanyIdQuery
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public PageInfo<Object> getActiveListByCompanyId(ActiveShowByCompanyIdQuery activeShowByCompanyIdQuery) throws MyDefinitionException {

        if (activeShowByCompanyIdQuery == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        Example example = new Example(QubaomingActiveBase.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("activeCompanyId", activeShowByCompanyIdQuery.getWechatCompanyId());
        example.orderBy("createTime").desc();
        PageHelper.startPage(activeShowByCompanyIdQuery.getPageNum(), activeShowByCompanyIdQuery.getPageSize());
        PageInfo pageInfo = null;
        List<QubaomingActiveBase> qubaomingActiveBaseList = null;
        try {
            qubaomingActiveBaseList = qubaomingActiveBaseService.selectUserActiveByExample(example);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        if (qubaomingActiveBaseList != null) {
            pageInfo = new PageInfo(qubaomingActiveBaseList);
            List<ActiveShowByCompanyIdVO> activeShowByCompanyIdVOS = null;
            try {
                activeShowByCompanyIdVOS = qubaomingActiveUserCollectionListToActiveShowByCompanyIdVOList(qubaomingActiveBaseList);
            } catch (MyDefinitionException e) {
                throw new MyDefinitionException(e.getMessage());
            }
            pageInfo.setList(activeShowByCompanyIdVOS);
        }
        return pageInfo;
    }

    /**
     * 数据转换
     *
     * @param qubaomingActiveUserCollectionList
     * @return
     */
    private List<ActiveShowByCompanyIdVO> qubaomingActiveUserCollectionListToActiveShowByCompanyIdVOList(List<QubaomingActiveBase> qubaomingActiveUserCollectionList) throws MyDefinitionException {
        List<ActiveShowByCompanyIdVO> qubaomingActiveBaseList = new ArrayList<>();
        for (QubaomingActiveBase qubaomingActiveBase : qubaomingActiveUserCollectionList) {
            ActiveShowByCompanyIdVO activeShowByCompanyIdVO = new ActiveShowByCompanyIdVO();
            activeShowByCompanyIdVO.setActiveId(qubaomingActiveBase.getId());
            activeShowByCompanyIdVO.setActiveName(qubaomingActiveBase.getActiveName());
            activeShowByCompanyIdVO.setActiveImg(qubaomingActiveBase.getActiveImg().replaceAll(MySeparatorUtil.SEMICOLON_SEPARATOR, ""));
            QubaomingActiveConfig qubaomingActiveConfig = quBaoMingActiveConfigService.selectOneByActiveId(qubaomingActiveBase.getId());
            if (qubaomingActiveConfig != null) {
                activeShowByCompanyIdVO.setActiveAddress(qubaomingActiveConfig.getActiveAddress());
                activeShowByCompanyIdVO.setActiveStartTime(qubaomingActiveConfig.getActiveStartTime());
                activeShowByCompanyIdVO.setActiveEndTime(qubaomingActiveConfig.getActiveEndTime());
            }

            WechatCompany wechatCompany = wechatCompanyService.selectOneByUserId(qubaomingActiveBase.getActiveUserId());
            if (wechatCompany != null) {
                activeShowByCompanyIdVO.setActiveCompanyName(wechatCompany.getCompanyName());
            }
            qubaomingActiveBaseList.add(activeShowByCompanyIdVO);
        }

        return qubaomingActiveBaseList;
    }

    /**
     * 增加分享量
     *
     * @param activeId
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public Integer addActiveShareNumByActiveId(Integer activeId) throws MyDefinitionException {
        if (activeId == null) {
            throw new MyDefinitionException("参数不能为空");
        }

        QubaomingActiveBase qubaomingActiveBase = null;
        try {
            qubaomingActiveBase = qubaomingActiveBaseService.selectByPkId(activeId);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        if (qubaomingActiveBase == null) {
            throw new MyDefinitionException("没有找到指定的活动数据");
        } else {
            qubaomingActiveBase.setActiveShareNum(qubaomingActiveBase.getActiveShareNum() + 1);
            return qubaomingActiveBaseService.updateSelectiveQubaomingActiveBase(qubaomingActiveBase);
            ///////////////// return qubaomingActiveBase.getActiveShareImg().replaceAll(MySeparatorUtil.SEMICOLON_SEPARATOR, "");
        }
    }

    /**
     * 获取活动的分享图
     *
     * @param activeId
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public ActiveShareImgVO getActiveShareImgByActiveId(Integer activeId, String scene, String page) throws MyDefinitionException {
        if (activeId == null) {
            throw new MyDefinitionException("参数不能为空");
        }

        ActiveShareImgVO activeShareImgVO = new ActiveShareImgVO();

        QubaomingActiveBase qubaomingActiveBase = null;
        try {
            qubaomingActiveBase = qubaomingActiveBaseService.selectByPkId(activeId);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        if (qubaomingActiveBase == null) {
            throw new MyDefinitionException("没有找到指定的活动数据");
        } else {
            String activeImg = qubaomingActiveBase.getActiveShareImg().replaceAll(MySeparatorUtil.SEMICOLON_SEPARATOR, "");
            activeShareImgVO.setActiveImg(activeImg);
            activeShareImgVO.setActiveName(qubaomingActiveBase.getActiveName().replaceAll("\\n", ""));
            activeShareImgVO.setActiveDesc(qubaomingActiveBase.getActiveDesc().replaceAll("\\n", ""));

            log.info("获取分享二维码的参数：scene：{}，page:{}", scene, page);
            String quBaoMingQrCodeUnlimited = weixinSmallContentDetectionApiService.
                    getQuBaoMingQrCodeUnlimited(scene, page);
            log.info("二维码图片查询结果：{}", quBaoMingQrCodeUnlimited);
            activeShareImgVO.setActiveWechatImg(quBaoMingQrCodeUnlimited);
            QubaomingActiveConfig qubaomingActiveConfigs = qubaomingActiveConfigService.selectOneByActiveId(activeId);
            if (qubaomingActiveConfigs != null) {
                Date date = MyDateUtil.numberDateFormatToDate(qubaomingActiveConfigs.getActiveEndTime());
                activeShareImgVO.setActiveEndTime(MyDateUtil.DateToString(date, MyDateStyle.YYYY_MM_DD_HH_MM));
            }
        }
        return activeShareImgVO;
    }

    /**
     * 通过activeId查询具体的活动详情
     * userId用于判断报名人数
     *
     * @param userId
     * @param activeId
     * @return 视图
     * @throws MyDefinitionException
     */
    @Override
    public ActiveDetailShowVO getActiveShowDetailByActiveId(Integer userId, Integer activeId) throws MyDefinitionException {

        if (userId == null || activeId == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        ActiveDetailShowVO activeDetailShowVO = new ActiveDetailShowVO();
        QubaomingActiveBase qubaomingActiveBase = null;
        try {
            qubaomingActiveBase = qubaomingActiveBaseService.selectByPkId(activeId);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        if (qubaomingActiveBase == null) {
            throw new MyDefinitionException("没有找到指定的活动数据");
        } else {
            activeDetailShowVO.setQubaomingActiveBase(qubaomingActiveBase);
            activeDetailShowVO.setActiveImgList(qubaomingActiveBase.getActiveImg().split(MySeparatorUtil.SEMICOLON_SEPARATOR));
            activeDetailShowVO.setActiveDescImgList(qubaomingActiveBase.getActiveDescImg().split(MySeparatorUtil.SEMICOLON_SEPARATOR));
            activeDetailShowVO.setActiveDetailImgList(qubaomingActiveBase.getActiveDetailImg().split(MySeparatorUtil.SEMICOLON_SEPARATOR));
            activeDetailShowVO.setActiveShareImgList(qubaomingActiveBase.getActiveShareImg().split(MySeparatorUtil.SEMICOLON_SEPARATOR));
        }
        List<QubaomingActiveConfig> qubaomingActiveConfigs = null;
        try {
            qubaomingActiveConfigs = qubaomingActiveConfigService.selectByActiveId(activeId);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        if (qubaomingActiveConfigs != null && qubaomingActiveConfigs.size() > 0) {
            activeDetailShowVO.setQubaomingActiveConfig(qubaomingActiveConfigs.get(0));
        } else {
            throw new MyDefinitionException("没有找到相关活动配置项");
        }
        WechatCompany wechatCompany = null;
        try {
            wechatCompany = wechatCompanyService.selectByPkId(qubaomingActiveBase.getActiveCompanyId());
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        activeDetailShowVO.setWechatCompany(wechatCompany);
        if (wechatCompany != null) {
            activeDetailShowVO.setLogoImgList(wechatCompany.getLogoImg().split(MySeparatorUtil.SEMICOLON_SEPARATOR));
            activeDetailShowVO.setWeixinQrCodeList(wechatCompany.getWeixinQrCode().split(MySeparatorUtil.SEMICOLON_SEPARATOR));
        }

        Example example5 = new Example(QubaomingUserSignUp.class);
        Example.Criteria criteria5 = example5.createCriteria();
        criteria5.andEqualTo("activeId", activeId);
        criteria5.andEqualTo("userId", userId);

        try {
            QubaomingUserSignUp qubaomingUserSignUp = qubaomingUserSignUpService.selectOneQubaomingUserSignUpByExample(example5);
            if (qubaomingUserSignUp == null) {
                activeDetailShowVO.setHasSignUp(false);
            } else {
                activeDetailShowVO.setHasSignUp(true);
            }
        } catch (MyDefinitionException e) {
            activeDetailShowVO.setHasSignUp(false);
            throw new MyDefinitionException(e.getMessage());
        }


        Example example = new Example(QubaomingUserSignUp.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("activeId", activeId);
        List<QubaomingUserSignUp> qubaomingUserSignUpList = null;
        try {
            qubaomingUserSignUpList = qubaomingUserSignUpService.selectQubaomingUserSignUpByExample(example);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        if (qubaomingUserSignUpList != null) {
            activeDetailShowVO.setSignUpNum(qubaomingUserSignUpList.size());
        }


        Example example1 = new Example(QubaomingActiveUserCollection.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("userId", userId);
        criteria1.andEqualTo("activeId", activeId);
        QubaomingActiveUserCollection qubaomingActiveUserCollection = qubaomingActiveUserCollectionService.selectOneQubaomingActiveUserCollectionByExample(example1);
        if (qubaomingActiveUserCollection != null) {
            activeDetailShowVO.setHasCollectionActive(true);
        } else {
            activeDetailShowVO.setHasCollectionActive(false);
        }
        if (wechatCompany != null) {
            Example example2 = new Example(QuBaoMingCompanyUserCollection.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("userId", userId);
            criteria2.andEqualTo("companyId", wechatCompany.getId());
            QuBaoMingCompanyUserCollection quBaoMingCompanyUserCollection = quBaoMingCompanyUserCollectionService.selectOneByExample(example2);
            if (quBaoMingCompanyUserCollection != null) {
                activeDetailShowVO.setHasCollectionCompany(true);
            } else {
                activeDetailShowVO.setHasCollectionCompany(false);
            }
        } else {
            activeDetailShowVO.setHasCollectionCompany(false);
        }

        try {
            qubaomingActiveBase.setActiveViewNum(qubaomingActiveBase.getActiveViewNum() + 1);
            qubaomingActiveBaseService.updateSelectiveQubaomingActiveBase(qubaomingActiveBase);
        } catch (MyDefinitionException e) {
            //do nothing
        }


        return activeDetailShowVO;
    }

    /**
     * 分页查询热门活动信息
     *
     * @param showActiveByPageQuery
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public PageInfo<Object> getAllActiveByShowActiveByPageQuery(ShowActiveByPageQuery showActiveByPageQuery) throws MyDefinitionException {

        if (showActiveByPageQuery == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        Example example = new Example(QubaomingActiveBase.class);
        Example.Criteria criteria = example.createCriteria();
        //criteria.andEqualTo("activeStatus", QubaomingActiveBase.ActiveStatusEnum.ONLINE.getCode());
        criteria.andNotEqualTo("activeStatus", QubaomingActiveBase.ActiveStatusEnum.UN_COMPLETE.getCode());
        if (StringUtils.isNotBlank(showActiveByPageQuery.getActiveQuery())) {
            criteria.andLike("activeName", "%" + showActiveByPageQuery.getActiveQuery() + "%");
        }
        example.orderBy("activeShowOrder").desc();
        example.orderBy("createTime").desc();
        example.orderBy("activeJoinNum").desc();
        PageHelper.startPage(showActiveByPageQuery.getPageNum(), showActiveByPageQuery.getPageSize());
        PageInfo pageInfo = null;
        try {
            List<QubaomingActiveBase> qubaomingActiveBases = qubaomingActiveBaseService.selectUserActiveByExample(example);
            pageInfo = new PageInfo(qubaomingActiveBases);

            List<ActiveByShowActiveCompleteVO> activeByShowActiveCompleteVOList = new ArrayList<>();
            List<ActiveByShowActiveCompleteVO> activeByShowActiveCompleteVOS = MyEntityUtil.entity2VMList(qubaomingActiveBases, ActiveByShowActiveCompleteVO.class);
            for (ActiveByShowActiveCompleteVO activeByShowActiveCompleteVO : activeByShowActiveCompleteVOS) {
                WechatCompany wechatCompany = null;
                try {
                    wechatCompany = wechatCompanyService.selectByPkId(activeByShowActiveCompleteVO.getActiveCompanyId());
                } catch (MyDefinitionException e) {
                    //do nothing
                }

                if (wechatCompany != null) {
                    wechatCompany.setLogoImg(wechatCompany.getLogoImg().replaceAll(MySeparatorUtil.SEMICOLON_SEPARATOR, ""));
                    wechatCompany.setWeixinQrCode(wechatCompany.getWeixinQrCode().replaceAll(MySeparatorUtil.SEMICOLON_SEPARATOR, ""));
                }

                activeByShowActiveCompleteVO.setWechatCompany(wechatCompany);

                activeByShowActiveCompleteVO.setActiveImgList(activeByShowActiveCompleteVO.getActiveImg().split(MySeparatorUtil.SEMICOLON_SEPARATOR));
                activeByShowActiveCompleteVO.setActiveDescImgList(activeByShowActiveCompleteVO.getActiveDescImg().split(MySeparatorUtil.SEMICOLON_SEPARATOR));
                activeByShowActiveCompleteVO.setActiveDetailImgList(activeByShowActiveCompleteVO.getActiveDetailImg().split(MySeparatorUtil.SEMICOLON_SEPARATOR));

                List<QubaomingActiveConfig> qubaomingActiveConfigs = null;
                try {
                    qubaomingActiveConfigs = qubaomingActiveConfigService.selectByActiveId(activeByShowActiveCompleteVO.getId());
                } catch (MyDefinitionException e) {
                    throw new MyDefinitionException(e.getMessage());
                }
                if (qubaomingActiveConfigs != null && qubaomingActiveConfigs.size() > 0) {
                    activeByShowActiveCompleteVO.setQubaomingActiveConfig(qubaomingActiveConfigs.get(0));
                }

                /*等明天一起写吧*/
                Example example1 = new Example(QubaomingActiveUserCollection.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("userId", showActiveByPageQuery.getUserId());
                criteria1.andEqualTo("activeId", activeByShowActiveCompleteVO.getId());
                QubaomingActiveUserCollection qubaomingActiveUserCollection = qubaomingActiveUserCollectionService.selectOneQubaomingActiveUserCollectionByExample(example1);
                if (qubaomingActiveUserCollection != null) {
                    activeByShowActiveCompleteVO.setHasCollectionActive(true);
                } else {
                    activeByShowActiveCompleteVO.setHasCollectionActive(false);
                }
                if (wechatCompany != null) {
                    Example example2 = new Example(QuBaoMingCompanyUserCollection.class);
                    Example.Criteria criteria2 = example2.createCriteria();
                    criteria2.andEqualTo("userId", showActiveByPageQuery.getUserId());
                    criteria2.andEqualTo("companyId", wechatCompany.getId());
                    QuBaoMingCompanyUserCollection quBaoMingCompanyUserCollection = quBaoMingCompanyUserCollectionService.selectOneByExample(example2);
                    if (quBaoMingCompanyUserCollection != null) {
                        activeByShowActiveCompleteVO.setHasCollectionCompany(true);
                    } else {
                        activeByShowActiveCompleteVO.setHasCollectionCompany(false);
                    }
                } else {
                    activeByShowActiveCompleteVO.setHasCollectionCompany(false);
                }

                activeByShowActiveCompleteVOList.add(activeByShowActiveCompleteVO);

                //log.info("处理结果：{}",activeByShowActiveCompleteVOList.toString());
            }

            pageInfo.setList(activeByShowActiveCompleteVOS);

        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        return pageInfo;
    }

    /**
     * 分页查询热门活动信息
     *
     * @param showActiveByPageQuery
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public PageInfo<Object> getHotActiveByShowActiveByPageQuery(ShowActiveByPageQuery showActiveByPageQuery) throws MyDefinitionException {
        if (showActiveByPageQuery == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        Example example = new Example(QubaomingActiveBase.class);
        Example.Criteria criteria = example.createCriteria();
        /*近一个月内创建的项目*/
        criteria.andBetween("createTime", Long.parseLong(MyDateUtil.numberDateFormatToDate(MyDateUtil.addDay(new Date(), -30), 13)), System.currentTimeMillis());
        criteria.andNotEqualTo("activeStatus", QubaomingActiveBase.ActiveStatusEnum.UN_COMPLETE.getCode());
        //criteria.andEqualTo("activeStatus", QubaomingActiveBase.ActiveStatusEnum.ONLINE.getCode());
        example.orderBy("activeShowOrder").desc();
        example.orderBy("activeShareNum").desc();
        example.orderBy("activeCollectionNum").desc();
        example.orderBy("activeJoinNum").desc();
        example.orderBy("activeViewNum").desc();

        PageHelper.startPage(showActiveByPageQuery.getPageNum(), showActiveByPageQuery.getPageSize());
        PageInfo pageInfo = null;
        try {
            List<QubaomingActiveBase> qubaomingActiveBases = qubaomingActiveBaseService.selectUserActiveByExample(example);
            pageInfo = new PageInfo(qubaomingActiveBases);

            List<ActiveByShowActiveCompleteVO> activeByShowActiveCompleteVOList = new ArrayList<>();

            List<ActiveByShowActiveCompleteVO> activeByShowActiveCompleteVOS = MyEntityUtil.entity2VMList(qubaomingActiveBases, ActiveByShowActiveCompleteVO.class);
            for (ActiveByShowActiveCompleteVO activeByShowActiveCompleteVO : activeByShowActiveCompleteVOS) {
                WechatCompany wechatCompany = null;
                try {
                    wechatCompany = wechatCompanyService.selectByPkId(activeByShowActiveCompleteVO.getActiveCompanyId());
                } catch (MyDefinitionException e) {
                    //do nothing
                }

                if (wechatCompany != null) {
                    wechatCompany.setLogoImg(wechatCompany.getLogoImg().replaceAll(MySeparatorUtil.SEMICOLON_SEPARATOR, ""));
                    wechatCompany.setWeixinQrCode(wechatCompany.getWeixinQrCode().replaceAll(MySeparatorUtil.SEMICOLON_SEPARATOR, ""));
                }

                activeByShowActiveCompleteVO.setWechatCompany(wechatCompany);

                activeByShowActiveCompleteVO.setActiveImgList(activeByShowActiveCompleteVO.getActiveImg().split(MySeparatorUtil.SEMICOLON_SEPARATOR));
                activeByShowActiveCompleteVO.setActiveDescImgList(activeByShowActiveCompleteVO.getActiveDescImg().split(MySeparatorUtil.SEMICOLON_SEPARATOR));
                activeByShowActiveCompleteVO.setActiveDetailImgList(activeByShowActiveCompleteVO.getActiveDetailImg().split(MySeparatorUtil.SEMICOLON_SEPARATOR));


                List<QubaomingActiveConfig> qubaomingActiveConfigs = null;
                try {
                    qubaomingActiveConfigs = qubaomingActiveConfigService.selectByActiveId(activeByShowActiveCompleteVO.getId());
                } catch (MyDefinitionException e) {
                    throw new MyDefinitionException(e.getMessage());
                }
                if (qubaomingActiveConfigs != null && qubaomingActiveConfigs.size() > 0) {
                    activeByShowActiveCompleteVO.setQubaomingActiveConfig(qubaomingActiveConfigs.get(0));
                }


                Example example1 = new Example(QubaomingActiveUserCollection.class);
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("userId", showActiveByPageQuery.getUserId());
                criteria1.andEqualTo("activeId", activeByShowActiveCompleteVO.getId());
                QubaomingActiveUserCollection qubaomingActiveUserCollection = qubaomingActiveUserCollectionService.selectOneQubaomingActiveUserCollectionByExample(example1);
                if (qubaomingActiveUserCollection != null) {
                    activeByShowActiveCompleteVO.setHasCollectionActive(true);
                } else {
                    activeByShowActiveCompleteVO.setHasCollectionActive(false);
                }
                if (wechatCompany != null) {
                    Example example2 = new Example(QuBaoMingCompanyUserCollection.class);
                    Example.Criteria criteria2 = example2.createCriteria();
                    criteria2.andEqualTo("userId", showActiveByPageQuery.getUserId());
                    criteria2.andEqualTo("companyId", wechatCompany.getId());
                    QuBaoMingCompanyUserCollection quBaoMingCompanyUserCollection = quBaoMingCompanyUserCollectionService.selectOneByExample(example2);
                    if (quBaoMingCompanyUserCollection != null) {
                        activeByShowActiveCompleteVO.setHasCollectionCompany(true);
                    } else {
                        activeByShowActiveCompleteVO.setHasCollectionCompany(false);
                    }
                } else {
                    activeByShowActiveCompleteVO.setHasCollectionCompany(false);
                }


                activeByShowActiveCompleteVOList.add(activeByShowActiveCompleteVO);
            }


            pageInfo.setList(activeByShowActiveCompleteVOS);

        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        return pageInfo;
    }
}
