package com.hyp.myweixin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.concurrent.Executor;

/**
 * @author heyapei
 * @EnableAsync 开启异步
 * @ComponentScan扫描 所有需要的包, 包含一些自用的工具类包 所在的路径
 * 在入口类使用注解@EnableTransactionManagement开启事务：
 * @EnableScheduling 开启定时执行的任务
 */
@SpringBootApplication
//扫描 mybatis mapper 包路径
@MapperScan(basePackages = "com.hyp.myweixin.mapper")
@ComponentScan(basePackages = {"com.hyp"})
@EnableAsync
@EnableTransactionManagement
@EnableScheduling
public class MyweixinApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyweixinApplication.class, args);
    }

    @Bean(name = "threadPoolTaskExecutor")
    public Executor threadPoolTaskExecutor() {
        return new ThreadPoolTaskExecutor();
    }

}
