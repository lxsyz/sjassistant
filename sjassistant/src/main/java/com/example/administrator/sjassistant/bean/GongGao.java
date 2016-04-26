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


}
