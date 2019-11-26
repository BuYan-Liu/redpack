package com.zhenglong.aspect;

import com.zhenglong.redpack.entity.Result;
import com.zhenglong.util.ResponseUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Lion
 * @描述
 * @date 2019/5/22
 */
@Aspect
@Configuration
public class LoginAspect {
    private final String ControllerPonit = "execution(* com.zhenglong.redpack.controller.*.*(..))";

    //定义切入点,拦截servie包其子包下的所有类的所有方法
//    @Pointcut("execution(* com.haiyang.onlinejava.complier.service..*.*(..))")
    //拦截指定的方法,这里指只拦截TestService.getResultData这个方法
    @Pointcut(ControllerPonit)
    public void excuteController() {

    }

    //环绕通知
    @Around("execution(* com.zhenglong.redpack.controller.*.*(..))")
    public Result aroundLog(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("AOP");
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token=request.getHeader("token");
        System.out.println(token);
        return ResponseUtil.error("未登录");
    }

}
