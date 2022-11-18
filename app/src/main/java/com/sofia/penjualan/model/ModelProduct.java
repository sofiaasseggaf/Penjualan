package com.sofia.penjualan.model;

public class ModelProduct {

    String product_code, product_name, product_image, currency, dimension, unit;
    int price, discount;

    public ModelProduct(String product_code, String product_name, String product_image, int price, String currency, int discount, String dimension, String unit) {
        this.product_code = product_code;
        this.product_name = product_name;
        this.product_image = product_image;
        this.currency = currency;
        this.dimension = dimension;
        this.unit = unit;
        this.price = price;
        this.discount = discount;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
