package com.example.aparu.birthday_schedule.Models;

import com.google.gson.annotations.SerializedName;

public class Template {

    @SerializedName("image")
    String template;

    int id;

    public Template(String template, int id) {
        this.template = template;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}
