package com.example.jobd;

public class Offer {
    public String userName;
    public String image;
    public String price;

    public Offer(){

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }






    public Offer(String userName, String price, String image) {
        this.userName = userName;
        this.price = price;
        this.image = image;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }






}