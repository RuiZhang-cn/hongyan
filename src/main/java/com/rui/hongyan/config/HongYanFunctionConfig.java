package com.rui.hongyan.config;

import cn.hutool.core.codec.Morse;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.text.CharPool;
import cn.hutool.core.util.*;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.rui.hongyan.constants.StringPool;
import com.rui.hongyan.function.HongYanBaseFunction;
import com.rui.hongyan.function.RandomStringFunction;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.rui.hongyan.utils.ParameterUtil.getOrDefault;

/**
 * @author by Rui
 * @Description
 * @Date 2022/07/29 上午 11:14
 */
@Configuration
@Slf4j
public class HongYanFunctionConfig {
    @Autowired
    private HongYanConfig hongYanConfig;

    @Bean(name = {"样例方法"})
    public HongYanBaseFunction sampleMethod() {
        return (request, response) -> {
            return getOrDefault(request.getParameter("参数名"), "默认参数");
        };
    }

    @Bean(name = {"随机字符串","randomString"})
    public RandomStringFunction randomStringFunction() {
        return new RandomStringFunction();
    }

    @Bean(name = {"随机密码","RandomPassWord"})
    public HongYanBaseFunction getRandomPassWord() {
        return (request, response) -> RandomUtil.randomString(RandomUtil.BASE_CHAR_NUMBER + "~!@#$%^&*_+.,",
                getOrDefault(request.getParameter("length"), 10)
        );
    }

    @Bean(name = {"获取请求信息","myreq"})
    public HongYanBaseFunction getMyRequestByRequest() {
        return (request, response) -> {
            JSONObject header = new JSONObject();
            JSONObject root = new JSONObject(4, true);
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerKey = headerNames.nextElement();
                String headerValue = request.getHeader(headerKey);
                header.set(headerKey, headerValue);
            }
            root.set("url", URLDecoder.decode(request.getRequestURL().toString(),StandardCharsets.UTF_8));
            root.set("method", request.getMethod());
            if (request.getContentType()!=null&&request.getContentType().equalsIgnoreCase(ContentType.JSON.getValue())){
                try {
                    String value = new String(IoUtil.readBytes(request.getInputStream(), false), StandardCharsets.UTF_8);
                    // 进行json转换之前 简单的判断是否为json
                    if (JSONUtil.isTypeJSONObject(value)){
                        root.set("body", JSONUtil.parse(value));
                    }else {
                        root.set("body", value);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            root.set("args", request.getParameterMap());
            root.set("header", header);
            return JSONUtil.toJsonPrettyStr(root);
        };
    }

    @Bean(name = {"我的IP", "ip"})
    public HongYanBaseFunction getClientIP() {
        return (request, response) -> ServletUtil.getClientIP(request,"cf-connecting-ip");
    }

    @Bean(name = {"随机数字","RandomInt"})
    public HongYanBaseFunction getRandomInt() {
        return (request, response) -> RandomUtil.randomNumbers(
                getOrDefault(request.getParameter("length"), 10)
        );
    }

    @Bean(name = {"生成二维码","qrcode"})
    public HongYanBaseFunction generateQrCode() {
        return (request, response) -> {
            String url = request.getParameter("url");
            if (StrUtil.isEmpty(url)) {
                return "生成二维码功能需传递参数url例:/生成二维码?url=baidu.com,此外也可以生成文本的二维码";
            }
            try {
                QrCodeUtil.generate(
                        url,
                        getOrDefault(request.getParameter("width"), 300),
                        getOrDefault(request.getParameter("height"), 300),
                        ImgUtil.IMAGE_TYPE_JPEG,
                        response.getOutputStream());
            } catch (IOException e) {
                log.error("生成二维码发生异常:",e);
                return e.getMessage();
            }
            return "";
        };
    }

    @Bean(name = {"摩斯密码","morse"})
    public HongYanBaseFunction morseCoder() {
        return (request, response) -> {
            String text = request.getParameter("text");
            if (text == null) {
                return "摩斯密码功能需传递参数text如 /摩斯密码?text=a,自动识别加密或者解密";
            }
            Morse morse = new Morse();
            char firstChar = text.charAt(0);
            if (firstChar == CharPool.DOT || firstChar == CharPool.DASHED) {
                return morse.decode(text);
            } else {
                return morse.encode(text);
            }
        };
    }

    @Bean(name = {"时间转换","date"})
    public HongYanBaseFunction timestamp() {
        return (request, response) -> {
            JSONObject root = new JSONObject();
            String date = request.getParameter("date");
            if (date == null) {
                long timeMillis = System.currentTimeMillis();
                root.set("当前时间戳(毫秒)", timeMillis);
                root.set("当前时间戳(秒)", timeMillis / 1000);
                root.set("当前时间", new DateTime().toString());
                root.set("介绍", "时间转换功能需要转递date参数自动识别要转换成时间或者时间戳,不传递默认返回当前时间");
                return JSONUtil.toJsonPrettyStr(root);
            }
            if (NumberUtil.isNumber(date)) {
                long parseLong = Long.parseLong(date);
                root.set("时间戳转换后(毫秒)", new DateTime(parseLong).toString());
                root.set("时间戳转换后(秒)", new DateTime(parseLong * 1000).toString());
                return JSONUtil.toJsonPrettyStr(root);
            }
            DateTime dateTime = DateUtil.parse(date);
            root.set("时间转换后(毫秒)", dateTime.getTime());
            root.set("时间转换后(秒)", dateTime.getTime() / 1000);
            return JSONUtil.toJsonPrettyStr(root);
        };
    }

    @Bean(name = {"代理下载","dl"})
    public HongYanBaseFunction proxyDownload() {
        return (request, response) -> {
            String url = request.getParameter("url");
            if (StrUtil.isEmpty(url)) {
                return "代理下载功能,可用于下载github等国外网站的资源,需要传递参数url,例如/代理下载?url=http://baidu.com/1.txt,流量有限,请勿滥用.";
            }
            try {
                long contentLength = HttpRequest.head(url).execute().contentLength();
                if (contentLength > hongYanConfig.getMaxProxyDownloadFileSize()) {
                    return "文件大小为:" + contentLength + "超出限制!";
                }
            } catch (UtilException e) {
                log.error("代理下载获取下载文件大小发生异常:",e);
                return e.getMessage();
            }
            response.setContentType("multipart/form-data");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + url.substring(url.lastIndexOf(CharPool.SLASH)));
            try {
                HttpUtil.download(url, response.getOutputStream(), true);
            } catch (IOException e) {
                log.error("代理下载下载文件发生异常:",e);
                return e.getMessage();
            }
            return "nop";
        };
    }

    @Bean(name = {"上传文件","uploadFile"})
    public HongYanBaseFunction uploadFile() {
        return (request, response) -> {
            if (!JakartaServletUtil.METHOD_POST.equals(request.getMethod())){
                return "此方法只支持POST请求";
            }
            if (!(request instanceof MultipartHttpServletRequest)) {
                return "请求格式错误,仅支持文件上传";
            }
            MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
            Iterator<String> files = mRequest.getFileNames();
            JSONObject resJson = new JSONObject();
            while (files.hasNext()) {
                String next = files.next();
                MultipartFile file = mRequest.getFile(next);
                Assert.notNull(file,"参数file不应为null！");
                String originalFilename = file.getOriginalFilename();
                Assert.notNull(originalFilename,"参数originalFilename不应为null！");
                String suffix = originalFilename.substring(originalFilename.lastIndexOf(StrUtil.DOT));
                String newFileName = IdUtil.fastSimpleUUID().concat(suffix);
                File tempFile = FileUtil.createTempFile(newFileName, null, true);
                try {
                    file.transferTo(tempFile);
                    String resBody = HttpRequest.post(hongYanConfig.getUploadFileUrl() + newFileName)
                            .header("apikey", hongYanConfig.getUploadFileApikey())
                            .header("authorization", "Bearer "+ hongYanConfig.getUploadFileApikey())
                            .form("body",tempFile)
                            .execute()
                            .body();
                    JSONObject entries = JSONUtil.parseObj(resBody);
                    //上传失败
                    if (!entries.containsKey("Key")) {
                        resJson.set(next, entries.toString());
                    }else {
                        resJson.set(next, hongYanConfig.getUploadFilePreviewUrl()+newFileName);
                    }
                } catch (IOException e) {
                    log.error("上传文件发生异常:",e);
                    return e.getMessage();
                }finally {
                    FileUtil.del(tempFile);
                }
            }
            return resJson.toStringPretty();
        };
    }

    @Bean(name = {"重定向","redirect"})
    public HongYanBaseFunction redirect() {
        return (request, response) -> {
            String url = request.getParameter(StringPool.intern("url"));
            if (StrUtil.isEmpty(url)) {
                return "重定向功能需传递参数url,值为要跳转的链接,如?url=baidu.com,携带参数请直接?url=baidu.com?参数1=参数值1&参数2=参数值2";
            }
            Map<String, String[]> parameterMap = request.getParameterMap();
            StringJoiner urlJoiner;
            if (url.contains(StringPool.QUESTION_MARK)){
                urlJoiner = new StringJoiner(StringPool.AMPERSAND, url+StringPool.AMPERSAND, StringPool.EMPTY);
            }else {
                urlJoiner = new StringJoiner(StringPool.AMPERSAND, url+StringPool.QUESTION_MARK, StringPool.EMPTY);
            }
            for (Map.Entry<String, String[]> stringEntry : parameterMap.entrySet()) {
                if (StringPool.intern("url").equals(stringEntry.getKey())){
                    continue;
                }
                urlJoiner.add(stringEntry.getKey()+StringPool.EQUALS+stringEntry.getValue()[0]);
            }
            try {
                response.sendRedirect(urlJoiner.toString());
                return null;
            } catch (IOException e) {
                log.error("重定向功能重定向发生异常:",e);
                return e.getMessage();
            }
        };
    }

    @Bean(name = {"网站信息","getBasicWebsiteInformation"})
    public HongYanBaseFunction getBasicWebsiteInformation(){
        return (request, response) -> {
            String urlStr = request.getParameter(StringPool.intern("url"));
            if (StrUtil.isBlank(urlStr)){
                return "请传递参数?url=";
            }
            if (false == HttpUtil.isHttp(urlStr) && false == HttpUtil.isHttps(urlStr)){
                return "请输入以HTTP或者HTTPS开头的url!";
            }
            final JSONObject res = new JSONObject();
            final URL url = URLUtil.url(urlStr);
            final String simpleUrl = url.getHost() + "/favicon.ico";

            final HttpResponse urlRes = HttpUtil.createGet(urlStr, true)
                    .timeout(2000)
                    .executeAsync();
            if (urlRes.isOk()){
                final String htmlPage = urlRes.body();
                final Document document = Jsoup.parse(htmlPage, urlStr);
                final Element titleElement = document.selectFirst("head > title");
                assert titleElement != null;
                res.set("title", titleElement.text());
                final Element iconElement = document.selectFirst("link[rel=\"shortcut icon\"], link[rel=\"icon\"]");
                if (iconElement == null) {
                    final HttpResponse iconRes = HttpUtil.createGet(simpleUrl, true)
                            .timeout(2000)
                            .executeAsync();
                    if (iconRes.isOk()){
                        final String iconBase64 = Base64.getEncoder().encodeToString(iconRes.bodyBytes());
                        res.set("icon",iconBase64);
                    }
                }else {
                    final String href = iconElement.absUrl("href");
                    if (HttpUtil.isHttps(href) || HttpUtil.isHttp(href)){
                        final byte[] bodyBytes = HttpUtil.createGet(href, true)
                                .timeout(2000)
                                .execute()
                                .bodyBytes();
                        res.set("icon",Base64.getEncoder().encodeToString(bodyBytes));
                    }else {
                        res.set("icon",href);
                    }
                }
            }
            return res.toJSONString(4);
        };
    }
}
