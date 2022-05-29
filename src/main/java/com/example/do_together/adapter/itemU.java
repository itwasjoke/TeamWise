package com.example.do_together.adapter;

import java.io.Serializable;

public class itemU implements Serializable {
    private String title;
    private String desc;
    private String date;
    private String to;
    private String from;
    private String done;
    private String status;
    private String type;

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    private int id = 0;

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getTitle() {
        return title;
    }

    public String getStatus() {
        return status;
    }

    public String getDone() {
        return done;
    }

    public String getDesc() {
        return desc;
    }

    public String getDate() {
        return date;
    }

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    public void setTitle(String title) { this.title = title; }

    public void setDesc(String desc) { this.desc = desc; }

    public void setDate(String date) { this.date = date; }

    public void setTo(String to) { this.to = to; }

    public void setFrom(String from) { this.from = from; }

    public void setDone(String done) { this.done = done; }

    public void setStatus(String status) { this.status = status; }
}
