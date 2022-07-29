package com.rui.hongyan.modules.hongyanmaptable.vo;

import com.rui.hongyan.modules.hongyanmaptable.entity.HongyanMapTable;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @Classname HongyanMapTableVo
 * @Description
 * @Date 2022/07/26 下午 06:20
 * @author by Rui
 */
@Data
@Accessors(chain=true)
public class HongyanMapTableSaveVo {
    private String key;
    @NotBlank
    @Length(max = 2000,min = 0)
    private String value;
    private String password;

    public HongyanMapTable toHongyanMapTable(){
        Date date = new Date();
        return new HongyanMapTable(null,date,date,key,value,password);
    }
}
