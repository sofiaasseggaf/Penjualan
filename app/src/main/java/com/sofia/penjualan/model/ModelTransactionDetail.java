package com.sofia.penjualan.model;

public class ModelTransactionDetail {

    // doc_code text, doc_number text, product_code text, price integer, quantity integer, unit text, subtotal integer, currency text

    String doc_code, doc_number, product_code, unit, currency;
    int price, quantity, subtotal;

    public ModelTransactionDetail(String doc_code, String doc_number, String product_code, int price, int quantity, String unit, int subtotal, String currency) {
        this.doc_code = doc_code;
        this.doc_number = doc_number;
        this.product_code = product_code;
        this.price = price;
        this.quantity = quantity;
        this.unit = unit;
        this.subtotal = subtotal;
        this.currency = currency;
    }

    public String getDoc_code() {
        return doc_code;
    }

    public void setDoc_code(String doc_code) {
        this.doc_code = doc_code;
    }

    public String getDoc_number() {
        return doc_number;
    }

    public void setDoc_number(String doc_number) {
        this.doc_number = doc_number;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }
}
