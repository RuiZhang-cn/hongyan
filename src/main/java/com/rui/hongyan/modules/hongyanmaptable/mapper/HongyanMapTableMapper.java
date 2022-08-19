package com.rui.hongyan.modules.hongyanmaptable.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rui.hongyan.modules.hongyanmaptable.entity.HongyanMapTable;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.cursor.Cursor;

/**
 * @Classname HongyanMapTableMapper
 * @Description
 * @Date 2022/07/26 下午 05:13
 * @author by Rui
 */
@Mapper
public interface HongyanMapTableMapper extends BaseMapper<HongyanMapTable> {
    @Select("select key from hongyan_map_table")
    @ResultType(String.class)
    Cursor<String> fullScanAllKey();
}
