package com.turkcell.sence.models;

public class User {
    private String Id;
    private String Username;
    private String Fullname;
    private String ImageUrl;

    public User(String id, String username, String fullname, String imageUrl) {
        Id = id;
        Username = username;
        Fullname = fullname;
        ImageUrl = imageUrl;
    }

    public User() {
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
