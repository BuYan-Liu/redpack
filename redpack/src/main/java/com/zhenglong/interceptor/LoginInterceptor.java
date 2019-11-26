package com.zhenglong.interceptor;

import com.alibaba.fastjson.JSON;
import com.zhenglong.redpack.service.AdminService;
import com.zhenglong.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Lion
 * @描述
 * @date 2019/5/22
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    AdminService adminService;

    @CrossOrigin
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token=request.getHeader("token");
        if (token!=null && !token.isEmpty()) {
            if (adminService.getToken(token)) {
                return true;
            }
        }
        String origin = request.getHeader("Origin");
        response.setHeader("Access-Control-Allow-Origin", origin);
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Headers","Origin,Content-Type,Accept,token,X-Requested-With");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(JSON.toJSONString(ResponseUtil.unlogin()));
        return false;
    }
}
