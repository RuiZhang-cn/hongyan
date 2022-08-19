package com.rui.hongyan.modules.hongyanmaptable.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rui.hongyan.modules.hongyanmaptable.entity.HongyanMapTable;
import com.rui.hongyan.modules.hongyanmaptable.mapper.HongyanMapTableMapper;
import com.rui.hongyan.modules.hongyanmaptable.service.IHongyanMapTableService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cursor.Cursor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * @Classname HongyanMapTableServiceImpl
 * @Description
 * @Date 2022/07/26 下午 05:14
 * @author by Rui
 */
@Service
@Slf4j
public class HongyanMapTableServiceImpl extends ServiceImpl<HongyanMapTableMapper, HongyanMapTable> implements IHongyanMapTableService {


    @Override
    @Transactional(readOnly = true,propagation = Propagation.NOT_SUPPORTED)
    public void fullScanAllKey(Consumer<String> consume) {
        try (Cursor<String> cursor = baseMapper.fullScanAllKey()){
            cursor.forEach(consume);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
