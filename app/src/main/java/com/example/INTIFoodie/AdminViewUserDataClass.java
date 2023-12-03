package com.example.INTIFoodie;

public class AdminViewUserDataClass {
    private String name;
    private String age;
    private String userID;
    private String key;
    private String identity;

    public AdminViewUserDataClass() {

    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public String getName() { return name; }

    public String getAge() {
        return age;
    }

    public String getUserID() {
        return userID;
    }


    public String getIdentity() {
        return identity;
    }


    public AdminViewUserDataClass(String name, String age, String userID, String identity) {
        this.name = name;
        this.age = age;
        this.userID = userID;
        this.identity = identity;
    }


}