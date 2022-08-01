package com.rui.hongyan.config;

import cn.hutool.core.codec.Morse;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.text.CharPool;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.rui.hongyan.function.HongYanBaseFunction;
import com.rui.hongyan.function.RandomStringFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Enumeration;

import static com.rui.hongyan.utils.OptionalUtil.getOrDefault;

/**
 * @author by Rui
 * @Description
 * @Date 2022/07/29 上午 11:14
 */
@Configuration
public class HongYanFunctionConfig {

    @Bean(name = {"样例方法"})
    public HongYanBaseFunction sampleMethod(){
        return (request, response) ->{
            return getOrDefault(request.getParameter("参数名"), "默认参数");
        };
    }

    @Bean(name = {"randomString","随机字符串"})
    public RandomStringFunction randomStringFunction(){
        return new RandomStringFunction();
    }

    @Bean(name = {"getRandomPassWord","随机密码"})
    public HongYanBaseFunction getRandomPassWord(){
        return (request, response) -> RandomUtil.randomString(RandomUtil.BASE_CHAR_NUMBER+"~!@#$%^&*_+.,",
                getOrDefault(request.getParameter("length"),10)
        );
    }

    @Bean(name = {"获取请求信息"})
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

    @Bean(name = {"我的IP","ip"})
    public HongYanBaseFunction getClientIP(){
        return (request, response) ->ServletUtil.getClientIP(request);
    }

    @Bean(name = {"随机数字"})
    public HongYanBaseFunction getRandomInt(){
        return (request, response) ->RandomUtil.randomNumbers(
                getOrDefault(request.getParameter("length"),10)
        );
    }

    @Bean(name = {"生成二维码"})
    public HongYanBaseFunction generateQrCode(){
        return (request, response) ->{
            String url = request.getParameter("url");
            if (StrUtil.isEmpty(url)){
                return "url参数不能为空！";
            }
            try {
                QrCodeUtil.generate(
                        url,
                        getOrDefault(request.getParameter("width"),300),
                        getOrDefault(request.getParameter("height"),300),
                        ImgUtil.IMAGE_TYPE_JPEG,
                        response.getOutputStream());
            } catch (IOException e) {
                return e.getMessage();
            }
            return "";
        };
    }

    @Bean(name = {"摩斯密码"})
    public HongYanBaseFunction morseCoder(){
        return (request, response) ->{
            String text = request.getParameter("text");
            if (text == null) {
                return "参数text不能为空";
            }
            Morse morse = new Morse();
            char firstChar = text.charAt(0);
            if (firstChar == CharPool.DOT|| firstChar ==CharPool.DASHED){
                return morse.decode(text);
            }else {
                return morse.encode(text);
            }
        };
    }

    @Bean(name = {"时间戳转换"})
    public HongYanBaseFunction timestamp(){
        return (request, response) ->{
            JSONObject root = new JSONObject();
            String date = request.getParameter("date");
            if (date==null){
                long timeMillis = System.currentTimeMillis();
                root.set("当前时间戳(毫秒)", timeMillis);
                root.set("当前时间戳(秒)", timeMillis/1000);
                root.set("当前时间", new DateTime().toString());
                return JSONUtil.toJsonPrettyStr(root);
            }
            if (NumberUtil.isNumber(date)) {
                long parseLong = Long.parseLong(date);
                root.set("时间戳转换后(毫秒)", new DateTime(parseLong).toString());
                root.set("时间戳转换后(秒)", new DateTime(parseLong*1000).toString());
                return JSONUtil.toJsonPrettyStr(root);
            }
            DateTime dateTime = DateUtil.parse(date);
            root.set("时间转换后(毫秒)", dateTime.getTime());
            root.set("时间转换后(秒)", dateTime.getTime()/1000);
            return JSONUtil.toJsonPrettyStr(root);
        };
    }
}
