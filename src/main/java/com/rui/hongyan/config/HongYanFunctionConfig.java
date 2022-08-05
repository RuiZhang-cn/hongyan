package com.rui.hongyan.config;

import cn.hutool.core.codec.Morse;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.text.CharPool;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.rui.hongyan.function.HongYanBaseFunction;
import com.rui.hongyan.function.RandomStringFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

import static com.rui.hongyan.utils.OptionalUtil.getOrDefault;

/**
 * @author by Rui
 * @Description
 * @Date 2022/07/29 上午 11:14
 */
@Configuration
public class HongYanFunctionConfig {
    @Autowired
    private HongYanConfig hongYanConfig;

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
                return "生成二维码功能需传递参数url例:/生成二维码?url=baidu.com,此外也可以生成文本的二维码";
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
                return "摩斯密码功能需传递参数text如 /摩斯密码?text=a,自动识别加密或者解密";
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
                root.set("介绍", "时间戳转换功能需要转递date参数自动识别要转换成时间或者时间戳,不传递默认返回当前时间");
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

    @Bean(name = {"代理下载"})
    public HongYanBaseFunction proxyDownload(){
        return (request, response) ->{
            String url = request.getParameter("url");
            if (StrUtil.isEmpty(url)){
                return "代理下载功能,可用于下载github等国外网站的资源,需要传递参数url,例如/代理下载?url=http://baidu.com/1.txt,流量有限,请勿滥用.";
            }
            try {
                URL http = URLUtil.toUrlForHttp(url);
                long contentLength = HttpRequest.head(url).execute().contentLength();
                if (contentLength>hongYanConfig.getMaxProxyDownloadFileSize()){
                    return "文件大小为:"+contentLength + "超出限制!";
                }
            } catch (UtilException e) {
                return e.getMessage();
            }
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename="+url.substring(url.lastIndexOf("/")));
            try {
                HttpUtil.download(url,response.getOutputStream(), true);
            } catch (IOException e) {
                return e.getMessage();
            }
            return "nop";
        };
    }
}
