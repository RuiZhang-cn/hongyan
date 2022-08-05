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
    private int maxValueLength=2000;
    private int maxProxyDownloadFileSize=300000;
}
