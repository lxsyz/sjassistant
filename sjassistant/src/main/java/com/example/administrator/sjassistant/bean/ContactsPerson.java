package com.example.administrator.sjassistant.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/4.
 */
public class ContactsPerson implements Serializable {
    private String id;

    private String customerCode;

    private String customerName;

    private String groupOrder;

    private int customerType;

    private int customerDept;

    private int customerPost;

    private String linkName;
    private String linkPhone;
    private String userCode;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public int getCustomerPost() {
        return customerPost;
    }

    public int getCustomerDept() {
        return customerDept;
    }

    public void setCustomerType(int customerType) {
        this.customerType = customerType;
    }

    public void setCustomerPost(int customerPost) {
        this.customerPost = customerPost;
    }

    public void setCustomerDept(int customerDept) {
        this.customerDept = customerDept;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }


}
