package com.rui.hongyan.utils;

import com.rui.hongyan.config.HongYanConfig;
import com.rui.hongyan.constants.MessageConstant;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author by Rui
 * @Description
 * @Date 2022/08/04 下午 06:21
 */
@Component
public class VerificationUtil {

    @Data
    public static class CheckDto{
        private boolean checkStatus;
        private String checkMessage;
        public static final CheckDto CHECK_SUCCESS =new CheckDto(true);

        public CheckDto(boolean checkStatus, String checkMessage) {
            this.checkStatus = checkStatus;
            this.checkMessage = checkMessage;
        }

        public CheckDto(boolean checkStatus) {
            this.checkStatus=checkStatus;
        }

        public static CheckDto checkError(String errorMessage){
            return new CheckDto(false,errorMessage);
        }

    }
    @Autowired
    private HongYanConfig config;

    public CheckDto checkLength(int keyLength,int valueLength){
        if (checkKeyLength(keyLength)) {
            return CheckDto.checkError(MessageConstant.KEY_TOO_LONG);
        }
        if (checkValueLength(valueLength)){
            return CheckDto.checkError(MessageConstant.VALUE_TOO_LONG);
        }
        return CheckDto.CHECK_SUCCESS;
    }

    public boolean checkKeyLength(int keyLength){
        return keyLength>config.getMaxKeyLength();
    }

    public boolean checkValueLength(int valueLength){
        return valueLength>config.getMaxValueLength();
    }
}
