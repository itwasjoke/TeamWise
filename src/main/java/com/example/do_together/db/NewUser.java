package com.example.do_together.db;

public class NewUser {
    public String name, team, root, email, desc;

    public NewUser(){
    }

    public NewUser(String name, String team, String root, String email, String desc) {
        this.name = name;
        this.team = team;
        this.root = root;
        this.email = email;
        this.desc=desc;
    }
}
