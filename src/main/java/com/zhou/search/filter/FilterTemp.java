package com.zhou.search.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@WebFilter(urlPatterns = "/*", filterName = "testFilter")
@Slf4j
public class FilterTemp implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
        log.info("过滤器0的init方法");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws ServletException, IOException {
        long start = System.currentTimeMillis();
        log.info("Filter0 Execute cost=" + (System.currentTimeMillis() - start));
        /**
         * 解决跨域和预请求
         */
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        resp.setHeader("Access-Control-Allow-Origin", "*");
        // 允许所有的请求
        // resp.setHeader("Access-Control-Allow-Origin", "*");
        // 跨域 Header
        resp.setHeader("Access-Control-Allow-Methods", "*");
        resp.setHeader("Access-Control-Allow-Headers", "*");
        // 预请求
        resp.setHeader("Access-Control-Max-Age", "3600");
        filterChain.doFilter(req, resp);
        log.info("Filter0之后执行");
    }

    @Override
    public void destroy() {
        log.info("Filter0的destroy方法");
    }
}