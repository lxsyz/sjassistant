package com.example.administrator.sjassistant.bean;

/**
 * Created by Administrator on 2016/5/6.
 */
public class LinkPerson {

    private String id;

    private String customerCode;

    private String customerName;

    private String groupCode;

    private int customerType;

    private int customerDept;

    private int customerPost;

    private String linkName;

    private String linkPhone;
    private String userCode;

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

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public int getCustomerDept() {
        return customerDept;
    }

    public void setCustomerDept(int customerDept) {
        this.customerDept = customerDept;
    }

    public int getCustomerType() {
        return customerType;
    }

    public void setCustomerType(int customerType) {
        this.customerType = customerType;
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
