package com.example.aparu.birthday_schedule.Models;

public class Employee {

    int id;

    String emp_id;

    String emp_name;

    String emp_dob;

    Boolean email_sent_status;

    Boolean status;

    Boolean scheduled_birthday_status;

    int template_id;

    Boolean isSelected;

    public Employee(int id, String emp_id, String emp_name, String emp_dob, Boolean email_sent_status, Boolean status, Boolean scheduled_birthday_status, int template_id) {
        this.id = id;
        this.emp_id = emp_id;
        this.emp_name = emp_name;
        this.emp_dob = emp_dob;
        this.email_sent_status = email_sent_status;
        this.status = status;
        this.scheduled_birthday_status = scheduled_birthday_status;
        this.template_id = template_id;
        this.isSelected = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public String getEmp_dob() {
        return emp_dob;
    }

    public void setEmp_dob(String emp_dob) {
        this.emp_dob = emp_dob;
    }

    public Boolean getEmail_sent_status() {
        return email_sent_status;
    }

    public void setEmail_sent_status(Boolean email_sent_status) {
        this.email_sent_status = email_sent_status;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getScheduled_birthday_status() {
        return scheduled_birthday_status;
    }

    public void setScheduled_birthday_status(Boolean scheduled_birthday_status) {
        this.scheduled_birthday_status = scheduled_birthday_status;
    }

    public int getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(int template_id) {
        this.template_id = template_id;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
