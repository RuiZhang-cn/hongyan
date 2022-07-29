package com.rui.hongyan.modules.hongyanmaptable.service;

import com.rui.hongyan.function.HongYanBaseFunction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Classname IDynamicMethodService
 * @Description
 * @Date 2022/07/27 下午 05:00
 * @author by Rui
 */
public interface IDynamicMethodService {
    String executeProcessing(HongYanBaseFunction hongYanBaseFunction,HttpServletRequest request,HttpServletResponse response);
}
