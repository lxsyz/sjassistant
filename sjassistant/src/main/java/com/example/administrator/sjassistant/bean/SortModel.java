package com.example.administrator.sjassistant.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/4.
 */
public class SortModel implements Serializable {

    private String name;        //名字

    private String sortLetter;  //首字母

    private String group;       //分所

    private String phoneNumber;     //电话号码

    private int checked;

    private int customerType;       //客户类型

    private int customerDept;       //客户部门

    private int customerPost;       //客户职位

    private String userCode;        //

    private String customerTypeName;

    private String customerDeptName;

    private String customerPostName;

    public String getCustomerDeptName() {
        return customerDeptName;
    }

    public String getCustomerPostName() {
        return customerPostName;
    }

    public String getCustomerTypeName() {
        return customerTypeName;
    }

    public void setCustomerDeptName(String customerDeptName) {
        this.customerDeptName = customerDeptName;
    }

    public void setCustomerPostName(String customerPostName) {
        this.customerPostName = customerPostName;
    }

    public void setCustomerTypeName(String customerTypeName) {
        this.customerTypeName = customerTypeName;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public int getCustomerDept() {
        return customerDept;
    }

    public int getCustomerPost() {
        return customerPost;
    }

    public int getCustomerType() {
        return customerType;
    }

    public void setCustomerDept(int customerDept) {
        this.customerDept = customerDept;
    }

    public void setCustomerPost(int customerPost) {
        this.customerPost = customerPost;
    }

    public void setCustomerType(int customerType) {
        this.customerType = customerType;
    }

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
