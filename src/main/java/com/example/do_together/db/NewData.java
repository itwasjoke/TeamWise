package com.example.do_together.db;

public class NewData {
    public String id, title, desc, date, to, from, done, status, type;

    public NewData() {
    }

    public NewData(String id, String title, String desc, String date, String to, String from, String done, String status, String type) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.date = date;
        this.to = to;
        this.from = from;
        this.done=done;
        this.status=status;
        this.type=type;
    }
}