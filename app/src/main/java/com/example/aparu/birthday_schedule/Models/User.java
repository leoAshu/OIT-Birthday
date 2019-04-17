package com.example.aparu.birthday_schedule.Models;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("user_id")
    int user_id;

    @SerializedName("user_name")
    String user_name;

    public User(int user_id,String user_name){

        this.user_id = user_id;
        this.user_name = user_name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
