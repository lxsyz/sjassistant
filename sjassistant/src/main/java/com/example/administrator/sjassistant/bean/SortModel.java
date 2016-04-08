package com.example.administrator.sjassistant.bean;

/**
 * Created by Administrator on 2016/4/4.
 */
public class SortModel {

    private String name;        //名字

    private String sortLetter;  //首字母

    private String group;       //分所

    private String phoneNumber;     //电话号码

    private int checked;

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public int getChecked() {
        return checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSortLetter() {
        return sortLetter;
    }

    public void setSortLetter(String sortLetter) {
        this.sortLetter = sortLetter;
    }

    public String getGroup() {
        return group;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
