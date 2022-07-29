package com.rui.hongyan.modules.hongyanmaptable.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rui.hongyan.function.HongYanBaseFunction;
import com.rui.hongyan.modules.hongyanmaptable.entity.HongyanMapTable;
import com.rui.hongyan.modules.hongyanmaptable.service.IDynamicMethodService;
import com.rui.hongyan.modules.hongyanmaptable.service.IHongyanMapTableService;
import com.rui.hongyan.modules.hongyanmaptable.vo.HongyanMapTableSaveVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.rui.hongyan.enums.MessageEnum.*;

/**
 * @author by Rui
 * @Classname HongyanXontroller
 * @Description
 * @Date 2022/07/26 下午 05:48
 */
@RestController
@Slf4j
public class HongyanController {
    @Autowired
    private IHongyanMapTableService hongyanMapTableService;
    @Autowired
    private IDynamicMethodService dynamicMethodService;
    @Autowired
    @Qualifier("configurableListableBeanFactory")
    private ConfigurableListableBeanFactory configurableListableBeanFactory;

    @Value("maxKeyLength:20")
    private Integer maxKeyLength;

    @GetMapping("/{key}")
    public String getValueByKey(@PathVariable String key, HttpServletResponse response, HttpServletRequest request) {
        // 如果是方法直接进行处理
        if (configurableListableBeanFactory.containsBean(key)){
            return dynamicMethodService.executeProcessing(
                    configurableListableBeanFactory.getBean(key, HongYanBaseFunction.class),
                    request,
                    response);
        }
        HongyanMapTable hongyanMapTable = hongyanMapTableService.getOne(
                Wrappers.lambdaQuery(HongyanMapTable.class)
                        .eq(HongyanMapTable::getKey, key)
        );
        if (hongyanMapTable == null) {
            return KEY_NOT_FOUND.message();
        }
        return hongyanMapTable.getValue();
    }

    @PostMapping("/{key}")
    public String setValueByKey(@PathVariable String key,
                                @RequestBody HongyanMapTableSaveVo hongyanMapTable) {
        if (key.length() > maxKeyLength){
            return KEY_TOO_LONG.message();
        }
        hongyanMapTableService.save(hongyanMapTable.setKey(key).toHongyanMapTable());
        return "新增成功!";
    }
}
