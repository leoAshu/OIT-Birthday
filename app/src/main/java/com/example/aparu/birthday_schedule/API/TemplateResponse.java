package com.example.aparu.birthday_schedule.API;

import com.example.aparu.birthday_schedule.Models.Template;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TemplateResponse {

    @SerializedName("success")
    Boolean success;

    @SerializedName("message")
    String message;

    @SerializedName("data")
    ArrayList<Template> templates;

    public TemplateResponse(Boolean success, String message, ArrayList<Template> templates) {
        this.success = success;
        this.message = message;
        this.templates = templates;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Template> getTemplates() {
        return templates;
    }

    public void setTemplates(ArrayList<Template> templates) {
        this.templates = templates;
    }
}
