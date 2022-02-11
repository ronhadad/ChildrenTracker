package com.example.loginregister;

import java.util.List;

public class Person {
    String username;
    String pass;
    String phone;
    String id;
    String gender;
    public Person() {
    }

    public Person(String username, String pass, String phone, String id, String gender) {
        this.username = username;
        this.pass = pass;
        this.phone = phone;
        this.id = id;
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}