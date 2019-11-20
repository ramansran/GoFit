package com.example.gofit;

import java.util.Date;

public class ChatMainData
{
    String Message;
    String RecieverId;
    String SenderId;
    Date TimeStamp;
    String OtherUserName;
    String OtherUserProfileUrl;


    public ChatMainData() {
    }

    public ChatMainData(String message, String recieverId, String senderId, String otherUserName, String otherUserProfileUrl) {
        Message = message;
        RecieverId = recieverId;
        SenderId = senderId;
        OtherUserName = otherUserName;
        OtherUserProfileUrl = otherUserProfileUrl;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getRecieverId() {
        return RecieverId;
    }

    public void setRecieverId(String recieverId) {
        RecieverId = recieverId;
    }

    public String getSenderId() {
        return SenderId;
    }

    public void setSenderId(String senderId) {
        SenderId = senderId;
    }

    public Date getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        TimeStamp = timeStamp;
    }

    public String getOtherUserName() {
        return OtherUserName;
    }

    public void setOtherUserName(String otherUserName) {
        OtherUserName = otherUserName;
    }

    public String getOtherUserProfileUrl() {
        return OtherUserProfileUrl;
    }

    public void setOtherUserProfileUrl(String otherUserProfileUrl) {
        OtherUserProfileUrl = otherUserProfileUrl;
    }
}
