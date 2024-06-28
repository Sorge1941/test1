package com.itluchao.reggie.Config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.MimeMappings.Mapping;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import com.itluchao.reggie.exception.JacksonObjectMapper;
import com.itluchao.reggie.filter.Interceptor;

@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {



                registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");

    }

    // 重写消息转换器
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 创建自定义的消息转换器
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        // 设置对象映射器，底层使用jackson将Java对象转为json，或者将json转为Java对象
        converter.setObjectMapper(new JacksonObjectMapper());
        // 添加到消息转换器中
        converters.add(0, converter);

    }
          @Autowired
     Interceptor loginInterceptor;
@Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**") // 拦截所有请求
                .excludePathPatterns("/employee/login", "/employee/logout", "/backend/**", "/front/**", "/user/logout","/user/login"); // 排除不需要拦截的路径
    }  
}