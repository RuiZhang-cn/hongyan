package com.rui.hongyan.filters;

import cn.hutool.bloomfilter.BitSetBloomFilter;
import cn.hutool.bloomfilter.BloomFilter;
import cn.hutool.bloomfilter.BloomFilterUtil;
import cn.hutool.system.SystemUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.ref.SoftReference;
import java.util.List;
import java.util.Optional;

/**
 * @author by Rui
 * @Description
 * @Date 2022/08/18 下午 04:48
 */
@Slf4j
public class KeyBloomFilter implements BloomFilter{
    private final SoftReference<BloomFilter> bloomFilter;

    public KeyBloomFilter(int size,int keyHashTimes) {
        bloomFilter= new SoftReference<>(BloomFilterUtil.createBitSet(size << 1, size, keyHashTimes));
    }



    @Override
    public boolean contains(String str) {
        return Optional.ofNullable(bloomFilter.get()).map(e -> e.contains(str)).orElseGet(() -> {
            log.error("内存不足,布隆过滤器已强制停用,内存信息为:{}", SystemUtil.getRuntimeInfo().toString());
            return false;
        });
    }

    @Override
    public boolean add(String str) {
        return Optional.ofNullable(bloomFilter.get()).map(e -> e.add(str)).orElseGet(() -> {
            log.error("内存不足,布隆过滤器已强制停用,内存信息为:{}", SystemUtil.getRuntimeInfo().toString());
            return true;
        });
    }

}
