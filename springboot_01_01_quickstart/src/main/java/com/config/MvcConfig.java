package com.config;

import com.Interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
*   一个配置类
*   作用：用以添加拦截器
*   注意：@Configuration注解必须要添加，不添加注解不会加载到配置中 拦截器不会生效
* */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /*
    * 添加拦截器
    * */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截器的排除路径
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns(
                        "/shop/**",
                        "/voucher/**",
                        "/shop-type/**",
                        "/upload/**",
                        "/blog/hot",
                        "/user/code",
                        "/user/login"
                );
    }
}
