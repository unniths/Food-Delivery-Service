package com.rishi.fooddeliveryserver.Model;


public class Food {
    private String Name,Image,Price,Discount,menuId;

    public Food() {

    }

    public Food(String name, String image, String price, String discount, String menuId) {
        Name = name;
        Image = image;
        Price = price;
        Discount = discount;
        this.menuId = menuId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}
