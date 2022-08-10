package com.rui.hongyan.modules.hongyanmaptable.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rui.hongyan.config.HongYanConfig;
import com.rui.hongyan.function.HongYanBaseFunction;
import com.rui.hongyan.modules.hongyanmaptable.entity.HongyanMapTable;
import com.rui.hongyan.modules.hongyanmaptable.service.IDynamicMethodService;
import com.rui.hongyan.modules.hongyanmaptable.service.IHongyanMapTableService;
import com.rui.hongyan.modules.hongyanmaptable.vo.HongyanMapTableSaveVo;
import com.rui.hongyan.utils.VerificationUtil;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Arrays;
import java.util.Date;

import static com.rui.hongyan.constants.MessageConstant.*;

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

    @Autowired
    private VerificationUtil verificationUtil;
    @GetMapping("/")
    public String getAllMethodName(){
        // 返回所有方法bean的name #只取第一个名字#
        String[] beanNamesForType = configurableListableBeanFactory.getBeanNamesForType(HongYanBaseFunction.class);
        return Arrays.toString(beanNamesForType);
    }


    @GetMapping("/{key}")
    public String getValueByKey(@PathVariable String key, HttpServletResponse response, HttpServletRequest request) {
        if (StrUtil.isEmpty(key)){
            // 返回所有方法的相关信息
            String[] beanNamesForType = configurableListableBeanFactory.getBeanNamesForType(HongYanBaseFunction.class);
            return Arrays.toString(beanNamesForType);
        }
        // 如果是方法直接进行处理
        if (configurableListableBeanFactory.containsBean(key)) {
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
            return KEY_NOT_FOUND;
        }
        return hongyanMapTable.getValue();
    }

    @PostMapping("/{key}/{value}/{password}")
    public String setValueByKey(@PathVariable String key,
                                @PathVariable String value,
                                @PathVariable String password) {
        VerificationUtil.CheckDto checkDto = verificationUtil.checkLength(key.length(), value.length());
        if (!checkDto.isCheckStatus()){
            return checkDto.getCheckMessage();
        }
        hongyanMapTableService.save(new HongyanMapTable(key,value,password));
        return SUCCESS;
    }

    @PostMapping("/{key}/{value}")
    public String setValueByKey(@PathVariable String key,
                                @PathVariable String value) {
        VerificationUtil.CheckDto checkDto = verificationUtil.checkLength(key.length(), value.length());
        if (!checkDto.isCheckStatus()){
            return checkDto.getCheckMessage();
        }
        hongyanMapTableService.save(new HongyanMapTable(key,value,null));
        return SUCCESS;
    }

    @PostMapping("/{key}")
    public String setValueByKey(@PathVariable String key,HttpServletResponse response, HttpServletRequest request) {
        if (configurableListableBeanFactory.containsBean(key)) {
            return dynamicMethodService.executeProcessing(
                    configurableListableBeanFactory.getBean(key, HongYanBaseFunction.class),
                    request,
                    response);
        }else {
            return KEY_NOT_FOUND;
        }
    }

    @PutMapping("/{key}")
    public String editValueByKey(@PathVariable String key,
                                 @RequestBody HongyanMapTableSaveVo hongyanMapTable) {
        if (!verificationUtil.checkValueLength(hongyanMapTable.getValue().length())){
            return VALUE_TOO_LONG;
        }
        HongyanMapTable mapTable = hongyanMapTableService.getOne(
                Wrappers.lambdaQuery(HongyanMapTable.class)
                        .eq(HongyanMapTable::getKey, key)
                        .select(HongyanMapTable::getId, HongyanMapTable::getPassword));
        if (mapTable == null) {
            return KEY_NOT_FOUND;
        }
        if (StrUtil.isEmpty(mapTable.getPassword()) || mapTable.getPassword().equals(hongyanMapTable.getPassword())) {
            boolean updateStatus = hongyanMapTableService.updateById(
                    mapTable.setValue(hongyanMapTable.getValue())
                            .setUpdatedAt(new Date())
            );
            if (updateStatus) {
                return SUCCESS;
            } else {
                return ERROR;
            }
        } else {
            return PASSWORD_ERROR;
        }
    }

    @DeleteMapping("/{key}")
    public String deleteValueByKey(@PathVariable String key,
                                   @RequestBody HongyanMapTableSaveVo hongyanMapTable) {
        if (StrUtil.isEmpty(hongyanMapTable.getPassword())) {
            return DELETE_IS_NOT_SUPPORTED;
        }
        HongyanMapTable mapTable = hongyanMapTableService.getOne(
                Wrappers.lambdaQuery(HongyanMapTable.class)
                        .eq(HongyanMapTable::getKey, key)
                        .select(HongyanMapTable::getId, HongyanMapTable::getPassword));
        if (mapTable == null) {
            return KEY_NOT_FOUND;
        }
        if (hongyanMapTable.getPassword().equals(mapTable.getPassword())) {
            if (hongyanMapTableService.remove(
                    Wrappers.lambdaQuery(HongyanMapTable.class)
                            .eq(HongyanMapTable::getKey, key)
            )) {
                return SUCCESS;
            } else {
                return ERROR;
            }
        } else {
            return PASSWORD_ERROR;
        }
    }
}
