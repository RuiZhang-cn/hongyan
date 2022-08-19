package com.rui.hongyan.config;

import com.rui.hongyan.filters.KeyBloomFilter;
import com.rui.hongyan.modules.hongyanmaptable.service.IHongyanMapTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author by Rui
 * @Description
 * @Date 2022/08/19 下午 02:40
 */
@Configuration
public class KeyBloomFilterConfig {
    @Autowired
    private HongYanConfig hongYanConfig;

    @Bean
    public KeyBloomFilter keyBloomFilter(IHongyanMapTableService hongyanMapTableService){
        long count = hongyanMapTableService.count();
        KeyBloomFilter keyBloomFilter = new KeyBloomFilter(Math.max(Math.toIntExact(count), hongYanConfig.getBloomFilterMinLength()), hongYanConfig.getBloomFilterKeyHashTimes());
        hongyanMapTableService.fullScanAllKey(keyBloomFilter::add);
        return keyBloomFilter;
    }
}
