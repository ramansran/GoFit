package com.example.gofit;

public class PostDataHoldingClass {
    String Username;
    String PostUrl;
    String Uid;
    String ProfileUrl;
    String DocumentId;
    String myurl;
    String myname;

    public PostDataHoldingClass(String username, String postUrl, String uid, String profileUrl, String documentId, String myurl, String myname) {
        Username = username;
        PostUrl = postUrl;
        Uid = uid;
        ProfileUrl = profileUrl;
        DocumentId = documentId;
        this.myurl = myurl;
        this.myname = myname;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPostUrl() {
        return PostUrl;
    }

    public void setPostUrl(String postUrl) {
        PostUrl = postUrl;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getProfileUrl() {
        return ProfileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        ProfileUrl = profileUrl;
    }

    public String getDocumentId() {
        return DocumentId;
    }

    public void setDocumentId(String documentId) {
        DocumentId = documentId;
    }

    public String getMyurl() {
        return myurl;
    }

    public void setMyurl(String myurl) {
        this.myurl = myurl;
    }

    public String getMyname() {
        return myname;
    }

    public void setMyname(String myname) {
        this.myname = myname;
    }
}