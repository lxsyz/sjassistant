package com.example.administrator.sjassistant.bean;

/**
 * Created by Administrator on 2016/4/24.
 */
public class InspectPerson {

    private String name;

    private String headImg;

    private boolean checkState = false;

    public String getName() {
        return name;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCheckState(boolean checkState) {
        this.checkState = checkState;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public boolean isCheckState() {
        return checkState;
    }
}
