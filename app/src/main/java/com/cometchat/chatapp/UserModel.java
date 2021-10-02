package com.cometchat.chatapp;

public class UserModel {
    private String uid;
    private String name;
    private String email;
    private String avatar;

    public UserModel() {
    }

    public UserModel(String uid, String name, String email, String avatar) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.avatar = avatar;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar() {
        return avatar;
    }
}
