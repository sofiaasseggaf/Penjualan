package com.sofia.penjualan.model;

public class ModelTransactionHeader {

    // doc_code text, doc_number text, user text, total integer, date text

    String doc_code, user, date;
    int doc_number, total;

    public ModelTransactionHeader(String doc_code, int doc_number, String user, int total, String date) {
        this.doc_code = doc_code;
        this.doc_number = doc_number;
        this.user = user;
        this.total = total;
        this.date = date;
    }

    public String getDoc_code() {
        return doc_code;
    }

    public void setDoc_code(String doc_code) {
        this.doc_code = doc_code;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDoc_number() {
        return doc_number;
    }

    public void setDoc_number(int doc_number) {
        this.doc_number = doc_number;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
