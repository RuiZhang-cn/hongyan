package com.rui.hongyan.constants;

/**
 * @author by Rui
 * @Description
 * @Date 2022/08/01 上午 11:26
 */
public interface MessageConstant {
    String KEY="KEY";
    String VALUE="VALUE";
    String TOO_LONG="长度超出限制";
    String PASSWORD_ERROR="密码错误";
    String SUCCESS="成功";
    String ERROR="失败";
    String KEY_NOT_FOUND="KEY未查询到";
    String KEY_EXIST="KEY已存在";
    String KEY_NOT_FOUND_OR_PASSWORD_ERROR =KEY_NOT_FOUND+"或"+PASSWORD_ERROR;
    String KEY_TOO_LONG=KEY+TOO_LONG;
    String VALUE_TOO_LONG=VALUE+TOO_LONG;
    String DELETE_IS_NOT_SUPPORTED="未设置密码的key不可删除/修改";
}
