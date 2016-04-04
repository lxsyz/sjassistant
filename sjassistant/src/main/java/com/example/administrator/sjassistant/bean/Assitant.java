package com.example.administrator.sjassistant.bean;

/**
 * Created by Administrator on 2016/4/3.
 */
public class Assitant {

    private String title;

    private String postman;     //发布人

    private String postTime;

    private String apartment;

    private String type;        //公告类型

    public String getTitle() {
        return title;
    }

    public String getPostman() {
        return postman;
    }

    public String getPostTime() {
        return postTime;
    }

    public String getType() {
        return type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPostman(String postman) {
        this.postman = postman;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public void setType(String type) {
        this.type = type;
    }


}
