package com.rui.hongyan.utils;

import cn.hutool.core.convert.Convert;

import java.io.Serializable;
import java.util.Optional;

/**
 * @author by Rui
 * @Description
 * @Date 2022/07/29 下午 11:35
 */
public class OptionalUtil{
    public static <T> T getOrDefault(Object t, T u){
        return Convert.convert(u.getClass(), t, u);
    }


}
