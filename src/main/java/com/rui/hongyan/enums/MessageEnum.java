package com.rui.hongyan.enums;

/**
 * @author by Rui
 * @Description
 * @Date 2022/07/29 下午 04:44
 */
public enum MessageEnum {
    /**
     * 用户输入的key长度超出限制
     */
    KEY_TOO_LONG("KEY长度超出限制"),
    /**
     * VALUE长度超出限制
     */
    VALUE_TOO_LONG("VALUE长度超出限制"),
    /**
     * 密码错误
     */
    PASSWORD_ERROR("密码错误"),
    SUCCESS("操作成功"),
    ERROR("操作失败"),
    /**
     * 未查询到该KEY
     */
    KEY_NOT_FOUND("未查询到该KEY");

    private final String message;

    MessageEnum(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }
}
