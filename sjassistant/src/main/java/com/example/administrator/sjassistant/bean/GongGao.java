package com.example.administrator.sjassistant.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/2.
 */
public class GongGao implements Serializable{

    private int id;

    private String noteTitle;
    private String noteDetail;
    private String notePublisher;
    private String notePublishtime;

    public int getId() {
        return id;
    }

    public String getNoteDetail() {
        return noteDetail;
    }

    public String getNotePublisher() {
        return notePublisher;
    }

    public String getNotePublishtime() {
        return notePublishtime;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNoteDetail(String noteDetail) {
        this.noteDetail = noteDetail;
    }

    public void setNotePublisher(String notePublisher) {
        this.notePublisher = notePublisher;
    }

    public void setNotePublishtime(String notePublishtime) {
        this.notePublishtime = notePublishtime;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    private String time;

    private String content;

    private String title;

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
