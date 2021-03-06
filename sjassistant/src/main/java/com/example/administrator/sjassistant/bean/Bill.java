package com.example.administrator.sjassistant.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/4/5.
 */
public class Bill implements Serializable{

    private int id;

    private String billType;

    private int billId;

    private String dealPerson;

    private String dealTime;

    private String userCode;

    private String dealResult;

    private String dealOpinion;

    private String nextStepDealPerson;

    private String phone;

    private String billTypeChina;

    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    private boolean deal = false;

    public boolean isDeal() {
        return deal;
    }

    public void setDeal(boolean deal) {
        this.deal = deal;
    }

    public String getBillTypeChina() {
        return billTypeChina;
    }

    public void setBillTypeChina(String billTypeChina) {
        this.billTypeChina = billTypeChina;
    }

    private List<BillDetail> listbf;

    public List<BillDetail> getListbf() {
        return listbf;
    }

    public void setListbf(List<BillDetail> listbf) {
        this.listbf = listbf;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getBillId() {
        return billId;
    }

    public int getId() {
        return id;
    }

    public String getBillType() {
        return billType;
    }

    public String getDealOpinion() {
        return dealOpinion;
    }

    public String getDealPerson() {
        return dealPerson;
    }

    public String getDealResult() {
        return dealResult;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public void setDealPerson(String dealPerson) {
        this.dealPerson = dealPerson;
    }

    public void setDealTime(String dealTime) {
        this.dealTime = dealTime;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public void setDealResult(String dealResult) {
        this.dealResult = dealResult;
    }

    public void setDealOpinion(String dealOpinion) {
        this.dealOpinion = dealOpinion;
    }

    public String getNextStepDealPerson() {
        return nextStepDealPerson;
    }

    public void setNextStepDealPerson(String nextStepDealPerson) {
        this.nextStepDealPerson = nextStepDealPerson;
    }

    public String getDealTime() {
        return dealTime;
    }
}
