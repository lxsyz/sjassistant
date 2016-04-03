package com.example.administrator.sjassistant.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/3.
 */
public class MessageInform implements Serializable {

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
