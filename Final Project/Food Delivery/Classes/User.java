package com.rishi.fooddelivery.Model;

public class User {

    private String Name;
    private String Password;
    private String Phone;
    private String IsStaff;

    public User(String name, String password) {
        Name = name;
        Password = password;
        IsStaff="true";
    }

    public User() {

    }

    public String getName() {

        return Name;
    }

    public String getIsStaff() {
        return IsStaff;
    }

    public void setIsStaff(String isStaff) {
        IsStaff = isStaff;
    }

    public String getPassword() {
        return Password;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

}
