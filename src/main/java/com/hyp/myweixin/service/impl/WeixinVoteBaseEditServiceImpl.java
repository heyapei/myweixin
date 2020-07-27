package com.hyp.myweixin.service.impl;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.modal.WeixinVoteBase;
import com.hyp.myweixin.pojo.modal.WeixinVoteConf;
import com.hyp.myweixin.pojo.modal.WeixinVoteOrganisers;
import com.hyp.myweixin.pojo.query.activeedit.ActiveEditFourthQuery;
import com.hyp.myweixin.pojo.query.activeedit.ActiveEditSecondQuery;
import com.hyp.myweixin.pojo.query.activeedit.ActiveEditThirdQuery;
import com.hyp.myweixin.pojo.vo.page.activeeditor.ActiveEditFirstVO;
import com.hyp.myweixin.pojo.vo.page.activeeditor.ActiveEditFourthVO;
import com.hyp.myweixin.pojo.vo.page.activeeditor.ActiveEditSecondVO;
import com.hyp.myweixin.pojo.vo.page.activeeditor.ActiveEditThirdVO;
import com.hyp.myweixin.service.*;
import com.hyp.myweixin.service.smallwechatapi.WeixinSmallContentDetectionApiService;
import com.hyp.myweixin.utils.MyEnumUtil;
import com.hyp.myweixin.utils.MyErrorList;
import com.hyp.myweixin.utils.dateutil.DateStyle;
import com.hyp.myweixin.utils.dateutil.MyDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/21 19:54
 * @Description: TODO
 */
@Slf4j
@Service
public class WeixinVoteBaseEditServiceImpl implements WeixinVoteBaseEditService {

    @Autowired
    private WeixinVoteBaseService weixinVoteBaseService;
    @Autowired
    private WeixinVoteConfService weixinVoteConfService;

    private static final String SEMICOLON_SEPARATOR = ";";

    @Autowired
    private AdministratorsOptionService administratorsOptionService;

    @Autowired
    private WeixinVoteOrganisersService weixinVoteOrganisersService;

    @Autowired
    private WeixinSmallContentDetectionApiService weixinSmallContentDetectionApiService;

    /**
     * 更新活动状态
     * 要求必须是管理员
     *
     * @param userId
     * @param activeId
     * @param activeStatus
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public Integer changeActiveStatus(Integer userId, Integer activeId, Integer activeStatus) throws MyDefinitionException {

        if (activeId == null || userId == null) {
            throw new MyDefinitionException("当前接口要求用户登录且活动指向明确");
        }
        if (activeStatus == null) {
            throw new MyDefinitionException("必须指定要修改的活动状态值");
        }

        WeixinVoteBase weixinVoteBase = null;
        try {
            weixinVoteBase = weixinVoteBaseService.getWeixinVoteBaseByWorkId(activeId);
        } catch (Exception e) {
            throw new MyDefinitionException("查询操作错误，" + e.getMessage());
        }
        if (weixinVoteBase == null) {
            throw new MyDefinitionException("想要查询的活动不存在");
        } else {
            /*判断是否为超级管理员 如果是就不做任何判断*/
            if (!administratorsOptionService.isSuperAdministrators(userId)) {
                if (!weixinVoteBase.getCreateSysUserId().equals(userId)) {
                    throw new MyDefinitionException("您不是该活动的管理员");
                }
            }
        }

        //是否允许更新的状态数据
        Boolean enumKeyRight = MyEnumUtil.enumKeyRight(activeStatus, WeixinVoteBase.ActiveStatusEnum.class);
        if (!enumKeyRight) {
            throw new MyDefinitionException("想要更改的活动状态不被允许,请联系系统管理");
        }

        Integer status = weixinVoteBase.getStatus();
        if (status.equals(activeStatus)) {
            return 1;
        }
        weixinVoteBase.setStatus(activeStatus);
        weixinVoteBase.setUpdateTime(new Date());
        int i = weixinVoteBaseService.updateVoteBaseVote(weixinVoteBase);
        if (i > 0) {
            return 1;
        }
        return i;
    }

    /**
     * 根据以下数据进行更新活动第四页中信息操作
     * 1. 要求是管理员
     *
     * @param activeEditFourthQuery 更新用实体类
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public Integer editActiveEditFourthQuery(ActiveEditFourthQuery activeEditFourthQuery) throws MyDefinitionException {

        if (activeEditFourthQuery == null) {
            throw new MyDefinitionException("更新活动第二页信息的参数不能为空");
        }
        Integer activeId = activeEditFourthQuery.getActiveId();
        Integer userId = activeEditFourthQuery.getUserId();
        if (activeId == null || userId == null) {
            throw new MyDefinitionException("当前接口要求用户登录且活动指向明确");
        }
        WeixinVoteBase weixinVoteBase = null;
        try {
            weixinVoteBase = weixinVoteBaseService.getWeixinVoteBaseByWorkId(activeId);
        } catch (Exception e) {
            throw new MyDefinitionException("查询操作错误，" + e.getMessage());
        }
        if (weixinVoteBase == null) {
            throw new MyDefinitionException("想要查询的活动不存在");
        } else {
            /*判断是否为超级管理员 如果是就不做任何判断*/
            if (!administratorsOptionService.isSuperAdministrators(userId)) {
                if (!weixinVoteBase.getCreateSysUserId().equals(userId)) {
                    throw new MyDefinitionException("您不是该活动的管理员");
                }
            }
        }
        MyErrorList myErrorList = new MyErrorList();
        /*性别是否能限制要判断当前是否有配置列表*/
        WeixinVoteConf weixinVoteConf = null;
        if (myErrorList.noErrors()) {
            weixinVoteConf = weixinVoteConfService.getWeixinVoteConfByVoteWorkId(activeId);
            if (weixinVoteConf == null) {
                myErrorList.add("未发现当前活动的配置内容");
            }
        }
        if (myErrorList.noErrors()) {
            weixinVoteConf.setActiveConfSex(activeEditFourthQuery.getSex());
        }
        if (myErrorList.noErrors()) {
            weixinVoteConf.setUpdateTime(new Date());
            Integer integer = weixinVoteConfService.updateWeixinVoteConf(weixinVoteConf);
            if (integer == null || integer <= 0) {
                myErrorList.add("保存活动相关配置错误");
            }
        }
        if (myErrorList.hasErrors()) {
            throw new MyDefinitionException(myErrorList.toPlainString());
        }
        return 1;
    }

    /**
     * 通过活动ID查询活动第四页需要回显的数据
     * 需要用户登录 且 为管理员
     *
     * @param activeId 活动ID
     * @param userId   用户ID
     * @return 第四页需要回显的数据
     * @throws MyDefinitionException
     */
    @Override
    public ActiveEditFourthVO getActiveEditFourthVOByActiveId(Integer activeId, Integer userId) throws MyDefinitionException {

        if (activeId == null || userId == null) {
            throw new MyDefinitionException("当前接口要求用户登录且活动指向明确");
        }
        WeixinVoteBase weixinVoteBase = null;
        try {
            weixinVoteBase = weixinVoteBaseService.getWeixinVoteBaseByWorkId(activeId);
        } catch (Exception e) {
            throw new MyDefinitionException("查询操作错误，" + e.getMessage());
        }
        if (weixinVoteBase == null) {
            throw new MyDefinitionException("想要查询的活动不存在");
        } else {
            /*判断是否为超级管理员 如果是就不做任何判断*/
            if (!administratorsOptionService.isSuperAdministrators(userId)) {
                if (!weixinVoteBase.getCreateSysUserId().equals(userId)) {
                    throw new MyDefinitionException("您不是该活动的管理员");
                }
            }
        }
        WeixinVoteConf weixinVoteConf = weixinVoteConfService.getWeixinVoteConfByVoteWorkId(activeId);
        if (weixinVoteConf == null) {
            throw new MyDefinitionException("未能发现当前活动的配置信息");
        }

        ActiveEditFourthVO activeEditFourthVO = new ActiveEditFourthVO();
        activeEditFourthVO.setActiveId(activeId);
        activeEditFourthVO.setSex(weixinVoteConf.getActiveConfSex());
        return activeEditFourthVO;
    }

    /**
     * 根据以下数据进行更新活动第三页中信息操作
     * 1. 要求是管理员
     *
     * @param activeEditThirdQuery 更新用实体类
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public Integer editActiveEditThirdQuery(ActiveEditThirdQuery activeEditThirdQuery) throws MyDefinitionException {

        if (activeEditThirdQuery == null) {
            throw new MyDefinitionException("更新活动第三页信息的参数不能为空");
        }

        log.info("更新第三页信息时候的请求参数：{}", activeEditThirdQuery.toString());
        Integer activeId = activeEditThirdQuery.getActiveId();
        Integer userId = activeEditThirdQuery.getUserId();
        if (activeId == null || userId == null) {
            throw new MyDefinitionException("当前接口要求用户登录且活动指向明确");
        }
        WeixinVoteBase weixinVoteBase = null;
        try {
            weixinVoteBase = weixinVoteBaseService.getWeixinVoteBaseByWorkId(activeId);
        } catch (Exception e) {
            throw new MyDefinitionException("查询操作错误，" + e.getMessage());
        }
        if (weixinVoteBase == null) {
            throw new MyDefinitionException("想要查询的活动不存在");
        } else {
            /*判断是否为超级管理员 如果是就不做任何判断*/
            if (!administratorsOptionService.isSuperAdministrators(userId)) {
                if (!weixinVoteBase.getCreateSysUserId().equals(userId)) {
                    throw new MyDefinitionException("您不是该活动的管理员");
                }
            }
        }
        MyErrorList myErrorList = new MyErrorList();
        /*if (myErrorList.noErrors()) {
            log.info("当前操作下没有错误");
        }*/
        /*绑定需要更新的数据*/
        /*活动开始结束时间*/
        if (myErrorList.noErrors()) {
            Date date = MyDateUtil.StringToDate(activeEditThirdQuery.getActiveStartTime(), DateStyle.YYYY_MM_DD_HH_MM);
            if (date != null) {
                weixinVoteBase.setActiveStartTime(date);
            } else {
                myErrorList.add("解析活动开始时间错误");
            }
        }
        if (myErrorList.noErrors()) {
            Date date = MyDateUtil.StringToDate(activeEditThirdQuery.getActiveEndTime(), DateStyle.YYYY_MM_DD_HH_MM);
            if (date != null) {
                weixinVoteBase.setActiveEndTime(date);
            } else {
                myErrorList.add("解析活动结束时间错误");
            }
        }
        if (myErrorList.noErrors()) {
            //log.info("当前的活动内容是：{}", weixinVoteBaseByWorkId.toString());
            weixinVoteBase.setUpdateTime(new Date());
            int i = weixinVoteBaseService.updateVoteBaseVote(weixinVoteBase);
            if (i <= 0) {
                myErrorList.add("保存活动开始/结束时间错误");
            }
        }


        /*是否可以重复投票 这里需要先判断活动是否已经有活动配置信息了*/
        WeixinVoteConf weixinVoteConf = null;
        if (myErrorList.noErrors()) {
            weixinVoteConf = weixinVoteConfService.getWeixinVoteConfByVoteWorkId(activeId);
            if (weixinVoteConf == null) {
                myErrorList.add("未发现当前活动的配置内容");
            }
        }
        if (myErrorList.noErrors()) {
            Integer activeConfRepeatVote = activeEditThirdQuery.getActiveConfRepeatVote();
            //log.info("允许重复投票吗？"+activeConfRepeatVote);
            /* 0默认不开启 1开启*/
            if (activeConfRepeatVote != null) {
                weixinVoteConf.setActiveConfRepeatVote(activeConfRepeatVote);
                weixinVoteConf.setActiveConfVoteType(activeEditThirdQuery.getActiveConfVoteType());
            }
        }

        /*是否允许用户自主上传配置*/
        if (myErrorList.noErrors()) {
            Integer activeConfSignUp = activeEditThirdQuery.getActiveConfSignUp();
            if (activeConfSignUp == null || activeConfSignUp == 1) {
                weixinVoteConf.setActiveConfSignUp(1);
            } else {
                weixinVoteConf.setActiveConfSignUp(0);
                /*报名开始时间*/
                Date date = MyDateUtil.StringToDate(activeEditThirdQuery.getActiveUploadStartTime(), DateStyle.YYYY_MM_DD_HH_MM);
                if (date != null) {
                    weixinVoteConf.setActiveUploadStartTime(date);
                } else {
                    myErrorList.add("解析报名开始时间错误");
                }
                /*报名结束时间*/
                if (myErrorList.noErrors()) {
                    date = MyDateUtil.StringToDate(activeEditThirdQuery.getActiveUploadEndTime(), DateStyle.YYYY_MM_DD_HH_MM);
                    if (date != null) {
                        weixinVoteConf.setActiveUploadEndTime(date);
                    } else {
                        myErrorList.add("解析报名结束时间错误");
                    }
                }

                /*报名是否需要微信号 0 默认不需要 1 需要*/
                if (myErrorList.noErrors()) {
                    Integer activeConfNeedWeixin = activeEditThirdQuery.getActiveConfNeedWeixin();
                    //log.info("当前是否需要微信号：{}",activeConfNeedWeixin);
                    if (activeConfNeedWeixin == null || activeConfNeedWeixin == 0) {
                        weixinVoteConf.setActiveConfNeedWeixin(0);
                    } else {
                        weixinVoteConf.setActiveConfNeedWeixin(1);
                    }
                }

                /*报名需要手机号 0 默认不需要 1 需要*/
                if (myErrorList.noErrors()) {
                    Integer activeConfNeedPhone = activeEditThirdQuery.getActiveConfNeedPhone();
                    // log.info("当前是否需要手机号：{}",activeConfNeedPhone);
                    if (activeConfNeedPhone == null || activeConfNeedPhone == 0) {
                        weixinVoteConf.setActiveConfNeedPhone(0);
                    } else {
                        weixinVoteConf.setActiveConfNeedPhone(1);
                    }
                }
            }
        }
        if (myErrorList.noErrors()) {
            weixinVoteConf.setUpdateTime(new Date());
            Integer integer = weixinVoteConfService.updateWeixinVoteConf(weixinVoteConf);
            if (integer == null || integer <= 0) {
                myErrorList.add("保存活动相关配置错误");
            }
        }
        if (myErrorList.hasErrors()) {
            throw new MyDefinitionException(myErrorList.toPlainString());
        }
        return 1;
    }

    /**
     * 通过活动ID查询活动第三页需要回显的数据
     * 需要用户登录 且 为管理员
     *
     * @param activeId 活动ID
     * @param userId   用户ID
     * @return 第三页需要回显的数据
     * @throws MyDefinitionException
     */
    @Override
    public ActiveEditThirdVO getActiveEditThirdVOByActiveId(Integer activeId, Integer userId) throws MyDefinitionException {

        if (activeId == null || userId == null) {
            throw new MyDefinitionException("当前接口要求用户登录且活动指向明确");
        }
        WeixinVoteBase weixinVoteBase = null;
        try {
            weixinVoteBase = weixinVoteBaseService.getWeixinVoteBaseByWorkId(activeId);
        } catch (Exception e) {
            throw new MyDefinitionException("查询操作错误，" + e.getMessage());
        }
        if (weixinVoteBase == null) {
            throw new MyDefinitionException("想要查询的活动不存在");
        } else {
            /*判断是否为超级管理员 如果是就不做任何判断*/
            if (!administratorsOptionService.isSuperAdministrators(userId)) {

                if (!weixinVoteBase.getCreateSysUserId().equals(userId)) {
                    throw new MyDefinitionException("您不是该活动的管理员");
                }
            }
        }
        WeixinVoteConf weixinVoteConf = weixinVoteConfService.getWeixinVoteConfByVoteWorkId(activeId);
        if (weixinVoteConf == null) {
            throw new MyDefinitionException("未能发现当前活动的配置信息");
        }

        ActiveEditThirdVO activeEditThirdVO = new ActiveEditThirdVO();
        activeEditThirdVO.setActiveId(activeId);
        activeEditThirdVO.setActiveStartTime(weixinVoteBase.getActiveStartTime());
        activeEditThirdVO.setActiveEndTime(weixinVoteBase.getActiveEndTime());
        activeEditThirdVO.setActiveUploadStartTime(weixinVoteConf.getActiveUploadStartTime());
        activeEditThirdVO.setActiveUploadEndTime(weixinVoteConf.getActiveUploadEndTime());
        activeEditThirdVO.setActiveConfNeedPhone(weixinVoteConf.getActiveConfNeedPhone());
        activeEditThirdVO.setActiveConfNeedWeixin(weixinVoteConf.getActiveConfNeedWeixin());
        activeEditThirdVO.setActiveConfRepeatVote(weixinVoteConf.getActiveConfRepeatVote());
        activeEditThirdVO.setActiveConfVoteType(weixinVoteConf.getActiveConfVoteType());
        activeEditThirdVO.setActiveConfSignUp(weixinVoteConf.getActiveConfSignUp());
        return activeEditThirdVO;
    }

    /**
     * 根据以下数据进行更新操作
     * 1. 要求是管理员
     * 2. 要求是创建完成的数据
     *
     * @param activeEditSecondQuery 更新用实体类
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public Integer editActiveEditSecondQuery(ActiveEditSecondQuery activeEditSecondQuery) throws MyDefinitionException {
        if (activeEditSecondQuery == null) {
            throw new MyDefinitionException("更新活动第二页信息的参数不能为空");
        }
        Integer activeId = activeEditSecondQuery.getActiveId();
        Integer userId = activeEditSecondQuery.getUserId();
        if (activeId == null || userId == null) {
            throw new MyDefinitionException("当前接口要求用户登录且活动指向明确");
        }
        WeixinVoteBase weixinVoteBase = null;
        try {
            weixinVoteBase = weixinVoteBaseService.getWeixinVoteBaseByWorkId(activeId);
        } catch (Exception e) {
            throw new MyDefinitionException("查询操作错误，" + e.getMessage());
        }
        if (weixinVoteBase == null) {
            throw new MyDefinitionException("想要查询的活动不存在");
        } else {
            /*判断是否为超级管理员 如果是就不做任何判断*/
            if (!administratorsOptionService.isSuperAdministrators(userId)) {

                if (!weixinVoteBase.getCreateSysUserId().equals(userId)) {
                    throw new MyDefinitionException("您不是该活动的管理员");
                }
            }
        }

        Boolean aBoolean = null;
        try {
            aBoolean = weixinSmallContentDetectionApiService.checkMsgSecCheckApi(
                    activeEditSecondQuery.getOrganisersName(),
                    null);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException("主办方名称文字检查未通过:" + e.getMessage());
        }
        if (aBoolean == null || aBoolean == false) {
            throw new MyDefinitionException("当前提交的主办方名称内容存在违规，请重新输入");
        }

        try {
            aBoolean = weixinSmallContentDetectionApiService.checkMsgSecCheckApi(
                    activeEditSecondQuery.getOrganisersPhone(),
                    null);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException("主办方手机号检查未通过:" + e.getMessage());
        }
        if (aBoolean == null || aBoolean == false) {
            throw new MyDefinitionException("当前提交的主办方手机号内容存在违规，请重新输入");
        }


        WeixinVoteConf weixinVoteConf = weixinVoteConfService.getWeixinVoteConfByVoteWorkId(activeId);
        if (weixinVoteConf == null) {
            weixinVoteConf = WeixinVoteConf.init();
            weixinVoteConf.setActiveVoteBaseId(activeId);
            weixinVoteConf.setActiveConfShareImg(activeEditSecondQuery.getActiveShareImg());
            Integer integer = weixinVoteConfService.saveWeixinVoteConf(weixinVoteConf);
            if (integer == null || integer <= 0) {
                throw new MyDefinitionException("更新活动配置项中新增错误");
            }
        } else {
            weixinVoteConf.setActiveConfShareImg(activeEditSecondQuery.getActiveShareImg());
            weixinVoteConf.setUpdateTime(new Date());
            Integer integer = weixinVoteConfService.updateWeixinVoteConf(weixinVoteConf);
            if (integer == null || integer <= 0) {
                throw new MyDefinitionException("更新活动配置项错误");
            }
        }

        WeixinVoteOrganisers weixinVoteOrganisers = weixinVoteOrganisersService.getWeixinVoteConfByVoteWorkId(activeId);
        /*hasOrganisers等于1标识有公司信息 0标识没有*/
        if (activeEditSecondQuery.getHasOrganisers() == 1) {

            if (activeEditSecondQuery.getOrganisersName() == null || activeEditSecondQuery.getOrganisersLogo() == null
                    || activeEditSecondQuery.getOrganisersPhone() == null || activeEditSecondQuery.getOrganisersWeixinCode() == null) {
                throw new MyDefinitionException("主办方信息必须填写");
            }


            if (weixinVoteOrganisers == null) {
                weixinVoteOrganisers = WeixinVoteOrganisers.init();
                weixinVoteOrganisers.setVoteBaseId(activeId);
                weixinVoteOrganisers.setName(activeEditSecondQuery.getOrganisersName());
                weixinVoteOrganisers.setLogoImg(activeEditSecondQuery.getOrganisersLogo());
                weixinVoteOrganisers.setPhone(activeEditSecondQuery.getOrganisersPhone());
                weixinVoteOrganisers.setWeixinQrCode(activeEditSecondQuery.getOrganisersWeixinCode());
                Integer saveWeixinVoteOrganisers = weixinVoteOrganisersService.saveWeixinVoteOrganisers(weixinVoteOrganisers);
                if (saveWeixinVoteOrganisers == null || saveWeixinVoteOrganisers <= 0) {
                    throw new MyDefinitionException("更新活动主办方信息中新增错误");
                }
            } else {
                weixinVoteOrganisers.setName(activeEditSecondQuery.getOrganisersName());
                weixinVoteOrganisers.setLogoImg(activeEditSecondQuery.getOrganisersLogo());
                weixinVoteOrganisers.setPhone(activeEditSecondQuery.getOrganisersPhone());
                weixinVoteOrganisers.setWeixinQrCode(activeEditSecondQuery.getOrganisersWeixinCode());
                Integer updateSelectiveWeixinVoteOrganisers = weixinVoteOrganisersService.updateSelectiveWeixinVoteOrganisers(weixinVoteOrganisers);
                if (updateSelectiveWeixinVoteOrganisers == null || updateSelectiveWeixinVoteOrganisers <= 0) {
                    throw new MyDefinitionException("更新活动主办方信息错误");
                }
            }
        } else {
            if (weixinVoteOrganisers != null) {
                WeixinVoteOrganisers weixinVoteOrganisersTemp = WeixinVoteOrganisers.init();
                weixinVoteOrganisersTemp.setId(weixinVoteOrganisers.getId());
                weixinVoteOrganisersTemp.setVoteBaseId(weixinVoteOrganisers.getVoteBaseId());
                Integer saveWeixinVoteOrganisers = weixinVoteOrganisersService.updateSelectiveWeixinVoteOrganisers(weixinVoteOrganisersTemp);
                if (saveWeixinVoteOrganisers == null || saveWeixinVoteOrganisers <= 0) {
                    throw new MyDefinitionException("更新活动主办方信息错误");
                }
            }
        }
        return 1;
    }

    /**
     * 通过活动ID查询活动第二页需要回显的数据
     * 需要用户登录 且 为管理员
     *
     * @param activeId 活动ID
     * @param userId   用户ID
     * @return 第二页需要回显的数据
     * @throws MyDefinitionException
     */
    @Override
    public ActiveEditSecondVO getActiveEditSecondVOByActiveId(Integer activeId, Integer userId) throws MyDefinitionException {

        if (activeId == null || userId == null) {
            throw new MyDefinitionException("当前接口要求用户登录且活动指向明确");
        }
        WeixinVoteBase weixinVoteBase = null;
        try {
            weixinVoteBase = weixinVoteBaseService.getWeixinVoteBaseByWorkId(activeId);
        } catch (Exception e) {
            throw new MyDefinitionException("查询操作错误，" + e.getMessage());
        }
        if (weixinVoteBase == null) {
            throw new MyDefinitionException("想要查询的活动不存在");
        } else {
            /*判断是否为超级管理员 如果是就不做任何判断*/
            if (!administratorsOptionService.isSuperAdministrators(userId)) {

                if (!weixinVoteBase.getCreateSysUserId().equals(userId)) {
                    throw new MyDefinitionException("您不是该活动的管理员");
                }
            }
        }
        WeixinVoteConf weixinVoteConf = weixinVoteConfService.getWeixinVoteConfByVoteWorkId(activeId);
        if (weixinVoteConf == null) {
            throw new MyDefinitionException("未能发现当前活动的配置信息");
        }

        WeixinVoteOrganisers weixinVoteOrganisers = weixinVoteOrganisersService.getWeixinVoteConfByVoteWorkId(activeId);
        if (weixinVoteOrganisers == null) {
            throw new MyDefinitionException("未能发现当前活动的主办方信息");
        }


        ActiveEditSecondVO activeEditSecondVO = new ActiveEditSecondVO();
        activeEditSecondVO.setShowPublic(weixinVoteBase.getActivePublic());
        activeEditSecondVO.setActiveId(activeId);
        activeEditSecondVO.setActiveShareImg(weixinVoteConf.getActiveConfShareImg().replaceAll(SEMICOLON_SEPARATOR, ""));
        String organisersName = weixinVoteOrganisers.getName();
        if (StringUtils.isNotBlank(organisersName)) {
            activeEditSecondVO.setHasOrganisers(1);
            activeEditSecondVO.setOrganisersName(organisersName);
            activeEditSecondVO.setOrganisersPhone(weixinVoteOrganisers.getPhone().replaceAll(SEMICOLON_SEPARATOR, ""));
            activeEditSecondVO.setOrganisersWeixinCode(weixinVoteOrganisers.getWeixinQrCode().replaceAll(SEMICOLON_SEPARATOR, ""));
            activeEditSecondVO.setOrganisersLogo(weixinVoteOrganisers.getLogoImg().replaceAll(SEMICOLON_SEPARATOR, ""));
        } else {
            activeEditSecondVO.setHasOrganisers(0);
        }
        return activeEditSecondVO;
    }

    /**
     * 根据以下数据进行更新操作
     * 1. 要求是管理员
     * 2. 要求是创建完成的数据
     *
     * @param userId     用户ID
     * @param activeId   活动ID
     * @param type       上传的数据类型
     * @param activeText 上传的文本 非必须
     * @param activeImg  上传的图片 使用英文;拼接好的
     * @return
     */
    @Override
    public Integer editBaseVoteWorkSavePageAndImg(int userId, int activeId, String type, String activeText, String activeImg) {

        WeixinVoteBase weixinVoteBase = weixinVoteBaseService.getWeixinVoteBaseByWorkId(activeId);

        if (weixinVoteBase == null) {
            return -1;
        }

        if (!administratorsOptionService.isSuperAdministrators(userId)) {
            if (!weixinVoteBase.getCreateSysUserId().equals(userId)) {
                throw new MyDefinitionException("您不是该活动的管理员");
            }
        }
        if (StringUtils.isBlank(type)) {
            log.error("上传的文件数据没有指定文件类型：{}", type);
        }

        Boolean aBoolean = null;
        try {
            aBoolean = weixinSmallContentDetectionApiService.checkMsgSecCheckApi(
                    activeText,
                    null);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException("违规内容检查未通过:" + e.getMessage());
        }
        if (aBoolean == null || aBoolean == false) {
            throw new MyDefinitionException("当前提交的文字内容存在违规，请重新输入");
        }


        /*如果类型为activeCoverImg 则保存 封面图 和 活动标题*/
        if (type.equalsIgnoreCase("activeCoverImg")) {
            if (activeImg != null) {
                weixinVoteBase.setActiveImg(activeImg);
            }
            weixinVoteBase.setActiveName(activeText);
        }

        /*如果类型是activeDesc 则保存 介绍文字 和 介绍图片*/
        if (type.equalsIgnoreCase("activeDesc")) {
            if (activeImg != null && activeImg.contains(";")) {
                String[] split = activeImg.split(";");
                StringBuffer imgUrlS = new StringBuffer();
                for (String s : split) {
                    imgUrlS.append(s).append(";");
                }
                weixinVoteBase.setActiveDescImg(imgUrlS.toString());
            }
            weixinVoteBase.setActiveDesc(activeText);
        }

        /*如果类型是activeReward 则保存 奖励文字 和 奖励图片*/
        if (type.equalsIgnoreCase("activeReward")) {
            if (activeImg != null && activeImg.contains(";")) {
                String[] split = activeImg.split(";");
                StringBuffer imgUrlS = new StringBuffer();
                for (String s : split) {
                    imgUrlS.append(s).append(";");
                }
                weixinVoteBase.setActiveRewardImg(imgUrlS.toString());
            }
            weixinVoteBase.setActiveReward(activeText);
        }
        /*创建时间每次都会更新的*/
        weixinVoteBase.setUpdateTime(new Date());
        return weixinVoteBaseService.updateVoteBaseVote(weixinVoteBase);
    }

    /**
     * 通过活动ID查询活动第一页需要回显的数据
     * 需要用户登录 且 为管理员
     *
     * @param activeId 活动ID
     * @param userId   用户ID
     * @return 第一页需要回显的数据
     * @throws MyDefinitionException
     */
    @Override
    public ActiveEditFirstVO getActiveEditFirstVOByActiveId(Integer activeId, Integer userId) throws MyDefinitionException {

        if (activeId == null || userId == null) {
            throw new MyDefinitionException("当前接口要求用户登录且活动指向明确");
        }


        WeixinVoteBase weixinVoteBase = null;
        try {
            weixinVoteBase = weixinVoteBaseService.getWeixinVoteBaseByWorkId(activeId);
        } catch (Exception e) {
            throw new MyDefinitionException("查询操作错误，" + e.getMessage());
        }
        if (weixinVoteBase == null) {
            throw new MyDefinitionException("想要查询的活动不存在");
        } else {
            /*判断是否为超级管理员 如果是就不做任何判断*/
            if (!administratorsOptionService.isSuperAdministrators(userId)) {

                if (!weixinVoteBase.getCreateSysUserId().equals(userId)) {
                    throw new MyDefinitionException("您不是该活动的管理员");
                }
            }
        }

        ActiveEditFirstVO activeEditFirstVO = new ActiveEditFirstVO();
        activeEditFirstVO.setUserId(userId);
        activeEditFirstVO.setActiveId(activeId);
        activeEditFirstVO.setActiveImg(weixinVoteBase.getActiveImg().replaceAll(SEMICOLON_SEPARATOR, ""));
        activeEditFirstVO.setActiveDesc(weixinVoteBase.getActiveDesc());
        if (weixinVoteBase.getActiveDescImg() != null && weixinVoteBase.getActiveDescImg().contains(SEMICOLON_SEPARATOR)) {
            activeEditFirstVO.setActiveDescImgS(weixinVoteBase.getActiveDescImg().split(SEMICOLON_SEPARATOR));
        } else {
            activeEditFirstVO.setActiveDescImgS(new String[0]);
        }

        activeEditFirstVO.setActiveName(weixinVoteBase.getActiveName());
        activeEditFirstVO.setActiveReward(weixinVoteBase.getActiveReward());
        String activeRewardImg = weixinVoteBase.getActiveRewardImg();
        if (activeRewardImg != null && activeRewardImg.contains(SEMICOLON_SEPARATOR)) {
            activeEditFirstVO.setActiveRewardImgS(activeRewardImg.split(SEMICOLON_SEPARATOR));
        } else {
            activeEditFirstVO.setActiveRewardImgS(new String[0]);
        }


        return activeEditFirstVO;
    }
}
