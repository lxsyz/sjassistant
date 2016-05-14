package com.example.administrator.sjassistant.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/6.
 */
public class FilterCondition implements Serializable {

    private String code;

    private String name;

    private int id;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
