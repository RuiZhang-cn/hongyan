package com.rui.hongyan.config;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.rui.hongyan.function.HongYanBaseFunction;
import com.rui.hongyan.function.RandomStringFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Optional;

/**
 * @author by Rui
 * @Description
 * @Date 2022/07/29 上午 11:14
 */
@Configuration
public class HongYanFunctionConfig {

    @Bean(name = {"randomString","getRandomString"})
    public RandomStringFunction randomStringFunction(){
        return new RandomStringFunction();
    }

    @Bean(name = {"getRandomPassWord","随机密码"})
    public HongYanBaseFunction getRandomPassWord(){
        return (request, response) -> RandomUtil.randomString(RandomUtil.BASE_CHAR_NUMBER+"~!@#$%^&*_+.,",
                Optional.ofNullable(request.getParameter("length"))
                        .map(Integer::parseInt)
                        .orElse(10));
    }

    @Bean(name = {"getMyRequestByRequest"})
    public HongYanBaseFunction getMyRequestByRequest(){
        return (request, response) -> {
            JSONObject header = new JSONObject();
            JSONObject root = new JSONObject(3,true);
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()){
                String headerKey = headerNames.nextElement();
                String headerValue = request.getHeader(headerKey);
                header.set(headerKey,headerValue);
            }
            root.set("url", request.getRequestURL());
            root.set("args", request.getParameterMap());
            root.set("header", header);
            return JSONUtil.toJsonPrettyStr(root);
        };
    }

    @Bean(name = {"getClientIP"})
    public HongYanBaseFunction getClientIP(){
        return (request, response) ->ServletUtil.getClientIP(request);
    }

    @Bean(name = {"getRandomInt"})
    public HongYanBaseFunction getRandomInt(){
        return (request, response) ->RandomUtil.randomNumbers(
                Optional.ofNullable(request.getParameter("length"))
                        .map(Integer::parseInt)
                        .orElse(10)
        );
    }

}
