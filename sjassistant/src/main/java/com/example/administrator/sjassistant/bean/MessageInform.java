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

    private String content;

    private String title;

    private String time;

    private String imgPath;

    private String username;

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImgPath() {
        return imgPath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
