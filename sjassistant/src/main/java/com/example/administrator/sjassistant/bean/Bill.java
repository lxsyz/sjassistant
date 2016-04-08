package com.example.administrator.sjassistant.bean;

/**
 * Created by Administrator on 2016/4/5.
 */
public class Bill {

    private String type;

    private String posttime;

    private String name;

    private String postman;


    public String getName() {
        return name;
    }

    public String getPostman() {
        return postman;
    }

    public String getPosttime() {
        return posttime;
    }

    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPostman(String postman) {
        this.postman = postman;
    }

    public void setPosttime(String posttime) {
        this.posttime = posttime;
    }

    public void setType(String type) {
        this.type = type;
    }
}
