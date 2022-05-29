package com.example.do_together.adapter;

import java.io.Serializable;

public class itemU3 implements Serializable {
    private String type;
    private String id;

    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
