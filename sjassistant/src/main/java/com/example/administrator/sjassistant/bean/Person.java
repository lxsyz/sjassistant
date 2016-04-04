package com.example.administrator.sjassistant.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/2.
 */
public class Person implements Serializable{
    private String name;

    private String group;

    private String phoneNumber;

    private String companyName;

    private String customer_type;

    private String apartment;

    private String person_work;

    public String getApartment() {
        return apartment;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getCustomer_type() {
        return customer_type;
    }

    public String getPerson_work() {
        return person_work;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setCustomer_type(String customer_type) {
        this.customer_type = customer_type;
    }

    public void setPerson_work(String person_work) {
        this.person_work = person_work;
    }

    public String getGroup() {
        return group;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}

