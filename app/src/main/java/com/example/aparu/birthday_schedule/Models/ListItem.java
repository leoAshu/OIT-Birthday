package com.example.aparu.birthday_schedule.Models;

/**
 * Created by aparu on 2/23/2019.
 */

public class ListItem {

    private String user_name;

    public ListItem(String user_name) {

        this.user_name = user_name;

    }

    public String getUser_name() {

        return user_name;
    }
    public void setUser_name(String user_name)
    {
        this.user_name = user_name;
    }
}
