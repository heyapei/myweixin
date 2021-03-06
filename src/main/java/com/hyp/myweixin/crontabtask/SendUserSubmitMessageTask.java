package com.hyp.myweixin.crontabtask;

import com.alibaba.fastjson.JSONObject;
import com.hyp.myweixin.pojo.qubaoming.model.*;
import com.hyp.myweixin.service.qubaoming.*;
import com.hyp.myweixin.service.smallwechatapi.WeixinSmallContentDetectionApiService;
import com.hyp.myweixin.utils.dateutil.MyDateUtil;
import com.hyp.myweixin.utils.redis.MyRedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/10/15 20:57
 * @Description: TODO 执行发送数据
 */
@Component
@Slf4j
@PropertySource("classpath:crontab-task-auto.properties")
@PropertySource("classpath:wechat.properties")
@PropertySource("classpath:redis-key.properties")
public class SendUserSubmitMessageTask {

    /**
     * 用户报名成功模板
     */
    @Value("${weixin.message.template.qubaoming.baoming.success.notify}")
    private String qubaoming_success_notify_template;

    /**
     * 活动开始通知模板
     */
    @Value("${weixin.message.template.qubaoming.active.start.notify}")
    private String qubaoming_active_start_template;


    @Autowired
    private UserSubmitMessageService userSubmitMessageService;


    @Autowired
    private QubaomingActiveBaseService qubaomingActiveBaseService;


    @Autowired
    private QuBaoMingActiveConfigService quBaoMingActiveConfigService;


    @Autowired
    private MyRedisUtil myRedisUtil;

    /**
     * 活动开始时间的时间戳
     */
    @Value("${redis.key.qubaoming.active.start.time}")
    private String REDIS_KEY_QUBAOMING_ACTIVE_START_TIME;
    /**
     * 活动开始时间的时间戳过期时间
     */
    @Value("${redis.key.qubaoming.active.start.time.expires.time}")
    private Long ACTIVE_START_TIME_REDIS_EXPIRE;


    @Autowired
    private WeixinSmallContentDetectionApiService weixinSmallContentDetectionApiService;

    @Autowired
    private QubaomingWeixinUserService qubaomingWeixinUserService;

    @Autowired
    private WechatCompanyService wechatCompanyService;


    /**
     * 活动开始前通知模板
     */
    @Scheduled(cron = "${crontab.task.loop.minute.thirty}")
    public void asyncFeedbackFromWeixin() {

        log.info("活动开始前通知模板，每三十分钟执行一次，发送用户通知的定时任务redis的key" + REDIS_KEY_QUBAOMING_ACTIVE_START_TIME);
        Example example = new Example(QubaomingMessageSubmit.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("messageId", qubaoming_active_start_template);
        criteria.andEqualTo("status", QubaomingMessageSubmit.StatusEnum.UN_SEND.getCode());
        List<QubaomingMessageSubmit> qubaomingMessageSubmitByExample = userSubmitMessageService.getQubaomingMessageSubmitByExample(example);
        if (qubaomingMessageSubmitByExample != null) {

            log.info("查找到待发送的数据：{}", qubaomingMessageSubmitByExample.toString());

            for (QubaomingMessageSubmit qubaomingMessageSubmit : qubaomingMessageSubmitByExample) {

                String startTime = (String) myRedisUtil.get(REDIS_KEY_QUBAOMING_ACTIVE_START_TIME +
                        qubaomingMessageSubmit.getActiveId());
                if (startTime != null) {

                    log.info("从redis查找出来开始时间：{}", startTime);

                    boolean b = twoHourRange(Long.parseLong(startTime));
                    if (b) {
                        QubaomingWeixinUser qubaomingWeixinUser = qubaomingWeixinUserService.selectByPkId(qubaomingMessageSubmit.getUserId());
                        QubaomingActiveConfig qubaomingActiveConfig = quBaoMingActiveConfigService.selectOneByActiveId(qubaomingMessageSubmit.getActiveId());
                        QubaomingActiveBase qubaomingActiveBase = qubaomingActiveBaseService.selectByPkId(qubaomingMessageSubmit.getActiveId());
                        List<WechatCompany> wechatCompanies = wechatCompanyService.selectListByUserId(qubaomingActiveBase.getActiveUserId());
                        if (qubaomingWeixinUser == null || qubaomingActiveConfig == null || qubaomingActiveBase == null || wechatCompanies == null || wechatCompanies.size() <= 0) {
                            continue;
                        }
                        sendTemplateMessage(qubaomingWeixinUser.getOpenId(),
                                qubaomingActiveBase.getActiveName(), MyDateUtil.numberDateFormat(String.valueOf(qubaomingActiveConfig.getActiveStartTime()), "yyyy-MM-dd HH:mm"),
                                qubaomingActiveConfig.getActiveAddress(), wechatCompanies.get(0).getCompanyName(), "活动快要开始了，请您抓紧时间参加活动");

                        qubaomingMessageSubmit.setStatus(QubaomingMessageSubmit.StatusEnum.SUCCESS.getCode());
                        qubaomingMessageSubmit.setSendTime(new Date());
                        userSubmitMessageService.updateQubaomingMessageSubmit(qubaomingMessageSubmit);
                    }
                } else {
                    QubaomingActiveConfig qubaomingActiveConfig = quBaoMingActiveConfigService.selectOneByActiveId(qubaomingMessageSubmit.getActiveId());
                    if (qubaomingActiveConfig != null) {

                        log.info("从数据库中查找出来的活动配置：{}", qubaomingActiveConfig.toString());


                        myRedisUtil.set(REDIS_KEY_QUBAOMING_ACTIVE_START_TIME +
                                        qubaomingMessageSubmit.getActiveId(),
                                qubaomingActiveConfig.getActiveStartTime(), ACTIVE_START_TIME_REDIS_EXPIRE);

                    }
                    boolean b = twoHourRange(qubaomingActiveConfig.getActiveStartTime());
                    if (b) {
                        QubaomingWeixinUser qubaomingWeixinUser = qubaomingWeixinUserService.selectByPkId(qubaomingMessageSubmit.getUserId());
                        QubaomingActiveBase qubaomingActiveBase = qubaomingActiveBaseService.selectByPkId(qubaomingMessageSubmit.getActiveId());
                        List<WechatCompany> wechatCompanies = wechatCompanyService.selectListByUserId(qubaomingActiveBase.getActiveUserId());
                        if (qubaomingWeixinUser == null || qubaomingActiveConfig == null || qubaomingActiveBase == null || wechatCompanies == null || wechatCompanies.size() <= 0) {
                            continue;
                        }
                        sendTemplateMessage(qubaomingWeixinUser.getOpenId(),
                                qubaomingActiveBase.getActiveName(), MyDateUtil.numberDateFormat(String.valueOf(qubaomingActiveConfig.getActiveStartTime()), "yyyy-MM-dd HH:mm"),
                                qubaomingActiveConfig.getActiveAddress(), wechatCompanies.get(0).getCompanyName(), "温馨提示：请提前10-15分钟进场签到");

                        qubaomingMessageSubmit.setStatus(QubaomingMessageSubmit.StatusEnum.SUCCESS.getCode());
                        qubaomingMessageSubmit.setSendTime(new Date());
                        userSubmitMessageService.updateQubaomingMessageSubmit(qubaomingMessageSubmit);
                    }

                }
            }
        }


        log.info("执行任务：{}", new Date() + "==" + qubaoming_active_start_template);
    }

    private JSONObject sendTemplateMessage(String openId, String activeName, String startTime,
                                           String activeAddress
            , String companyName, String tip) {


        if (StringUtils.isNotBlank(activeName)) {
            int length = 20;
            if (activeName.length() >= length) {
                activeName = activeName.substring(0, length-1);
            }
        } else {
            activeName = "";
        }

        if (StringUtils.isNotBlank(activeAddress)) {
            int length = 20;
            if (activeAddress.length() >= length) {
                activeAddress = activeAddress.substring(0, length-1);
            }
        } else {
            activeAddress = "";
        }

        if (StringUtils.isNotBlank(companyName)) {
            int length = 20;
            if (companyName.length() >= length) {
                companyName = companyName.substring(0, length-1);
            }
        } else {
            companyName = "";
        }

        if (StringUtils.isNotBlank(tip)) {
            int length = 20;
            if (tip.length() >= length) {
                tip = tip.substring(0, length-1);
            }
        } else {
            tip = "";
        }


        String jsonString = "{\n" +
                "\t\"touser\": \"" + openId + "\",\n" +
                "\t\"data\": {\n" +
                "\t\t\"thing4\": {\n" +
                "\t\t\t\"value\": \"" + activeName + "\"\n" +
                "\t\t},\n" +
                "\t\t\"date3\": {\n" +
                "\t\t\t\"value\": \"" + startTime + "\"\n" +
                "\t\t},\n" +
                "\t\t  \"thing6\": {\n" +
                "\t\t\t\"value\": \"" + activeAddress + "\"\n" +
                "\t\t},\n" +
                "\t\t\"thing11\": {\n" +
                "\t\t\t\"value\": \"" + companyName + "\"\n" +
                "\t\t},\n" +
                "\t\t\"thing7\": {\n" +
                "\t\t\t\"value\": \"" + tip + "\"\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t\"template_id\": \"" + qubaoming_active_start_template + "\",\n" +
                "\t\"miniprogram_state\": \"formal\",\n" +
                "\t\"page\": \"pages/index/index\",\n" +
                "\t\"lang\": \"zh_CN\"\n" +
                "}";

        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        JSONObject accessToken = weixinSmallContentDetectionApiService.
                sendQuBaoMingUserSubmitMessage(jsonObject);
        log.info("定时任务发送信息时的json参数：{}，发送结果：{}", jsonObject.toJSONString(), accessToken);

        return accessToken;
    }


    /**
     * 判断传过来的时间戳在一小时范围内
     *
     * @param timestamp
     * @return
     */
    private boolean twoHourRange(Long timestamp) {
        Date date = MyDateUtil.addHour(new Date(), 2);
        String s = MyDateUtil.numberDateFormatToDate(date, 13);
        long l = Long.parseLong(s);
        if (l >= timestamp) {
            return true;
        }
        return false;
    }

}
