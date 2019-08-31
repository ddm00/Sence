package com.turkcell.sence.models;
public class User {
    private String id;
    private String username;
    private String fullname;
    private String imageurl;
    private boolean open;
    private String token;

    public User(String id, String username, String fullname, String imageurl, boolean open) {
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.imageurl = imageurl;
        this.open = open;
    }

    public User() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}

