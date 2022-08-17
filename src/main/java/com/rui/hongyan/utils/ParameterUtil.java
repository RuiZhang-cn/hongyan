package com.rui.hongyan.utils;

import cn.hutool.core.convert.Convert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Optional;

/**
 * @author by Rui
 * @Description
 * @Date 2022/07/29 下午 11:35
 */
@Slf4j
public class ParameterUtil{

    /**
     * 将Object转换为defaultValue的类型
     * defaultValue如果是基础类型会进行自动装箱
     * 如果是数组转字符串会转换成[0, 1]的字符串
     * 转其他的会报错
     * @param t 源
     * @param defaultValue 默认值
     * @return 转换为defaultValue的t
     * @param <T>
     */
    public static <T> T getOrDefault(Object t,@NonNull T defaultValue){
        return Convert.convert(defaultValue.getClass(), t, defaultValue);
    }
    public static <T> T getOrDefaultByRequest(HttpServletRequest request,String parameterName,T defaultValue){
        return getOrDefault(
                Optional.ofNullable(request.getAttribute(parameterName))
                        .orElseGet(()->request.getParameter(parameterName))
                ,defaultValue);
    }
}
