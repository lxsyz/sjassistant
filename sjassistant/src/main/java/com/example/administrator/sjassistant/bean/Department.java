package com.example.administrator.sjassistant.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/20.
 */
public class Department implements Serializable{
    private int id;

    private String code;

    private String name;

    private boolean checkState;

    public void setCheckState(boolean checkState) {
        this.checkState = checkState;
    }

    public boolean isCheckState() {
        return checkState;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setId(int id) {
        this.id = id;
    }
}
