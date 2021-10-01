package com.MyNotePlaceApp.app;

public class User {
    public String fullName, age, email,id;
    public User(){}

    public User(String fullName, String age, String email)
    {
        this.fullName = fullName;
        this.age = age;
        this.email = email;
        this.id = null;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
