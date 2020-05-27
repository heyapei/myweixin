package com.hyp.myweixin;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * @author 何亚培
 * @version V1.0
 * @Date 2020/4/14 17:39
 * @Description: TODO
 */
public class SpringTomcat extends SpringBootServletInitializer {
    /**
     * 修改启动类，继承 SpringBootServletInitializer 并重写 configure 方法
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 注意这里要指向原先用main方法执行的Application启动类
        return builder.sources(MyweixinApplication.class);
    }
}
