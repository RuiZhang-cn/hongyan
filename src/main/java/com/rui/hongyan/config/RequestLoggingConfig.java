package com.rui.hongyan.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.AbstractRequestLoggingFilter;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;

/**
 * Documentation as Code
 * if it works, don't touch
 *
 * @author rui
 * @since 2025/2/17 上午10:36
 */
@Configuration
public class RequestLoggingConfig {
    @Bean
    public AbstractRequestLoggingFilter logFilter() {
        AbstractRequestLoggingFilter filter = new AbstractRequestLoggingFilter() {
            @Override
            protected void beforeRequest(HttpServletRequest request, String message) {
                logger.info(message);
            }
            @Override
            protected void afterRequest(HttpServletRequest request, String message) {

            }
        };
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setIncludeHeaders(true);
        filter.setIncludeClientInfo(true);
        filter.setMaxPayloadLength(1024);
        filter.setAfterMessagePrefix("REQUEST DATA-");
        return filter;
    }
}