package com.itluchao.reggie.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.alibaba.fastjson.JSON;
import com.itluchao.reggie.entity.R;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//拦截器
@Component
public class Interceptor implements HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求头中的token
        Long token = (Long) request.getSession().getAttribute("employee");
        Long user = (Long) request.getSession().getAttribute("user");
        //判断token是否为空
        if(token == null && user == null){
            //如果为空，返回false，不放行
            response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
            return false;
        }
        //如果不为空，返回true，放行
        return true;
    }


}
