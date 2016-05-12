package com.example.administrator.sjassistant.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/4/28.
 */
public class BillDetailList implements Serializable{

    private int id;
    private int recordId;

    private int billId;

    private String displayName;

    private String displayKey;

    private String displayLevel;

    private String displayValue;

    private List<ListLog> listLogs;

    private String userImg;

    private String isHref;

    public String getIsHref() {
        return isHref;
    }

    public void setIsHref(String isHref) {
        this.isHref = isHref;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public List<ListLog> getListLogs() {
        return listLogs;
    }

    public void setListLogs(List<ListLog> listLogs) {
        this.listLogs = listLogs;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }

    private String seq;

    private int fatherId;

    private String billShowType;

    public String getDisplayLevel() {
        return displayLevel;
    }

    public void setDisplayLevel(String displayLevel) {
        this.displayLevel = displayLevel;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayKey() {
        return displayKey;
    }

    public void setDisplayKey(String displayKey) {
        this.displayKey = displayKey;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public int getFatherId() {
        return fatherId;
    }

    public void setFatherId(int fatherId) {
        this.fatherId = fatherId;
    }

    public String getBillShowType() {
        return billShowType;
    }

    public void setBillShowType(String billShowType) {
        this.billShowType = billShowType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
