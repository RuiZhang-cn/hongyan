package com.rui.hongyan.modules.hongyanmaptable.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.rui.hongyan.constants.StringPool;
import com.rui.hongyan.function.HongYanBaseFunction;
import com.rui.hongyan.modules.hongyanmaptable.entity.HongyanMapTable;
import com.rui.hongyan.modules.hongyanmaptable.service.IDynamicMethodService;
import com.rui.hongyan.modules.hongyanmaptable.service.IHongyanMapTableService;
import com.rui.hongyan.modules.hongyanmaptable.vo.HongyanMapTableSaveVo;
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

    @GetMapping("/")
    public String getAllMethodName(){
        // 返回所有方法bean的name #只取第一个名字#
        String[] beanNamesForType = configurableListableBeanFactory.getBeanNamesForType(HongYanBaseFunction.class);
        return Arrays.toString(beanNamesForType);
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
        HongyanMapTable hongyanMapTable = hongyanMapTableService.getOne(
                Wrappers.lambdaQuery(HongyanMapTable.class)
                        .eq(HongyanMapTable::getKey, key)
        );
        if (hongyanMapTable == null) {
            return KEY_NOT_FOUND;
        }
        if (StrUtil.isEmpty(hongyanMapTable.getPassword())&&Objects.equals(password,hongyanMapTable.getPassword())) {
            return PASSWORD_ERROR;
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
        return SUCCESS;
    }

    @PostMapping("/{key}/{value}")
    public String setValueByKey(@PathVariable String key,
                                @PathVariable String value,
                                HttpServletRequest request) {
        return setValueByKey(key, value,null,request);
    }


    @PutMapping("/{key}")
    public String editValueByKey(@PathVariable String key,
                                 @RequestBody HongyanMapTableSaveVo hongyanMapTable) {
        // 未设置密码的不允许修改
        if (StrUtil.isEmpty(hongyanMapTable.getPassword())){
            return DELETE_IS_NOT_SUPPORTED;
        }
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
        if (mapTable.getPassword().equals(hongyanMapTable.getPassword())) {
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
