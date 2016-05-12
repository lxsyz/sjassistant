package com.example.administrator.sjassistant.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/2.
 */
public class Person implements Serializable{
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getGroupOrder() {
        return groupOrder;
    }

    public void setGroupOrder(String groupOrder) {
        this.groupOrder = groupOrder;
    }

    public int getCustomerType() {
        return customerType;
    }

    public void setCustomerType(int customerType) {
        this.customerType = customerType;
    }

    public int getCustomerDept() {
        return customerDept;
    }

    public void setCustomerDept(int customerDept) {
        this.customerDept = customerDept;
    }

    public int getCustomerPost() {
        return customerPost;
    }

    public void setCustomerPost(int customerPost) {
        this.customerPost = customerPost;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }

    private String customerCode;

    private String customerName;

    private String groupOrder;

    private int customerType;

    private int customerDept;

    private int customerPost;

    private String linkName;

    private String linkPhone;

    private String userCode;

    private String postName;

    private String deptName;

    private String customerTypeName;

    public String getCustomerTypeName() {
        return customerTypeName;
    }

    public String getDeptName() {
        return deptName;
    }

    public String getPostName() {
        return postName;
    }

    public void setCustomerTypeName(String customerTypeName) {
        this.customerTypeName = customerTypeName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }
}

