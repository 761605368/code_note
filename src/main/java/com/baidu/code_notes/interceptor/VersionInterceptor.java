package com.baidu.code_notes.interceptor;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 版本拦截器
 * @author lxh
 * @date 2022/3/6 20:36
 */
@Component
public class VersionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String version = request.getHeader("version");
        if ("1.0.0".equals(version)) {
            return true;
        }

        throw new RuntimeException("版本不对111");
    }
}
