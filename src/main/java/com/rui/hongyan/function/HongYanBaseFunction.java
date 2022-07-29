package com.rui.hongyan.function;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.function.BiFunction;

/**
 * @Description
 * @Date 2022/07/29 上午 09:44
 * @author by Rui
 */
public interface HongYanBaseFunction extends BiFunction<HttpServletRequest,HttpServletResponse,String> {

}
