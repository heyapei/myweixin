package com.hyp.myweixin.service.qubaoming.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingActiveBase;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingActiveConfig;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingActiveUserCollection;
import com.hyp.myweixin.pojo.qubaoming.model.WechatCompany;
import com.hyp.myweixin.pojo.qubaoming.query.active.ShowActiveByPageQuery;
import com.hyp.myweixin.pojo.qubaoming.vo.active.ActiveByShowActiveCompleteVO;
import com.hyp.myweixin.service.qubaoming.*;
import com.hyp.myweixin.utils.MyEntityUtil;
import com.hyp.myweixin.utils.MySeparatorUtil;
import com.hyp.myweixin.utils.dateutil.MyDateUtil;
import lombok.extern.slf4j.Slf4j;
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
    private QubaomingActiveConfigService qubaomingActiveConfigService;

    @Autowired
    private QubaomingActiveUserCollectionService qubaomingActiveUserCollectionService;

    @Autowired
    private QuBaoMingCompanyUserCollectionService quBaoMingCompanyUserCollectionService;


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
        criteria.andEqualTo("activeStatus", QubaomingActiveBase.ActiveStatusEnum.ONLINE.getCode());
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
               // activeByShowActiveCompleteVO.setHasCollectionActive(false);

                activeByShowActiveCompleteVOList.add(activeByShowActiveCompleteVO);
            }

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
        criteria.andEqualTo("activeStatus", QubaomingActiveBase.ActiveStatusEnum.ONLINE.getCode());
        example.orderBy("activeShowOrder").desc();
        example.orderBy("activeJoinNum").desc();
        example.orderBy("activeShareNum").desc();
        example.orderBy("activeCollectionNum").desc();
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


                activeByShowActiveCompleteVOList.add(activeByShowActiveCompleteVO);
            }


            pageInfo.setList(activeByShowActiveCompleteVOS);

        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        return pageInfo;
    }
}
