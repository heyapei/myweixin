package com.hyp.myweixin.crontabtask;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Auther: YangTao
 * @Date: 2019/2/21 0021
 * 配置类，解决定时任务无法注入的问题
 */
@Component
public class ApplicationContextForTaskUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationContextForTaskUtil.applicationContext = applicationContext;

    }


    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }
}