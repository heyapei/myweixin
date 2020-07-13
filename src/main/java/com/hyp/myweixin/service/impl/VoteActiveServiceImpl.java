package com.hyp.myweixin.service.impl;

import com.hyp.myweixin.config.imgvideres.ImgVideResConfig;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.dto.ResourceSimpleDTO;
import com.hyp.myweixin.pojo.dto.WeixinVoteWorkDTO;
import com.hyp.myweixin.pojo.modal.*;
import com.hyp.myweixin.pojo.query.voteactive.Page2OrgShowQuery;
import com.hyp.myweixin.pojo.query.voteactive.Page3RegulationQuery;
import com.hyp.myweixin.pojo.query.voteactive.Page4RegulationQuery;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.*;
import com.hyp.myweixin.utils.MyEntityUtil;
import com.hyp.myweixin.utils.MyErrorList;
import com.hyp.myweixin.utils.dateutil.DateStyle;
import com.hyp.myweixin.utils.dateutil.MyDateUtil;
import com.hyp.myweixin.utils.fileutil.MyFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/27 21:37
 * @Description: TODO
 */
@Service
@Slf4j
public class VoteActiveServiceImpl implements VoteActiveService {


    @Autowired
    private ImgVideResConfig imgVideResConfig;

    @Autowired
    private WeixinResourceService weixinResourceService;
    @Autowired
    private WeixinResourceConfigService weixinResourceConfigService;

    @Autowired
    private WeixinVoteBaseService weixinVoteBaseService;
    @Autowired
    private WeixinVoteConfService weixinVoteConfService;
    @Autowired
    private WeixinVoteOrganisersService weixinVoteOrganisersService;
    @Autowired
    private WeixinVoteUserService weixinVoteUserService;


    /**
     * 添加第四屏幕的内容
     * 1. 性别限制
     * <p>
     * 逻辑：
     * 1.检查是否有配置数据 如果有则更新没有就生成 生成过程中写入数据
     *
     * @param page4RegulationQuery 查询实体类
     * @return 判断result的值
     */
    @Override
    public MyErrorList createPage4Regulation(Page4RegulationQuery page4RegulationQuery) {

        MyErrorList myErrorList = new MyErrorList();
        /*判断值是否为空*/
        if (page4RegulationQuery == null) {
            myErrorList.add("创建第四页信息的参数不能为空");
        }
        /*判断活动是否属于当前用户 保证用户和活动的有效性*/
        Integer userId = page4RegulationQuery.getUserId();
        Integer voteWorkId = page4RegulationQuery.getVoteWorkId();
        if (myErrorList.noErrors()) {
            if (!judgeIsOperatorLegal(userId, voteWorkId)) {
                myErrorList.add("人员信息或活动内容错误");
            }
        }

        /*查找当前的活动*/
        WeixinVoteBase weixinVoteBaseByWorkId = null;
        if (myErrorList.noErrors()) {
            weixinVoteBaseByWorkId = weixinVoteBaseService.getWeixinVoteBaseByWorkId(voteWorkId);
            if (weixinVoteBaseByWorkId == null) {
                myErrorList.add("未能查找到正确的活动的内容");
            }
        }

        /*性别是否能限制要判断当前是否有配置列表*/
        WeixinVoteConf weixinVoteConf = null;
        if (myErrorList.noErrors()) {
            weixinVoteConf = weixinVoteConfService.getWeixinVoteConfByVoteWorkId(voteWorkId);
            if (weixinVoteConf == null) {
                myErrorList.add("未发现当前活动的配置内容");
            }
        }


        if (myErrorList.noErrors()) {
            weixinVoteConf.setActiveConfSex(page4RegulationQuery.getSex());
        }


        if (myErrorList.noErrors()) {
            weixinVoteConf.setUpdateTime(new Date());
            Integer integer = weixinVoteConfService.updateWeixinVoteConf(weixinVoteConf);
            if (integer == null || integer <= 0) {
                myErrorList.add("保存活动相关配置错误");
            }
        }

        if (myErrorList.noErrors()) {
            weixinVoteBaseByWorkId.setStatus(1);
            int i = weixinVoteBaseService.updateVoteBaseVote(weixinVoteBaseByWorkId);
            if (i <= 0) {
                myErrorList.add("活动上线失败");
            }
        }

        return myErrorList;
    }

    /**
     * 添加第三屏幕的内容
     * 1. 活动开始时间 必须
     * 2. 活动结束时间 必须
     * 3. 是否可以对对手重复点赞 必须
     * 4. 是否开启在线报名 必须
     * 5. 报名开始时间 必须
     * 6. 报名结束时间 必须
     * 7. 报名手机号必填 必填
     * 8. 报名微信号必填 必填
     * 逻辑：
     * 1.检查是否有配置数据 如果有则更新没有就生成 生成过程中写入数据
     *
     * @param page3RegulationQuery 查询实体类
     * @return
     */
    @Override
    public MyErrorList createPage3Regulation(Page3RegulationQuery page3RegulationQuery) {

        MyErrorList myErrorList = new MyErrorList();
        /*判断值是否为空*/
        if (page3RegulationQuery == null) {
            myErrorList.add("创建第三页信息的参数不能为空");
        }
        /*判断活动是否属于当前用户 保证用户和活动的有效性*/
        Integer userId = page3RegulationQuery.getUserId();
        Integer voteWorkId = page3RegulationQuery.getVoteWorkId();
        if (myErrorList.noErrors()) {
            if (!judgeIsOperatorLegal(userId, voteWorkId)) {
                myErrorList.add("人员信息或活动内容错误");
            }
        }

        /*查找当前的活动*/
        WeixinVoteBase weixinVoteBaseByWorkId = null;
        if (myErrorList.noErrors()) {
            weixinVoteBaseByWorkId = weixinVoteBaseService.getWeixinVoteBaseByWorkId(voteWorkId);
            if (weixinVoteBaseByWorkId == null) {
                myErrorList.add("未能查找到正确的活动的内容");
            }
        }

        /*绑定需要更新的数据*/
        /*活动开始结束时间*/
        if (myErrorList.noErrors()) {
            Date date = MyDateUtil.StringToDate(page3RegulationQuery.getActiveStartTime(), DateStyle.YYYY_MM_DD_HH_MM);
            if (date != null) {
                weixinVoteBaseByWorkId.setActiveStartTime(date);
            } else {
                myErrorList.add("解析活动开始时间错误");
            }
        }
        if (myErrorList.noErrors()) {
            Date date = MyDateUtil.StringToDate(page3RegulationQuery.getActiveEndTime(), DateStyle.YYYY_MM_DD_HH_MM);
            if (date != null) {
                weixinVoteBaseByWorkId.setActiveEndTime(date);
            } else {
                myErrorList.add("解析活动结束时间错误");
            }
        }


        if (myErrorList.noErrors()) {
            //log.info("当前的活动内容是：{}", weixinVoteBaseByWorkId.toString());
            weixinVoteBaseByWorkId.setUpdateTime(new Date());
            int i = weixinVoteBaseService.updateVoteBaseVote(weixinVoteBaseByWorkId);
            if (i <= 0) {
                myErrorList.add("保存活动开始/结束时间错误");
            }
        }


        /*是否可以重复投票 这里需要先判断活动是否已经有活动配置信息了*/
        WeixinVoteConf weixinVoteConf = null;
        if (myErrorList.noErrors()) {
            log.info("当前的活动ID：{}", voteWorkId);
            weixinVoteConf = weixinVoteConfService.getWeixinVoteConfByVoteWorkId(voteWorkId);
            if (weixinVoteConf == null) {
                myErrorList.add("未发现当前活动的配置内容");
            }
        }
        if (myErrorList.noErrors()) {
            Integer activeConfRepeatVote = page3RegulationQuery.getActiveConfRepeatVote();
            //log.info("允许重复投票吗？"+activeConfRepeatVote);
            /* 0默认不开启 1开启*/
            if (activeConfRepeatVote == null) {
                weixinVoteConf.setActiveConfRepeatVote(activeConfRepeatVote);
                weixinVoteConf.setActiveConfVoteType(page3RegulationQuery.getActiveConfVoteType());
                //log.info("不允许");
            }/* else {
                String activeConfVoteType = page3RegulationQuery.getActiveConfVoteType();
                if (StringUtils.isNotBlank(activeConfVoteType) && activeConfVoteType.contains(";")) {
                    String[] activeConfVoteTypeSplit = activeConfVoteType.split(";");
                    weixinVoteConf.setActiveConfRepeatVote(Integer.parseInt(activeConfVoteTypeSplit[0]));
                    //log.info("不允许多票");
                    weixinVoteConf.setActiveConfVoteType(Integer.parseInt(activeConfVoteTypeSplit[1]));
                } else {
                    myErrorList.add("当前投票配置模式下有必填项没有完整填写");
                }
            }*/
        }

        /*是否允许用户自主上传配置*/
        if (myErrorList.noErrors()) {
            Integer activeConfSignUp = page3RegulationQuery.getActiveConfSignUp();
            if (activeConfSignUp == null || activeConfSignUp == 1) {
                weixinVoteConf.setActiveConfSignUp(1);
            } else {
                weixinVoteConf.setActiveConfSignUp(0);
                /*报名开始时间*/
                Date date = MyDateUtil.StringToDate(page3RegulationQuery.getActiveUploadStartTime(), DateStyle.YYYY_MM_DD_HH_MM);
                if (date != null) {
                    weixinVoteConf.setActiveUploadStartTime(date);
                } else {
                    myErrorList.add("解析报名开始时间错误");
                }
                /*报名结束时间*/
                if (myErrorList.noErrors()) {
                    date = MyDateUtil.StringToDate(page3RegulationQuery.getActiveUploadEndTime(), DateStyle.YYYY_MM_DD_HH_MM);
                    if (date != null) {
                        weixinVoteConf.setActiveUploadEndTime(date);
                    } else {
                        myErrorList.add("解析报名结束时间错误");
                    }
                }

                /*报名是否需要微信号 0 默认不需要 1 需要*/
                if (myErrorList.noErrors()) {
                    Integer activeConfNeedWeixin = page3RegulationQuery.getActiveConfNeedWeixin();
                    if (activeConfNeedWeixin == null || activeConfNeedWeixin == 0) {
                        weixinVoteConf.setActiveConfNeedWeixin(0);
                    } else {
                        weixinVoteConf.setActiveConfNeedWeixin(1);
                    }
                }

                /*报名需要手机号 0 默认不需要 1 需要*/
                if (myErrorList.noErrors()) {
                    Integer activeConfNeedPhone = page3RegulationQuery.getActiveConfNeedPhone();
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

        return myErrorList;
    }

    @Override
    public Integer createPage2AndImg(Page2OrgShowQuery page2Query) {
        WeixinVoteBase weixinVoteBaseByWorkId;
        Integer userId = page2Query.getUserId();
        Integer voteWorkId = page2Query.getVoteWorkId();
        if (judgeIsOperatorLegal(userId, voteWorkId)) {
            weixinVoteBaseByWorkId = weixinVoteBaseService.getWeixinVoteBaseByWorkId(voteWorkId);
            /*是否公开到首页 0默认不公开 1公开*/
            weixinVoteBaseByWorkId.setActivePublic(page2Query.getIsShowIndex());
            weixinVoteBaseService.updateVoteBaseVote(weixinVoteBaseByWorkId);
            WeixinVoteConf weixinVoteConf = weixinVoteConfService.getWeixinVoteConfByVoteWorkId(voteWorkId);
            if (weixinVoteConf == null) {
                weixinVoteConf = new WeixinVoteConf();
                weixinVoteConf.setActiveVoteBaseId(voteWorkId);
                weixinVoteConf.setActiveConfMusicId(0);
                weixinVoteConf.setActiveConfRepeatVote(0);
                weixinVoteConf.setActiveConfVoteType("1,1");
                weixinVoteConf.setActiveConfSignUp(0);
                weixinVoteConf.setActiveConfVerify(0);
                weixinVoteConf.setActiveConfNumHide(0);
                weixinVoteConf.setActiveConfUserHide(0);
                weixinVoteConf.setActiveConfRankHide(0);
                weixinVoteConf.setCreateTime(new Date());
                weixinVoteConf.setUpdateTime(new Date());
                weixinVoteConf.setActiveUploadStartTime(new Date());
                weixinVoteConf.setActiveUploadEndTime(new Date());
                weixinVoteConf.setActiveConfSex("");
                weixinVoteConf.setActiveConfRegion("");
                weixinVoteConf.setActiveConfNeedWeixin(0);
                weixinVoteConf.setActiveConfNeedPhone(0);
                weixinVoteConf.setActiveConfShareImg(page2Query.getShareImg());
                weixinVoteConfService.saveWeixinVoteConf(weixinVoteConf);
            } else {
                weixinVoteConf.setActiveConfShareImg(page2Query.getShareImg());
                weixinVoteConf.setUpdateTime(new Date());
                weixinVoteConfService.updateWeixinVoteConf(weixinVoteConf);
            }

            /*hasOrganisers等于1标识有公司信息 0标识没有*/
            if (page2Query.getHasOrganisers() == 1) {
                WeixinVoteOrganisers weixinVoteOrganisers = weixinVoteOrganisersService.getWeixinVoteConfByVoteWorkId(voteWorkId);
                if (weixinVoteOrganisers == null) {
                    weixinVoteOrganisers = new WeixinVoteOrganisers();
                    weixinVoteOrganisers.setVoteBaseId(voteWorkId);
                    weixinVoteOrganisers.setName(page2Query.getOrgName());
                    weixinVoteOrganisers.setLogoImg(page2Query.getOrgLogoImg());
                    weixinVoteOrganisers.setOrganisersDesc("");
                    weixinVoteOrganisers.setPhone(page2Query.getOrgPhone());
                    weixinVoteOrganisers.setAddress("");
                    weixinVoteOrganisers.setCompany("");
                    weixinVoteOrganisers.setType("");
                    weixinVoteOrganisers.setJobMajor("");
                    weixinVoteOrganisers.setBuildTime(new Date());
                    weixinVoteOrganisers.setCorporate("");
                    weixinVoteOrganisers.setWeixinQrCode(page2Query.getOrgWeixinQrCode());
                    Integer saveWeixinVoteOrganisers = weixinVoteOrganisersService.saveWeixinVoteOrganisers(weixinVoteOrganisers);
                    if (saveWeixinVoteOrganisers == null || saveWeixinVoteOrganisers <= 0) {
                        return -1;
                    }
                } else {
                    weixinVoteOrganisers.setName(page2Query.getOrgName());
                    weixinVoteOrganisers.setLogoImg(page2Query.getOrgLogoImg());
                    weixinVoteOrganisers.setPhone(page2Query.getOrgPhone());
                    weixinVoteOrganisers.setWeixinQrCode(page2Query.getOrgWeixinQrCode());
                    Integer updateSelectiveWeixinVoteOrganisers = weixinVoteOrganisersService.updateSelectiveWeixinVoteOrganisers(weixinVoteOrganisers);
                    if (updateSelectiveWeixinVoteOrganisers == null || updateSelectiveWeixinVoteOrganisers <= 0) {
                        return -1;
                    }
                }
            }
        } else {
            return -1;
        }
        return 1;
    }

    /**
     * 判断操作是否合理，主要是 1人员信息是否正确 2该人员是否允许修改
     *
     * @param userId
     * @param voteWorkId
     * @return
     */
    private boolean judgeIsOperatorLegal(Integer userId, Integer voteWorkId) {
        WeixinVoteUser userById = weixinVoteUserService.getUserById(userId);
        if (userById == null) {
            return false;
        }
        WeixinVoteBase weixinVoteBaseByWorkId = weixinVoteBaseService.getWeixinVoteBaseByWorkId(voteWorkId);
        if (weixinVoteBaseByWorkId == null) {
            return false;
        }

        if (!weixinVoteBaseByWorkId.getCreateSysUserId().equals(userId)) {
            return false;
        }

        return true;
    }

    /**
     * 根据以下数据进行更新操作
     *
     * @param userId     用户ID
     * @param workId     活动ID
     * @param type       上传的数据类型
     * @param activeText 上传的文本 非必须
     * @param activeImg  上传的图片 使用英文;拼接好的
     * @return
     */
    @Override
    public Integer createBaseVoteWorkSavePageAndImg(int userId, int workId, String type, String activeText, String activeImg) {
        WeixinVoteBase weixinVoteBase = null;
        // 先查询出来该用户下活动状态为4（未创建完成）的活动
        List<WeixinVoteBase> weixinVoteBaseByUserIdAndStatus = weixinVoteBaseService.getWeixinVoteBaseByUserIdAndStatus(userId, 4);
        if (weixinVoteBaseByUserIdAndStatus != null && weixinVoteBaseByUserIdAndStatus.size() > 0) {
            weixinVoteBase = weixinVoteBaseByUserIdAndStatus.get(0);
        } else {
            log.error("该用户:{}，没有未创建完成的活动", userId);
            return -1;
        }

        if (weixinVoteBase.getId() != workId) {
            log.error("活动：{}，不属于该用户：{}", workId, userId);
            return -1;
        }

        if (StringUtils.isBlank(type)) {
            log.error("上传的文件数据没有指定文件类型：{}", type);
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
     * 返回活动的ID 如果用户没有创建完成的数据则创建一个 ，如果有则返回新的活动ID
     *
     * @param userId
     * @return
     */
    @Override
    public Integer createBaseVoteWork(int userId) {
        if (userId <= 0) {
            return -1;
        }
        WeixinVoteUser userById = weixinVoteUserService.getUserById(userId);
        if (userById == null) {
            return -1;
        }
        List<WeixinVoteBase> weixinVoteBaseByUserIdAndStatus = weixinVoteBaseService.getWeixinVoteBaseByUserIdAndStatus(userId, 4);
        if (weixinVoteBaseByUserIdAndStatus != null && weixinVoteBaseByUserIdAndStatus.size() > 0) {
            return weixinVoteBaseByUserIdAndStatus.get(0).getId();
        }
        WeixinVoteBase weixinVoteBase = new WeixinVoteBase();
        weixinVoteBase.setId(0);
        weixinVoteBase.setActiveImg("");
        weixinVoteBase.setActiveName("");
        weixinVoteBase.setActiveDesc("");
        weixinVoteBase.setActiveDescImg("");
        weixinVoteBase.setActiveReward("");
        weixinVoteBase.setActiveRewardImg("");
        weixinVoteBase.setActiveStartTime(new Date());
        weixinVoteBase.setActiveEndTime(new Date());
        weixinVoteBase.setActivePublic(0);
        weixinVoteBase.setActiveShowOrder(0);
        weixinVoteBase.setStatus(4);
        weixinVoteBase.setCreateTime(new Date());
        weixinVoteBase.setUpdateTime(new Date());
        weixinVoteBase.setCreateSysUserId(userId);
        weixinVoteBase.setViewCountNum(0);
        weixinVoteBase.setVoteCountNum(0);
        Integer saveVoteBase = null;
        try {
            saveVoteBase = weixinVoteBaseService.saveVoteBase(weixinVoteBase);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("新增用户活动创建失败，失败原因{}", e.toString());
        }

        return saveVoteBase;
    }

    /**
     * 创建活动
     *
     * @param weixinVoteWorkDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer createVoteWork(WeixinVoteWorkDTO weixinVoteWorkDTO) {

        /*在这里做一下预判断 因为有可能会出现图片链接地址出现空的情况*/

        /*活动展示封面*/
        String activeImg = weixinVoteWorkDTO.getActiveImg();
        if (activeImg.contains(";")) {
            String[] split = activeImg.split(";");
            StringBuffer imgUrlS = new StringBuffer();
            for (String s : split) {
                imgUrlS.append(s).append(";");
            }
            weixinVoteWorkDTO.setActiveImg(imgUrlS.toString());
        }

        /*活动介绍*/
        String activeDescImg = weixinVoteWorkDTO.getActiveDescImg();
        if (activeDescImg.contains(";")) {
            String[] split = activeDescImg.split(";");
            StringBuffer imgUrlS = new StringBuffer();
            for (String s : split) {
                imgUrlS.append(s).append(";");
            }
            weixinVoteWorkDTO.setActiveDescImg(imgUrlS.toString());
        }
        /*活动奖励*/
        String activeRewardImg = weixinVoteWorkDTO.getActiveRewardImg();
        if (activeRewardImg.contains(";")) {
            String[] split = activeRewardImg.split(";");
            StringBuffer imgUrlS = new StringBuffer();
            for (String s : split) {
                imgUrlS.append(s).append(";");
            }
            weixinVoteWorkDTO.setActiveRewardImg(imgUrlS.toString());
        }


        Integer resultVal = null;

        WeixinVoteBase weixinVoteBaseWithWeixinVoteWorkDTO = getWeixinVoteBaseWithWeixinVoteWorkDTO(weixinVoteWorkDTO);
        log.info("查询数据：{}", weixinVoteBaseWithWeixinVoteWorkDTO.toString());
        Integer saveVoteBaseKey = weixinVoteBaseService.saveVoteBase(weixinVoteBaseWithWeixinVoteWorkDTO);
        resultVal = saveVoteBaseKey;
        if (saveVoteBaseKey != null && saveVoteBaseKey > 0) {
            log.info("保存活动基础信息成功成功");
            /*提取weixinVoteOrganisers*/
            WeixinVoteOrganisers weixinVoteOrganisersWithWeixinVoteWorkDTO = getWeixinVoteOrganisersWithWeixinVoteWorkDTO(weixinVoteWorkDTO);
            weixinVoteOrganisersWithWeixinVoteWorkDTO.setVoteBaseId(saveVoteBaseKey);
            Integer saveWeixinVoteOrganisers = weixinVoteOrganisersService.saveSelectiveWeixinVoteOrganisers(weixinVoteOrganisersWithWeixinVoteWorkDTO);
            resultVal = saveWeixinVoteOrganisers;
            if (saveWeixinVoteOrganisers != null && saveWeixinVoteOrganisers > 0) {
                log.info("保存活动主办方信息成功");
                /*提取WeixinVoteConf数据*/
                WeixinVoteConf weixinVoteConfWithWeixinVoteWorkDTO = getWeixinVoteConfWithWeixinVoteWorkDTO(weixinVoteWorkDTO);

                weixinVoteConfWithWeixinVoteWorkDTO.setActiveVoteBaseId(saveVoteBaseKey);

                Integer saveWeixinVoteConf = weixinVoteConfService.saveSelectiveWeixinVoteConf(weixinVoteConfWithWeixinVoteWorkDTO);
                resultVal = saveWeixinVoteConf;
                if (saveWeixinVoteConf != null && saveWeixinVoteConf > 0) {
                    log.info("保存活动配置信息成功");
                }
            }
        }
        return resultVal;
    }


    /**
     * 从weixinVoteWorkDTO提取weixinVoteConf数据
     *
     * @param weixinVoteWorkDTO
     * @return
     */
    private static WeixinVoteConf getWeixinVoteConfWithWeixinVoteWorkDTO(WeixinVoteWorkDTO weixinVoteWorkDTO) {
        WeixinVoteConf weixinVoteConf = null;
        try {
            weixinVoteConf = MyEntityUtil.entity2VM(weixinVoteWorkDTO, WeixinVoteConf.class);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("从weixinVoteWorkDTO提取weixinVoteConf数据错误，错误原因：{}", e.toString());
        }
        return weixinVoteConf;
    }

    /**
     * 从weixinVoteWorkDTO提取weixinVoteOrganisers数据
     *
     * @param weixinVoteWorkDTO
     * @return
     */
    private static WeixinVoteOrganisers getWeixinVoteOrganisersWithWeixinVoteWorkDTO(WeixinVoteWorkDTO weixinVoteWorkDTO) {
        WeixinVoteOrganisers weixinVoteOrganisers = null;
        try {
            weixinVoteOrganisers = MyEntityUtil.entity2VM(weixinVoteWorkDTO, WeixinVoteOrganisers.class);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("从weixinVoteWorkDTO提取weixinVoteOrganisers数据错误，错误原因：{}", e.toString());
        }
        return weixinVoteOrganisers;
    }


    /**
     * 从weixinVoteWorkDTO提取WeixinVoteBase数据
     *
     * @param weixinVoteWorkDTO
     * @return
     */
    private static WeixinVoteBase getWeixinVoteBaseWithWeixinVoteWorkDTO(WeixinVoteWorkDTO weixinVoteWorkDTO) {
        WeixinVoteBase weixinVoteBase = null;
        try {
            weixinVoteBase = MyEntityUtil.entity2VM(weixinVoteWorkDTO, WeixinVoteBase.class);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("从weixinVoteWorkDTO提取WeixinVoteBase数据错误，错误原因：{}", e.toString());
        }
        weixinVoteBase.setViewCountNum(0);
        weixinVoteBase.setVoteCountNum(0);
        return weixinVoteBase;
    }

    /**
     * 保存图片资源
     *
     * @param file
     * @param type
     * @return
     */
    @Override
    public Result saveSingleRes(MultipartFile file, String type) {

        log.info("数据类型");
        log.info("数据类型，{}", type);

        if (type == null) {
            throw new MyDefinitionException("未指定上传文件自定义类型");
        }
        if (file == null) {
            throw new MyDefinitionException("上传文件为空");
        }


        String path = null;
        try {
            path = ResourceUtils.getURL("classpath:").getPath();
            //log.info("系统路径：{}", path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new MyDefinitionException("获取项目路径失败");
        }


        ResourceSimpleDTO resourceSimpleDTO = new ResourceSimpleDTO();


        String fileMd5 = MyFileUtil.getFileMd5(file);
        log.info("文件MD5：{}", fileMd5);

        WeixinResource weixinResourceByMD5 = weixinResourceService.getWeixinResourceByMD5(fileMd5);
        // 如果存在则直接返回数据
        if (weixinResourceByMD5 != null) {
            resourceSimpleDTO.setFileUrl(weixinResourceByMD5.getPath());
            resourceSimpleDTO.setFileType(weixinResourceByMD5.getType());
            resourceSimpleDTO.setSaveFileName(weixinResourceByMD5.getName());
            resourceSimpleDTO.setOriginalFileName(weixinResourceByMD5.getRealName());
            resourceSimpleDTO.setFileSize(weixinResourceByMD5.getSize());
            log.info("直接返回数据：{}", weixinResourceByMD5.toString());
            return Result.buildResult(Result.Status.OK, resourceSimpleDTO);
        }

        log.info("数据类型：{}", type);

        int resource_config_id = 0;
        String savePath = path + imgVideResConfig.getActiveImgBasePath();
        if (type.equalsIgnoreCase(imgVideResConfig.getActiveVoteWorkPath())) {
            savePath += imgVideResConfig.getActiveVoteWorkPath();
            resource_config_id = 2;
        } else if (type.equalsIgnoreCase(imgVideResConfig.getActiveUserWorkPath())) {
            resource_config_id = 3;
            savePath += imgVideResConfig.getActiveUserWorkPath();
        } else {
            throw new MyDefinitionException("还未定义数据，还请和技术沟通");
        }

        // 文件大小
        resourceSimpleDTO.setFileSize(file.getSize());
        // 文件类型
        // 获得文件类型 image/jpeg
        String fileType = file.getContentType();
        // 获得文件后缀名称 jpeg
        fileType = fileType.substring(fileType.indexOf("/") + 1);
        resourceSimpleDTO.setFileType(fileType);

        // 原名称 test.jpg
        String fileName = file.getOriginalFilename();

        // 文件原名称 去除后缀
        String fileOriginalFilename = fileName.substring(0, file.getOriginalFilename().lastIndexOf("."));
        resourceSimpleDTO.setOriginalFileName(fileOriginalFilename);

        String filename = fileName.substring(fileName.lastIndexOf("\\") + 1);
        //得到文件保存的名称 f1a315ae4b7a4c81b5cf1cb396fbfdd2_test.jpg
        String saveFilename = MyFileUtil.makeFileName(filename);
        //log.info("图片最后名字{}", saveFilename);
        resourceSimpleDTO.setSaveFileName(saveFilename.substring(0, saveFilename.lastIndexOf(".")));
        //得到文件的保存目录
        String realSavePath = MyFileUtil.makePath(saveFilename, savePath);
        try {
            MyFileUtil.upload(file, realSavePath, saveFilename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //String dataSavePath = File.separator + realSavePath.substring(realSavePath.indexOf(imgVideResConfig.getActiveImgBasePath()));
        String dataSavePath = "/" + realSavePath.substring(realSavePath.indexOf(imgVideResConfig.getActiveImgBasePath()));
        // 拼接图片url
        String fileUrl = dataSavePath + "/" + saveFilename;
        resourceSimpleDTO.setFileUrl(fileUrl);


        WeixinResource weixinResource = new WeixinResource();
        weixinResource.setCreateTime(new Date());
        weixinResource.setType(resourceSimpleDTO.getFileType());
        weixinResource.setDescription("用户上传文件");
        weixinResource.setName(resourceSimpleDTO.getSaveFileName());
        weixinResource.setRealName(resourceSimpleDTO.getOriginalFileName());
        weixinResource.setMd5(fileMd5);
        weixinResource.setPath(fileUrl);
        weixinResource.setTitle("资源文件");
        weixinResource.setResourceConfigId(resource_config_id);
        weixinResource.setStatus(0);
        weixinResource.setSize(resourceSimpleDTO.getFileSize());
        WeixinResourceConfig weixinResourceConfigById = weixinResourceConfigService.getWeixinResourceConfigById(resource_config_id);
        if (weixinResourceConfigById != null) {
            weixinResource.setTitle(weixinResourceConfigById.getKeyWord());
            weixinResource.setDescription(weixinResourceConfigById.getDescription());
        } else {
            weixinResource.setTitle("");
            weixinResource.setDescription("");
        }
        weixinResource.setCreateUserId(0);

        weixinResourceService.addWeixinResource(weixinResource);


        return Result.buildResult(Result.Status.OK, resourceSimpleDTO);
    }
}
