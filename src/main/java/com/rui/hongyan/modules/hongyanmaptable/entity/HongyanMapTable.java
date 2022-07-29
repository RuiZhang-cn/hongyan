package com.rui.hongyan.modules.hongyanmaptable.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Classname HongyanMapTable
 * @Description
 * @Date 2022/07/26 下午 05:10
 * @author by Rui
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HongyanMapTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;
    private Date createdAt;
    private Date updatedAt;

    private String key;
    private String value;
    private String password;

    public HongyanMapTable(String key, String value, String password) {
        this.key = key;
        this.value = value;
        this.password = password;
    }
}