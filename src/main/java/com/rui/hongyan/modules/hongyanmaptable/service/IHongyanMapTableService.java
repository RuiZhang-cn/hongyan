package com.rui.hongyan.modules.hongyanmaptable.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rui.hongyan.modules.hongyanmaptable.entity.HongyanMapTable;
import org.apache.ibatis.cursor.Cursor;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

/**
 * @Classname IHongyanMapTableService
 * @Description
 * @Date 2022/07/26 下午 05:14
 * @author by Rui
 */
public interface IHongyanMapTableService extends IService<HongyanMapTable> {
    void fullScanAllKey(Consumer<String> consume);
}
