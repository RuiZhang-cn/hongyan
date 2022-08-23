package com.rui.hongyan.constants;

/**
 * @author by Rui
 * @Description
 * @Date 2022/08/01 上午 10:32
 */
public interface StringPool {
    String METHOD_PREFIX="method:";
    /**
     * &
     */
    String AMPERSAND=com.baomidou.mybatisplus.core.toolkit.StringPool.AMPERSAND;
    /**
     * ?
     */
    String QUESTION_MARK=com.baomidou.mybatisplus.core.toolkit.StringPool.QUESTION_MARK;
    /**
     * ""
     */
    String EMPTY=com.baomidou.mybatisplus.core.toolkit.StringPool.EMPTY;
    /**
     * =
     */
    String EQUALS=com.baomidou.mybatisplus.core.toolkit.StringPool.EQUALS;
    /**
     * /
     */
    String SLASH=com.baomidou.mybatisplus.core.toolkit.StringPool.SLASH;

    /**
     * 将字符加入字符串缓冲区
     * @param cacheString
     * @return
     */
    static String intern(String cacheString){
        return cacheString.intern();
    }
}
