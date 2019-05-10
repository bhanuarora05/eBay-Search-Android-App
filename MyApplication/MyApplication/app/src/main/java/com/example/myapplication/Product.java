package com.example.myapplication;

public class Product {

    public String imageurl,itemid,title,short_title,price,shipping_cost,zipcode,condition;
    public String[] prod_details;
    public String encoded;

    public Product(){
        prod_details = new String[8];
    }

    public void encode(){
        encoded = String.join("~",prod_details);
    }

    public void decode(String str){
        prod_details = str.split("~");
        condition = prod_details[7];
        imageurl = prod_details[0];
        price = prod_details[4];
        itemid = prod_details[1];
        shipping_cost = prod_details[5];
        short_title = prod_details[3];
        title = prod_details[2];
        zipcode = prod_details[6];
        encoded = str;
    }

    public void setCondition(String condition) {
        this.condition = condition;
        prod_details[7]= condition;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
        prod_details[0]= imageurl;
    }

    public void setPrice(String price) {
        this.price = price;
        prod_details[4]= price;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
        prod_details[1]=itemid;
    }

    public void setShipping_cost(String shipping_cost) {
        this.shipping_cost = shipping_cost;
        prod_details[5]= shipping_cost;
    }

    public void setShort_title(String short_title) {
        this.short_title = short_title;
        prod_details[3]= short_title;
    }

    public void setTitle(String title) {
        this.title = title;
        prod_details[2]= title;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
        prod_details[6]=  zipcode;
    }
}

