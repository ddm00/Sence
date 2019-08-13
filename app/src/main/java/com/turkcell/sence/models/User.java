package com.turkcell.sence.models;

public class User {
    private String Id;
    private String Username;
    private String Fullname;
    private String ImageUrl;

    public User() {

    }

    public User(String id, String username, String fullname, String imageUrl) {
        this.Id = id;
        this.Username = username;
        this.Fullname = fullname;
        this.ImageUrl = imageUrl;
    }


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        this.Fullname = fullname;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.ImageUrl = imageUrl;
    }
}
