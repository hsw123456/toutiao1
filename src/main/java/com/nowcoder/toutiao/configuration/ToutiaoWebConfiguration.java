package com.nowcoder.toutiao.configuration;

import com.nowcoder.toutiao.Interceptor.LoginRequiredInterceptor;
import com.nowcoder.toutiao.Interceptor.PassPortInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by hsw on 2017/5/25.
 */

/**
 * 注册拦截器
 */
@Component
public class ToutiaoWebConfiguration extends WebMvcConfigurerAdapter{
    @Autowired
    PassPortInterceptor passPortInterceptor;
    @Autowired
    LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //是否登录的
        registry.addInterceptor(passPortInterceptor);
        //是否有某个权限
        registry.addInterceptor(loginRequiredInterceptor).addPathPatterns("/setting*");
        super.addInterceptors(registry);
    }
}

