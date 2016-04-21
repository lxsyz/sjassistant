package com.example.administrator.sjassistant.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/3.
 */
public class MessageInform implements Serializable {

    private int id;
    private String messagePublishtime;
    private String messagePublisher;
    private String messageTitle;
    private String headImg;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessagePublisher() {
        return messagePublisher;
    }

    public void setMessagePublisher(String messagePublisher) {
        this.messagePublisher = messagePublisher;
    }

    public String getMessagePublishtime() {
        return messagePublishtime;
    }

    public void setMessagePublishtime(String messagePublishtime) {
        this.messagePublishtime = messagePublishtime;
    }


}
