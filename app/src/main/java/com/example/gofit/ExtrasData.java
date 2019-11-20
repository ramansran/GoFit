package com.example.gofit;

public class ExtrasData {
private String UID;
private String profile_pic_url;
private String username;

    public ExtrasData(String UID, String profile_pic_url, String username) {
        this.UID = UID;
        this.profile_pic_url = profile_pic_url;
        this.username = username;
    }

    public ExtrasData() {
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getProfile_pic_url() {
        return profile_pic_url;
    }

    public void setProfile_pic_url(String profile_pic_url) {
        this.profile_pic_url = profile_pic_url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
