package com.example.administrator.sjassistant.bean;

/**
 * Created by Administrator on 2016/4/25.
 */
public class Manager {

    private String name;

    private boolean CheckState = false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCheckState(boolean checkState) {
        CheckState = checkState;
    }

    public boolean isCheckState() {
        return CheckState;
    }
}
