package com.hyp.myweixin.crontabtask;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/12 0:56
 * @Description: TODO
 */
@Component
@Slf4j
@PropertySource("classpath:crontab-task-auto.properties")
public class AsyncFeedbackFromWeixin {


    /**
     * 测试
     * 每五秒钟执行一次
     */
    //@Scheduled(cron = "${crontab.task.loop.second.five}")
    public void asyncFeedbackFromWeixin() {
        log.info("执行任务：{}", new Date());
    }
}
