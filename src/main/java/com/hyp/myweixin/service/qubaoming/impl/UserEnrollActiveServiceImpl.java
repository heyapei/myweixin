package com.hyp.myweixin.service.qubaoming.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.dto.mail.MailDTO;
import com.hyp.myweixin.pojo.qubaoming.model.*;
import com.hyp.myweixin.service.MailService;
import com.hyp.myweixin.service.qubaoming.*;
import com.hyp.myweixin.service.smallwechatapi.WeixinSmallContentDetectionApiService;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private WechatCompanyService wechatCompanyService;

    /**
     * 获取活动的报名人的头像数据
     *
     * @param activeId
     * @param pageNum
     * @param pageSize
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public PageInfo<Object> getSignUpUserInfoByActiveIdPage(Integer activeId, Integer pageNum, Integer pageSize) throws MyDefinitionException {

        if (activeId == null) {
            throw new MyDefinitionException("参数不能为空");
        }


        Example example = new Example(QubaomingUserSignUp.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("activeId", activeId);
        example.orderBy("createTime").desc();
        PageHelper.startPage(pageNum, pageSize);
        PageInfo pageInfo = null;
        List<QubaomingUserSignUp> qubaomingUserSignUpList = null;
        try {
            qubaomingUserSignUpList = qubaomingUserSignUpService.selectQubaomingUserSignUpByExample(example);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        if (qubaomingUserSignUpList != null) {
            pageInfo = new PageInfo(qubaomingUserSignUpList);
        }


        List<String> userAvatars = new ArrayList<>();
        for (QubaomingUserSignUp qubaomingUserSignUp : qubaomingUserSignUpList) {
            QubaomingWeixinUser qubaomingWeixinUser = qubaomingWeixinUserService.selectByPkId(qubaomingUserSignUp.getUserId());
            userAvatars.add(qubaomingWeixinUser.getAvatarUrl());
        }
        pageInfo.setList(userAvatars);
        return pageInfo;
    }

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

        QubaomingWeixinUser qubaomingWeixinUser = qubaomingWeixinUserService.selectByPkId(userId);


        String phone = "";
        String name = "";
        String activeRequireOption = qubaomingActiveConfigs.get(0).getActiveRequireOption();
        String[] activeRequireOptionList = activeRequireOption.split(MySeparatorUtil.SEMICOLON_SEPARATOR);
        String[] signUpInfo = qubaomingUserSignUp.getSignUpInfo().split(MySeparatorUtil.SEMICOLON_SEPARATOR);

        for (int i = 0; i < activeRequireOptionList.length; i++) {
            if (activeRequireOptionList[i].equalsIgnoreCase(
                    String.valueOf(QubaomingActiveConfig.ActiveRequireOptionEnum.PHONE.getCode()))) {
                phone = signUpInfo[i];
            } else if (activeRequireOptionList[i].equalsIgnoreCase(
                    String.valueOf(QubaomingActiveConfig.ActiveRequireOptionEnum.NAME.getCode()))) {
                name = signUpInfo[i];
            }
        }

        sendUserSubmitMessage(qubaomingWeixinUser.getOpenId(),
                phone,
                qubaomingActiveConfigs.get(0).getActiveAddress(),
                qubaomingActiveBase.getActiveName(),
                name,
                MyDateUtil.numberDateFormat(String.valueOf(
                        qubaomingActiveConfigs.get(0).getActiveStartTime()), "yyyy年MM月dd日 HH:mm"));

        return pkId;
    }

    @Autowired
    private WeixinSmallContentDetectionApiService weixinSmallContentDetectionApiService;

    @Async("threadPoolTaskExecutor")
    void sendUserSubmitMessage(String openId, String phone, String activeAddress,
                               String activeName, String realName, String activeStartTime) {

        if (StringUtils.isNotBlank(phone)) {
            int length = 17;
            if (phone.length() >= length) {
                phone = phone.substring(0, length - 1);
            }
        } else {
            phone = "";
        }

        if (StringUtils.isNotBlank(activeAddress)) {
            int length = 20;
            if (activeAddress.length() >= length) {
                activeAddress = activeAddress.substring(0, length - 1);
            }
        } else {
            activeAddress = "";
        }

        if (StringUtils.isNotBlank(activeName)) {
            int length = 20;
            if (activeName.length() >= length) {
                activeName = activeName.substring(0, length - 1);
            }
        } else {
            activeName = "";
        }


        if (StringUtils.isNotBlank(realName)) {
            int length = 10;
            if (realName.length() >= length) {
                realName = realName.substring(0, length - 1);
            }
        } else {
            realName = "";
        }


        String jsonString = "{\n" +
                "\t\"touser\": \"" + openId + "\",\n" +
                "\t\"data\": {\n" +
                "\t\t\"phone_number7\": {\n" +
                "\t\t\t\"value\": \"" + phone + "\"\n" +
                "\t\t},\n" +
                "\t\t\"thing3\": {\n" +
                "\t\t\t\"value\": \"" + activeAddress + "\"\n" +
                "\t\t},\n" +
                "\t\t  \"thing2\": {\n" +
                "\t\t\t\"value\": \"" + activeName + "\"\n" +
                "\t\t},\n" +
                "\t\t\"date4\": {\n" +
                "\t\t\t\"value\": \"" + activeStartTime + "\"\n" +
                "\t\t},\n" +
                "\t\t\"name1\": {\n" +
                "\t\t\t\"value\": \"" + realName + "\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t\"template_id\": \"AqFKNC0aP5aQFEZXaDVilNWSy_F39KgHX_USzmGwbcM\",\n" +
                "\t\"miniprogram_state\": \"formal\",\n" +
                "\t\"page\": \"pages/index/index\",\n" +
                "\t\"lang\": \"zh_CN\"\n" +
                "}";
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        //log.info("json参数：{}", jsonObject.toJSONString());
        JSONObject jsonObject1 = weixinSmallContentDetectionApiService.
                sendQuBaoMingUserSubmitMessage(jsonObject);


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

        QubaomingActiveConfig qubaomingActiveConfig = qubaomingActiveConfigService.selectOneByActiveId(qubaomingUserSignUp.getActiveId());
        StringBuilder stringBuilder = new StringBuilder();
        String activeRequireOption = qubaomingActiveConfig.getActiveRequireOption();
        String[] activeRequireOptionList = activeRequireOption.split(MySeparatorUtil.SEMICOLON_SEPARATOR);
        String[] signUpInfo = qubaomingUserSignUp.getSignUpInfo().split(MySeparatorUtil.SEMICOLON_SEPARATOR);

        for (int i = 0; i < activeRequireOptionList.length; i++) {
            if (activeRequireOptionList[i].equalsIgnoreCase(
                    String.valueOf(QubaomingActiveConfig.ActiveRequireOptionEnum.NAME.getCode()))) {
                stringBuilder.append(QubaomingActiveConfig.ActiveRequireOptionEnum.NAME.getMsg())
                        .append(":")
                        .append(signUpInfo[i]).append(";");
            } else if (activeRequireOptionList[i].equalsIgnoreCase(
                    String.valueOf(QubaomingActiveConfig.ActiveRequireOptionEnum.AGE.getCode()))) {
                stringBuilder.append(QubaomingActiveConfig.ActiveRequireOptionEnum.AGE.getMsg())
                        .append(":")
                        .append(signUpInfo[i]).append("岁;");
            } else if (activeRequireOptionList[i].equalsIgnoreCase(
                    String.valueOf(QubaomingActiveConfig.ActiveRequireOptionEnum.PHONE.getCode()))) {
                stringBuilder.append(QubaomingActiveConfig.ActiveRequireOptionEnum.PHONE.getMsg())
                        .append(":")
                        .append(signUpInfo[i]).append(";");
            } else if (activeRequireOptionList[i].equalsIgnoreCase(
                    String.valueOf(QubaomingActiveConfig.ActiveRequireOptionEnum.GENDER.getCode()))) {
                stringBuilder.append(QubaomingActiveConfig.ActiveRequireOptionEnum.GENDER.getMsg())
                        .append(":")
                        .append(signUpInfo[i]).append(";");
            }
        }

       /* mailDTO.setContent("您好，用户ID：" + qubaomingUserSignUp.getUserId() + "（" + nickName + "），在" + MyDateUtil.DateToString(new Date(), MyDateStyle.YYYY_MM_DD_HH_MM)
                + "，报名了活动ID：" + qubaomingUserSignUp.getActiveId() + "（" + activeName + "）,报名填写内容为：" + stringBuilder.toString() + "！请您悉知，感谢您使用趣报名平台！");
        */
        mailDTO.setContent("您好，" + "（" + nickName + "），在" + MyDateUtil.DateToString(new Date(), MyDateStyle.YYYY_MM_DD_HH_MM)
                + "，报名了活动" + "（" + activeName + "）,报名填写内容为：" + stringBuilder.toString() + "！请您悉知，感谢您使用趣报名平台！");
        mailDTO.setAttachment(null);
        mailDTO.setTitle("趣报名--用户报名成功通知");

        ArrayList<String> emailAddress = new ArrayList<>();
        emailAddress.add("1004683635@qq.com");
        emailAddress.add("15518901416@163.com");
        WechatCompany wechatCompany = wechatCompanyService.selectOneByUserId(qubaomingActiveBase.getActiveCompanyId());
        if (wechatCompany != null) {
            if (StringUtils.isNotBlank(wechatCompany.getCompanyEmail())) {

                /**
                 * 防止给超级管理员重复发送相同内容
                 */
                String companyEmail = wechatCompany.getCompanyEmail();
                if (companyEmail.equalsIgnoreCase("1004683635@qq.com")
                        || companyEmail.equalsIgnoreCase("15518901416@163.com")) {

                } else {
                    //定义要匹配的Email地址的正则表达式
                    //其中\w代表可用作标识符的字符,不包括$. \w+表示多个
                    //  \\.\\w表示点.后面有\w 括号{2,3}代表这个\w有2至3个
                    //牵扯到有些邮箱类似com.cn结尾 所以(\\.\\w{2,3})*后面表示可能有另一个2至3位的域名结尾
                    //*表示重复0次或更多次
                    String regex = "\\w+@\\w+(\\.\\w{2,3})*\\.\\w{2,3}";
                    if (wechatCompany.getCompanyEmail().matches(regex)) {
                        emailAddress.add(wechatCompany.getCompanyEmail());
                    }
                }
            }
            //emailAddress.add(wechatCompany.getCompanyEmail());
        }


        /*去重*/
        List<String> listWithoutDuplicates = emailAddress.stream().distinct().
                collect(Collectors.toList());

        for (String address : listWithoutDuplicates) {


            mailDTO.setEmail(address);
            mailService.sendTextMailAsync(mailDTO);
        }
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


    /**
     * 获取活动收藏人员头像信息
     *
     * @param activeId
     * @param pageNum
     * @param pageSize
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public PageInfo<Object> getCollectionUserInfoByActiveIdPage(Integer activeId, Integer pageNum, Integer pageSize) throws MyDefinitionException {
        if (activeId == null) {
            throw new MyDefinitionException("参数不能为空");
        }
        Example example = new Example(QubaomingActiveUserCollection.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("activeId", activeId);
        example.orderBy("createTime").desc();
        PageHelper.startPage(pageNum, pageSize);
        PageInfo pageInfo = null;
        List<QubaomingActiveUserCollection> qubaomingActiveUserCollectionList = null;
        try {
            qubaomingActiveUserCollectionList = qubaomingActiveUserCollectionService.selectQubaomingActiveUserCollectionByExample(example);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        if (qubaomingActiveUserCollectionList != null) {
            pageInfo = new PageInfo(qubaomingActiveUserCollectionList);
        }


        List<String> userAvatars = new ArrayList<>();
        for (QubaomingActiveUserCollection qubaomingActiveUserCollection : qubaomingActiveUserCollectionList) {
            QubaomingWeixinUser qubaomingWeixinUser = qubaomingWeixinUserService.selectByPkId(qubaomingActiveUserCollection.getUserId());
            userAvatars.add(qubaomingWeixinUser.getAvatarUrl());
        }
        pageInfo.setList(userAvatars);
        return pageInfo;
    }
}
