package com.itluchao.reggie.filter;

import java.io.IOException;

import com.alibaba.fastjson.JSON;
import com.itluchao.reggie.entity.R;
import com.itluchao.reggie.exception.BaseContext;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter("/*") // 对所有请求进行拦截
public class LoginCheckFilter implements Filter {
    // 对没有登录的进行拦截
    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
            throws IOException, ServletException {
        // 获取url
        HttpServletRequest request = (HttpServletRequest) arg0;
        HttpServletResponse response = (HttpServletResponse) arg1;
        String url = request.getRequestURL().toString();

        String[] strs = { "/login", "/logout", "/sendMsg" };

        // 如果url包括/login和/logout就放行
        for (String str : strs) {
            if (url.contains(str) ) {
                arg2.doFilter(arg0, arg1);
                return;
            }
        }

        // 判断session里有没有
        if (request.getSession().getAttribute("employee") != null) {

            // 拿到id
            Long EmpId = (long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(EmpId);
            arg2.doFilter(arg0, arg1);
            return;
        }
        //移动端
        if (request.getSession().getAttribute("user") != null) {

            // 拿到id
            Long EmpId = (long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(EmpId);
            arg2.doFilter(arg0, arg1);
            return;
        }

        // 未登录状态，回写数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
    }

}
