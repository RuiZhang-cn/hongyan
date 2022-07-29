package com.rui.hongyan.modules.hongyanmaptable.service.impl;

import cn.hutool.core.math.MathUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.rui.hongyan.function.HongYanBaseFunction;
import com.rui.hongyan.modules.hongyanmaptable.service.IDynamicMethodService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

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
