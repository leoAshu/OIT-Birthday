package com.example.aparu.birthday_schedule.API;

import com.example.aparu.birthday_schedule.Models.User;
import com.google.gson.annotations.SerializedName;

public class VerifyResponse {

    @SerializedName("success")
    private String success;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private User user;


    public VerifyResponse(String success,String message , User user)
    {
        this.success = success;
        this.message = message;
        this.user = user;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
