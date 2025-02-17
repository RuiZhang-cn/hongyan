package com.rui.hongyan.aspect;

import com.rui.hongyan.function.HongYanBaseFunction;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author by Rui
 * @Description
 * @Date 2022/08/22 下午 12:11
 */
@Aspect
@Component
@Slf4j
public class ControllerLogAspect {

    @Pointcut("execution(public * com.rui.hongyan.modules.hongyanmaptable.controller..*.*(..))")
    public void controllerPointcut() {
    }

    @AfterReturning(returning = "res",pointcut = "controllerPointcut()")
    public void resLog(String res){
        log.info("用户请求返回,返回信息为:{}",res);
    }
}
