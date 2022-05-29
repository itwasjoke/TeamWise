package com.example.do_together.adapter;

import java.io.Serializable;

public class itemU2 implements Serializable {
    private String name;
    private String team;
    private String root;
    private String desc;

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public String getNam() { return name; }

    public String getTeam() {
        return team;
    }

    public String getRoot() {
        return root;
    }

    public void setNam(String name) {
        this.name = name;
    }

    public void setTeam(String team) { this.team = team; }

    public void setRoot(String root) { this.root = root; }
}
