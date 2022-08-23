package com.rui.hongyan.modules.hongyanmaptable.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rui.hongyan.constants.StringPool;
import com.rui.hongyan.filters.KeyBloomFilter;
import com.rui.hongyan.function.HongYanBaseFunction;
import com.rui.hongyan.modules.hongyanmaptable.entity.HongyanMapTable;
import com.rui.hongyan.modules.hongyanmaptable.service.IDynamicMethodService;
import com.rui.hongyan.modules.hongyanmaptable.service.IHongyanMapTableService;
import com.rui.hongyan.utils.VerificationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

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

    @Autowired
    private KeyBloomFilter keyBloomFilter;

    @GetMapping("/")
    public String getAllMethodName(){
        // 返回所有方法bean的name #只取第一个名字#
        String[] beanNamesForType = configurableListableBeanFactory.getBeanNamesForType(HongYanBaseFunction.class);
        return Arrays.toString(beanNamesForType)+"  使用方式请参阅:https://gitee.com/rui2450/hongyan/blob/master/README.md";
    }


    @RequestMapping(value = "/{key}",method = {RequestMethod.GET,RequestMethod.POST})
    public String getValueByKey(@PathVariable String key,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        return getValueByKey(key,null, request, response);
    }

    @GetMapping("/{key}/{password}")
    public String getValueByKey(@PathVariable String key,
                                @PathVariable String password,
                                HttpServletRequest request,
                                HttpServletResponse response) {
        if (StrUtil.isBlank(key)){
            // 返回所有方法的相关信息
            return getAllMethodName();
        }
        // 如果是方法直接进行处理
        if (configurableListableBeanFactory.containsBean(key)) {
            return dynamicMethodService.executeProcessing(
                    configurableListableBeanFactory.getBean(key, HongYanBaseFunction.class),
                    request,
                    response);
        }
        if (keyBloomFilter.isEnabled()&&!keyBloomFilter.contains(key)){
            log.debug("key:{}一定不存在,被布隆过滤器拦截",key);
            return KEY_NOT_FOUND;
        }
        HongyanMapTable hongyanMapTable = hongyanMapTableService.getOne(
                Wrappers.lambdaQuery(HongyanMapTable.class)
                        .eq(HongyanMapTable::getKey, key)
        );
        if (hongyanMapTable == null||!Objects.equals(hongyanMapTable.getPassword(),password)) {
            return KEY_NOT_FOUND_OR_PASSWORD_ERROR;
        }
        String value = hongyanMapTable.getValue();
        if (value.startsWith(StringPool.METHOD_PREFIX)) {
            value = value.substring(StringPool.METHOD_PREFIX.length());
            try {
                request.getRequestDispatcher(StringPool.SLASH+value).forward(request,response);
            } catch (ServletException | IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        }
        return value;
    }

    @PostMapping("/{key}/{value}/{password}")
    public String setValueByKey(@PathVariable String key,
                                @PathVariable String value,
                                @PathVariable String password,
                                HttpServletRequest request) {
        VerificationUtil.CheckDto checkDto = verificationUtil.checkLength(key.length(), value.length());
        if (!checkDto.isCheckStatus()){
            return checkDto.getCheckMessage();
        }
        if (keyBloomFilter.isEnabled()&&keyBloomFilter.contains(key)){
            log.debug("key:{}有可能存在,被布隆过滤器拦截",key);
            long count = hongyanMapTableService.count(
                    Wrappers.lambdaQuery(HongyanMapTable.class)
                            .eq(HongyanMapTable::getKey, key)
            );
            if (count > 0) {
                return KEY_EXIST;
            }
        }
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (!parameterMap.isEmpty()){
            StringJoiner stringJoiner = new StringJoiner(StringPool.AMPERSAND, value+StringPool.QUESTION_MARK, StringPool.EMPTY);
            StringBuilder argBuilder = new StringBuilder(3);
            for (Map.Entry<String, String[]> stringEntry : parameterMap.entrySet()) {
                stringJoiner.add(argBuilder.append(stringEntry.getKey())
                        .append(StringPool.EQUALS)
                        .append(stringEntry.getValue()[0])
                        .toString());
                argBuilder.delete(0,argBuilder.length());
            }
            value=stringJoiner.toString();
        }
        hongyanMapTableService.save(new HongyanMapTable(key,value,password));
        keyBloomFilter.add(key);
        return SUCCESS;
    }

    @PostMapping("/{key}/{value}")
    public String setValueByKey(@PathVariable String key,
                                @PathVariable String value,
                                HttpServletRequest request) {
        return setValueByKey(key, value,null,request);
    }

    @PutMapping("/{key}/{value}")
    public String editValueByKey(@PathVariable String key,
                                 @PathVariable String value){
        return DELETE_IS_NOT_SUPPORTED;
    }

    @PutMapping("/{key}/{value}/{password}")
    public String editValueByKey(@PathVariable String key,
                                 @PathVariable String value,
                                 @PathVariable String password) {
        // 未设置密码的不允许修改
        if (StrUtil.isBlank(password)){
            return DELETE_IS_NOT_SUPPORTED;
        }
        if (keyBloomFilter.isEnabled()&&!keyBloomFilter.contains(key)){
            return KEY_NOT_FOUND;
        }
        if (verificationUtil.checkValueLength(password.length())){
            return VALUE_TOO_LONG;
        }
        HongyanMapTable mapTable = hongyanMapTableService.getOne(
                Wrappers.lambdaQuery(HongyanMapTable.class)
                        .eq(HongyanMapTable::getKey, key)
                        .select(HongyanMapTable::getId, HongyanMapTable::getPassword));
        if (mapTable == null) {
            return KEY_NOT_FOUND;
        }
        if (mapTable.getPassword().equals(password)) {
            boolean updateStatus = hongyanMapTableService.updateById(
                    mapTable.setValue(value)
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
    public String deleteValueByKey(@PathVariable String key) {
        return DELETE_IS_NOT_SUPPORTED;
    }

    @DeleteMapping("/{key}/{password}")
    public String deleteValueByKey(@PathVariable String key,
                                   @PathVariable String password) {
        if (StrUtil.isBlank(password)) {
            return DELETE_IS_NOT_SUPPORTED;
        }
        if (keyBloomFilter.isEnabled()&&!keyBloomFilter.contains(key)){
            return KEY_NOT_FOUND;
        }
        HongyanMapTable mapTable = hongyanMapTableService.getOne(
                Wrappers.lambdaQuery(HongyanMapTable.class)
                        .eq(HongyanMapTable::getKey, key)
                        .eq(HongyanMapTable::getPassword, password)
                        .select(HongyanMapTable::getId, HongyanMapTable::getPassword));
        if (mapTable == null) {
            return KEY_NOT_FOUND_OR_PASSWORD_ERROR;
        }
        if (hongyanMapTableService.remove(
                Wrappers.lambdaQuery(HongyanMapTable.class)
                        .eq(HongyanMapTable::getKey, key)
        )) {
            return SUCCESS;
        } else {
            return ERROR;
        }
    }
}
