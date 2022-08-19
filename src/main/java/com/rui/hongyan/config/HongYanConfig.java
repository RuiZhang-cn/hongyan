package com.rui.hongyan.config;

/**
 * @author by Rui
 * @Description
 * @Date 2022/08/04 下午 06:04
 */

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "hongyan")
@Component
@Data
public class HongYanConfig {
    private int maxKeyLength=20;
    private int bloomFilterMinLength=100000;
    private int bloomFilterKeyHashTimes=4;
    private int maxValueLength=2000;
    private int maxProxyDownloadFileSize=300000;
    private String uploadFileUrl="https://c6cgg225g6h1m0ol6ocg.baseapi.memfiredb.com/storage/v1/object/randomchat/";
    private String uploadFilePreviewUrl="https://c6cgg225g6h1m0ol6ocg.baseapi.memfiredb.com/storage/v1/object/public/randomchat/";
    private String uploadFileApikey="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoic2VydmljZV9yb2xlIiwiZXhwIjozMTc1MzM5MDI2LCJpYXQiOjE2Mzc0MTkwMjYsImlzcyI6InN1cGFiYXNlIn0.XACnBFPeQVwXJvTamLFywWzsnwonq7nV9xDZGOrzM1w";
}
