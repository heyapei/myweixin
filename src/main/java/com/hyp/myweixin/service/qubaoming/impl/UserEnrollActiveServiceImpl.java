package com.hyp.myweixin.service.qubaoming.impl;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.dto.mail.MailDTO;
import com.hyp.myweixin.pojo.qubaoming.model.*;
import com.hyp.myweixin.service.MailService;
import com.hyp.myweixin.service.qubaoming.*;
import com.hyp.myweixin.utils.MyErrorList;
import com.hyp.myweixin.utils.MySeparatorUtil;
import com.hyp.myweixin.utils.dateutil.MyDateStyle;
import com.hyp.myweixin.utils.dateutil.MyDateUtil;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/18 19:42
 * @Description: TODO
 */
@Service
@Slf4j
public class UserEnrollActiveServiceImpl implements UserEnrollActiveService {


    @Autowired
    private QubaomingActiveUserCollectionService qubaomingActiveUserCollectionService;
    @Autowired
    private QubaomingWeixinUserService qubaomingWeixinUserService;

    @Autowired
    private QubaomingActiveBaseService qubaomingActiveBaseService;

    @Autowired
    private QuBaoMingActiveConfigService qubaomingActiveConfigService;


    @Autowired
    private QubaomingUserSignUpService qubaomingUserSignUpService;

    @Autowired
    private MailService mailService;


    /**
     * 用户报名参加活动
     * 1. 写入报名数据
     * 2. 添加参与人数
     *
     * @param userId
     * @param activeId
     * @param signUpOption 报名的填报信息
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer addSignUpByActiveIdAndUserId(Integer userId, Integer activeId, String signUpOption) throws MyDefinitionException {
        if (userId == null || activeId == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        try {
            qubaomingWeixinUserService.validateUserRight(userId);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }

        QubaomingActiveBase qubaomingActiveBase = null;
        try {
            qubaomingActiveBase = qubaomingActiveBaseService.selectByPkId(activeId);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        if (qubaomingActiveBase == null) {
            throw new MyDefinitionException("没有找到指定的活动");
        }

        /*处理表情包问题*/
        if (StringUtils.isNotBlank(signUpOption)) {
            String s = EmojiParser.removeAllEmojis(signUpOption);
            if (StringUtils.isNotBlank(s)) {
                signUpOption = s;
            } else {
                signUpOption = "";
            }
        }

        Example example = new Example(QubaomingUserSignUp.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("activeId", activeId);
        try {
            QubaomingUserSignUp qubaomingUserSignUp = qubaomingUserSignUpService.selectOneQubaomingUserSignUpByExample(example);
            if (qubaomingUserSignUp != null) {
                throw new MyDefinitionException("当前已经报名成功，无需重复报名");
            }
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }

        List<QubaomingActiveConfig> qubaomingActiveConfigs = qubaomingActiveConfigService.selectByActiveId(activeId);
        if (qubaomingActiveConfigs != null && qubaomingActiveConfigs.size() > 0) {
            QubaomingActiveConfig qubaomingActiveConfig = qubaomingActiveConfigs.get(0);
            String[] split = qubaomingActiveConfig.getActiveRequireOption().split(MySeparatorUtil.SEMICOLON_SEPARATOR);
            if (split.length != signUpOption.split(MySeparatorUtil.SEMICOLON_SEPARATOR).length) {
                throw new MyDefinitionException("当前填报的信息不满足活动要求");
            }
        } else {
            throw new MyDefinitionException("当前活动配置项不完整");
        }


        QubaomingUserSignUp qubaomingUserSignUp = QubaomingUserSignUp.init();
        qubaomingUserSignUp.setUserId(userId);
        qubaomingUserSignUp.setActiveId(activeId);
        qubaomingUserSignUp.setSignUpInfo(signUpOption);
        Integer pkId = qubaomingUserSignUpService.insertReturnPk(qubaomingUserSignUp);
        if (pkId != null && pkId > 0) {
            qubaomingActiveBase.setActiveJoinNum(qubaomingActiveBase.getActiveJoinNum() + 1);
            try {
                qubaomingActiveBaseService.updateSelectiveQubaomingActiveBase(qubaomingActiveBase);
            } catch (MyDefinitionException e) {
                throw new MyDefinitionException(e.getMessage());
            }
        }
        sendUserEnrollMail(qubaomingUserSignUp);
        return pkId;
    }

    @Async("threadPoolTaskExecutor")
    void sendUserEnrollMail(QubaomingUserSignUp qubaomingUserSignUp) {
        MailDTO mailDTO = new MailDTO();
        QubaomingWeixinUser qubaomingWeixinUser = null;
        try {
            qubaomingWeixinUser = qubaomingWeixinUserService.selectByPkId(qubaomingUserSignUp.getUserId());
        } catch (MyDefinitionException e) {
            // do nothing
        }
        String nickName = "无此用户";
        if (qubaomingWeixinUser != null) {
            nickName = qubaomingWeixinUser.getNickName();
        }
        QubaomingActiveBase qubaomingActiveBase = null;
        try {
            qubaomingActiveBase = qubaomingActiveBaseService.selectByPkId(qubaomingUserSignUp.getActiveId());
        } catch (MyDefinitionException e) {
            // do nothing
        }
        String activeName = "无此活动";
        if (qubaomingActiveBase != null) {
            activeName = qubaomingActiveBase.getActiveName();
        }
        mailDTO.setContent("您好，用户ID：" + qubaomingUserSignUp.getUserId() + "（" + nickName + "），在" + MyDateUtil.DateToString(new Date(), MyDateStyle.YYYY_MM_DD_HH_MM)
                + "，报名了活动ID：" + qubaomingUserSignUp.getActiveId() + "（" + activeName + "）,报名填写内容为：" + qubaomingUserSignUp.getSignUpInfo() + "！请您悉知，感谢您使用趣报名平台！");
        mailDTO.setAttachment(null);
        mailDTO.setTitle("趣报名--用户报名成功通知");
        mailDTO.setEmail("1004683635@qq.com");
        mailService.sendTextMailAsync(mailDTO);
    }


    /**
     * 获取指定活动要求的必填项目
     *
     * @param activeId
     * @return 返回的是String类型，前端判断是否包含某个字段用于显示
     * @throws MyDefinitionException
     */
    @Override
    public String getSignUpOption(Integer activeId) throws MyDefinitionException {

        if (activeId == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        List<QubaomingActiveConfig> qubaomingActiveConfigs = qubaomingActiveConfigService.selectByActiveId(activeId);
        if (qubaomingActiveConfigs != null && qubaomingActiveConfigs.size() > 0) {
            return qubaomingActiveConfigs.get(0).getActiveRequireOption();
        }
        return null;
    }

    /**
     * 验证用户是否可以报名
     * 1. 是否在时间范围内
     * 2. 是否已经报名了
     * 3. 报名名额是否已满
     * 4. 人员是否拥有权限
     * 5. 活动是否已上线
     *
     * @param userId
     * @param activeId
     * @return 处理结果 为null就说明可以 不为null就要处理
     * @throws MyDefinitionException
     */
    @Override
    public MyErrorList ValidateUserSignActive(Integer userId, Integer activeId)
            throws MyDefinitionException {

        if (userId == null || activeId == null) {
            throw new MyDefinitionException("参数不能为空");
        }

        /*判断是否已经报名了*/
        Example example = new Example(QubaomingUserSignUp.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("activeId", activeId);
        try {
            QubaomingUserSignUp qubaomingUserSignUp = qubaomingUserSignUpService.selectOneQubaomingUserSignUpByExample(example);
            if (qubaomingUserSignUp != null) {
                throw new MyDefinitionException("当前已经报名成功，无需重复报名");
            }
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }

        try {
            qubaomingWeixinUserService.validateUserRight(userId);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        QubaomingActiveBase qubaomingActiveBase = null;
        try {
            qubaomingActiveBase = qubaomingActiveBaseService.selectByPkId(activeId);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        /*判断活动是否可以报名*/
        if (qubaomingActiveBase == null || !qubaomingActiveBase.getActiveStatus().equals(QubaomingActiveBase.ActiveStatusEnum.ONLINE.getCode())) {
            throw new MyDefinitionException("当前活动不允许报名");
        }
        /*判断活动配置项*/
        QubaomingActiveConfig qubaomingActiveConfig = null;
        List<QubaomingActiveConfig> qubaomingActiveConfigs = null;
        try {
            qubaomingActiveConfigs = qubaomingActiveConfigService.selectByActiveId(activeId);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        if (qubaomingActiveConfigs != null && qubaomingActiveConfigs.size() > 0) {
            qubaomingActiveConfig = qubaomingActiveConfigs.get(0);
        } else {
            throw new MyDefinitionException("没有找到当前活动的配置项");
        }
        long timeMillis = System.currentTimeMillis();
        Long activeStartTime = qubaomingActiveConfig.getActiveStartTime();
        Long activeEndTime = qubaomingActiveConfig.getActiveEndTime();
        Long signUpEndTime = qubaomingActiveConfig.getSignUpEndTime();
        Long signUpStartTime = qubaomingActiveConfig.getSignUpStartTime();
/*
        if (activeEndTime < timeMillis) {
            throw new MyDefinitionException("活动已结束");
        }
        if (activeStartTime > timeMillis) {
            throw new MyDefinitionException("活动未开始");
        }*/

        if (signUpStartTime > timeMillis) {
            throw new MyDefinitionException("报名时间未开始");
        }
        if (signUpEndTime < timeMillis) {
            throw new MyDefinitionException("报名时间已结束");
        }

        Example example1 = new Example(QubaomingUserSignUp.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("activeId", activeId);
        try {
            List<QubaomingUserSignUp> qubaomingUserSignUpList = qubaomingUserSignUpService.selectQubaomingUserSignUpByExample(example1);
            if (qubaomingUserSignUpList != null && qubaomingActiveConfig.getActiveJoinNumMax() > 0 && qubaomingUserSignUpList.size() >= qubaomingActiveConfig.getActiveJoinNumMax()) {
                throw new MyDefinitionException("当前活动报名名额已满");
            }
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        return null;
    }

    /**
     * 用户收藏活动
     * 1. 删除收藏
     * 2. 减少收藏数
     *
     * @param userId
     * @param activeId
     * @return 影响的行数
     * @throws MyDefinitionException
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer UserUnEnrollActive(Integer userId, Integer activeId) throws MyDefinitionException {

        if (userId == null || activeId == null) {
            throw new MyDefinitionException("参数不能为空");
        }

        Example example = new Example(QubaomingActiveUserCollection.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("activeId", activeId);
        Integer integer = null;
        try {
            integer = qubaomingActiveUserCollectionService.deleteQubaomingActiveUserCollectionByExample(example);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }

        if (integer == null || integer <= 0) {
            throw new MyDefinitionException("未能找到您收藏的活动数据");
        }

        QubaomingActiveBase qubaomingActiveBase = null;
        try {
            qubaomingActiveBase = qubaomingActiveBaseService.selectByPkId(activeId);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        if (qubaomingActiveBase == null) {
            throw new MyDefinitionException("未能发现指定的活动");
        }

        /*更新收藏数*/
        qubaomingActiveBase.setActiveCollectionNum(qubaomingActiveBase.getActiveCollectionNum() - 1);
        try {
            qubaomingActiveBaseService.updateSelectiveQubaomingActiveBase(qubaomingActiveBase);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }

        return integer;
    }

    /**
     * 用户收藏活动
     * 1. 判断用户权限
     * 2. 判断活动是否存在
     * 3. 添加收藏
     * 4. 添加收藏数
     *
     * @param userId
     * @param activeId
     * @return 用户收藏活动的主键信息
     * @throws MyDefinitionException
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Integer UserEnrollActive(Integer userId, Integer activeId) throws MyDefinitionException {

        if (userId == null || activeId == null) {
            throw new MyDefinitionException("参数不能为空");
        }


        try {
            qubaomingWeixinUserService.validateUserRight(userId);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }


        /*如果已经有收藏了 不允许多次收藏*/
        Example example = new Example(QubaomingActiveUserCollection.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("activeId", activeId);
        QubaomingActiveUserCollection qubaomingActiveUserCollection1 = qubaomingActiveUserCollectionService.selectOneQubaomingActiveUserCollectionByExample(example);
        if (qubaomingActiveUserCollection1 != null) {
            throw new MyDefinitionException("您已经收藏了该活动不允许重复收藏");
        }

        QubaomingActiveBase qubaomingActiveBase = null;
        try {
            qubaomingActiveBase = qubaomingActiveBaseService.selectByPkId(activeId);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        if (qubaomingActiveBase == null) {
            throw new MyDefinitionException("未能发现指定的活动");
        }


        QubaomingActiveUserCollection qubaomingActiveUserCollection = QubaomingActiveUserCollection.init();
        qubaomingActiveUserCollection.setUserId(userId);
        qubaomingActiveUserCollection.setActiveId(activeId);

        /*添加收藏*/
        Integer integer = null;
        try {
            integer = qubaomingActiveUserCollectionService.insertReturnPk(qubaomingActiveUserCollection);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        /*更新收藏数*/
        qubaomingActiveBase.setActiveCollectionNum(qubaomingActiveBase.getActiveCollectionNum() + 1);
        try {
            qubaomingActiveBaseService.updateSelectiveQubaomingActiveBase(qubaomingActiveBase);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }


        return integer;
    }
}
