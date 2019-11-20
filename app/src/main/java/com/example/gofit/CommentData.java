package com.example.gofit;

public class CommentData
{

    String Url;
    String Comment;
    String userid;

    public CommentData(String url, String comment, String userid) {
        Url = url;
        Comment = comment;
        this.userid = userid;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
