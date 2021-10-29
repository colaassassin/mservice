package com.gateway.ssogateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 用于配置不需要保护的资源路径
 * Created by macro on 2018/11/5.
 */
@Component
@ConfigurationProperties(prefix ="secure.ignore")
public class IgnoreUrlsConfig {
    private List<String> urls;

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}
