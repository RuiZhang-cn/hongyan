package com.rui.hongyan.modules.hongyanmaptable.service.impl;

import com.rui.hongyan.function.HongYanBaseFunction;
import com.rui.hongyan.modules.hongyanmaptable.service.IDynamicMethodService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Classname DynamicMethodServiceImpl
 * @Description
 * @Date 2022/07/27 下午 05:00
 * @author by Rui
 */
@Service
public class DynamicMethodServiceImpl implements IDynamicMethodService {
    @Override
    public String executeProcessing(HongYanBaseFunction hongYanBaseFunction,HttpServletRequest request,HttpServletResponse response) {
        return hongYanBaseFunction.apply(request, response);
    }
}
