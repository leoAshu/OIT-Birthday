package com.example.aparu.birthday_schedule.API;

import com.example.aparu.birthday_schedule.Models.Employee;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BirthdayResponse {

    Boolean success;

    String message;

    @SerializedName("data")
    ArrayList<Employee> employees;

    public BirthdayResponse(Boolean success, String message, ArrayList<Employee> employees) {
        this.success = success;
        this.message = message;
        this.employees = employees;
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

    public ArrayList<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(ArrayList<Employee> employees) {
        this.employees = employees;
    }
}
