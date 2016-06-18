package com.example.administrator.sjassistant.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/4.
 */
public class GroupDetail implements Serializable {
    private int id;

    private String username;

    private String userpassword;

    private String userCode;

    private String email;

    private String sex;

    private int deptId;

    private String deptName;

    private String title;
    private String address;
    private String roleId;
    private String roleName;
    private String portrait;
    private String canLogin;
    private String trueName;
    private String phone;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getCanLogin() {
        return canLogin;
    }

    public void setCanLogin(String canLogin) {
        this.canLogin = canLogin;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
