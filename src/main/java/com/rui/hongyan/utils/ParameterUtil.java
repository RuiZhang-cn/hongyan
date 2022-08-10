package com.rui.hongyan.utils;

import cn.hutool.core.convert.Convert;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Optional;

/**
 * @author by Rui
 * @Description
 * @Date 2022/07/29 下午 11:35
 */
public class ParameterUtil{
    public static <T> T getOrDefault(Object t, T defaultValue){
        return Convert.convert(defaultValue.getClass(), t, defaultValue);
    }
    public static <T> T getOrDefaultByRequest(HttpServletRequest request,String parameterName,T defaultValue){
        return getOrDefault(request.getParameter(parameterName),defaultValue);
    }
}
