package com.rui.hongyan.function;

import cn.hutool.core.util.RandomUtil;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * @author by Rui
 * @Description
 * @Date 2022/07/29 上午 10:46
 */

public class RandomStringFunction implements HongYanBaseFunction{
    @Override
    public String apply(HttpServletRequest request, HttpServletResponse response) {
        return RandomUtil.randomString(
                Optional.ofNullable(request.getParameter("length"))
                        .map(Integer::parseInt)
                        .orElse(10)
        );
    }
}
