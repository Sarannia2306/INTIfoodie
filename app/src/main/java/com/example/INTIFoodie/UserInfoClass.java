package com.example.INTIFoodie;

public class UserInfoClass {
    private String Name;
    private String Age;
    private String UserID;
    private String Identity;

    public UserInfoClass() {
    }

    public UserInfoClass(String Name, String Age, String UserID, String Identity) {
        this.Name = Name;
        this.Age = Age;
        this.UserID = UserID;
        this.Identity = Identity;
    }

    // Getter methods with annotations

    public String getName() {
        return Name;
    }


    public String getAge() {
        return Age;
    }



    public String getUserID() {
        return UserID;
    }

    public String getIdentity() {
        return Identity;
    }


    public void setName(String name) {
        Name = name;
    }

    // Setter methods
    public void setAge(String age) {
        Age = age;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public void setIdentity(String identity) {
        Identity = identity;
    }
}
