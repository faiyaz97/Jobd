package com.example.jobd;

public class User {
    public String name, surname, dob, email, phone, address, postcode;

    public User(){

    }

    public User(String name, String surname, String dob, String email, String phone, String address, String postcode) {
        this.name = name;
        this.surname = surname;
        this.dob = dob;
        //this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.postcode = postcode;
    }
}