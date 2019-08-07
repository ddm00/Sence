package com.turkcell.sence.models;

public class User {
    private String id;
    private String username;
    private String fullname;
    private String imageurl;
    private int profilePhoto;
    private String bio;

    public User(String id, String username, String fullname, String imageurl, String bio, int profilePhoto) {
        this.id = id;
        this.username = username;
        this.fullname = fullname;
        this.imageurl = imageurl;
        this.profilePhoto = profilePhoto;
        this.bio = bio;
    }

    public User() {
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

     public int getProfilePhoto() {
            return profilePhoto;
        }

        public void setImageurl(int profilePhoto) {
            this.profilePhoto = profilePhoto;
        }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
