package com.sofia.penjualan.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;

import com.sofia.penjualan.R;
import com.sofia.penjualan.model.ModelProduct;
import com.sofia.penjualan.model.ModelTransactionDetail;
import com.sofia.penjualan.model.ModelTransactionHeader;
import com.sofia.penjualan.model.ModelUser;

import java.util.ArrayList;
import java.util.List;

public class DataHelper extends SQLiteOpenHelper {

    private  static final String DATABASE_NAME = "penjualaaan.db";
    private static final int DATABASE_VERSION = 1;

    public DataHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // TABLE : LOGIN, PRODUCT, TRANSACTIONHEADER, TRANSACTIONDETAIL

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_TABLE_LOGIN = "create table login(username text, password text);";
        Log.d("Data", "onCreate : " + CREATE_TABLE_LOGIN);
        db.execSQL(CREATE_TABLE_LOGIN);

        String CREATE_TABLE_PRODUCT = "create table product(product_code text, product_name text, product_image text, price integer, currency text, discount integer, dimension text, unit text);";
        Log.d("Data", "onCreate : " + CREATE_TABLE_PRODUCT);
        db.execSQL(CREATE_TABLE_PRODUCT);

        String CREATE_TABLE_TRANSACTIONDETAIL = "create table tdetail(doc_code text, doc_number text, product_code text, price integer, quantity integer, unit text, subtotal integer, currency text);";
        Log.d("Data", "onCreate : " + CREATE_TABLE_TRANSACTIONDETAIL);
        db.execSQL(CREATE_TABLE_TRANSACTIONDETAIL);

        String CREATE_TABLE_TRANSACTIONHEADER = "create table theader(doc_code text, doc_number text, user text, total integer, date text);";
        Log.d("Data", "onCreate : " + CREATE_TABLE_TRANSACTIONHEADER);
        db.execSQL(CREATE_TABLE_TRANSACTIONHEADER);

        String INSERT_INTO_USER = "INSERT INTO login(username, password) VALUES ('sofia', '1234');";
        db.execSQL(INSERT_INTO_USER);


        String INSERT_INTO_PRODUCT1 = "INSERT INTO product(product_code, product_name, product_image, price, currency, discount, dimension, unit) VALUES ('ABCD', 'Rinso Cair', '', 12000, 'IDR', '0', '10 cm x 15 cm', 'PCS');";
        db.execSQL(INSERT_INTO_PRODUCT1);

        String INSERT_INTO_PRODUCT2 = "INSERT INTO product(product_code, product_name, product_image, price, currency, discount, dimension, unit) VALUES ('ABDC', 'Sunlight', '', 3000, 'IDR', '0', '7 cm x 5 cm', 'PCS');";
        db.execSQL(INSERT_INTO_PRODUCT2);

    }

    public List<ModelUser> getAllUser() {
        List<ModelUser> modelUserList = new ArrayList<ModelUser>();
        // Select All Query
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM login", null);
        cursor.moveToFirst();
        // Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ModelUser profileModel = new ModelUser(
                        cursor.getString(0),
                        cursor.getString(1));

                modelUserList.add(profileModel);
            } while (cursor.moveToNext());
        }

        return modelUserList;
    }

    public List<ModelProduct> getAllProduct() {
        List<ModelProduct> modelProductList = new ArrayList<ModelProduct>();
        // Select All Query
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM product", null);
        cursor.moveToFirst();
        // Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ModelProduct productModel = new ModelProduct(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getString(4),
                        cursor.getInt(5),
                        cursor.getString(6),
                        cursor.getString(7));

                modelProductList.add(productModel);
            } while (cursor.moveToNext());
        }

        return modelProductList;
    }

    public List<ModelTransactionDetail> getAllTransactionDetail() {
        List<ModelTransactionDetail> modelTransactionDetailList = new ArrayList<ModelTransactionDetail>();
        // Select All Query
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM tdetail", null);
        cursor.moveToFirst();
        // Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ModelTransactionDetail transactionDetailModel = new ModelTransactionDetail(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getInt(4),
                        cursor.getString(5),
                        cursor.getInt(6),
                        cursor.getString(7));

                modelTransactionDetailList.add(transactionDetailModel);
            } while (cursor.moveToNext());
        }
        return modelTransactionDetailList;
    }

    public List<ModelTransactionHeader> getAllTransactionHeader() {
        List<ModelTransactionHeader> modelTransactionHeaderList = new ArrayList<ModelTransactionHeader>();
        // Select All Query
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT  * FROM theader", null);
        cursor.moveToFirst();
        // Looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ModelTransactionHeader transactionHeaderModel = new ModelTransactionHeader(
                        cursor.getString(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getString(4));

                modelTransactionHeaderList.add(transactionHeaderModel);
            } while (cursor.moveToNext());
        }
        return modelTransactionHeaderList;
    }

    //get product with product_code
    public ModelProduct getProduct(String product_code) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM product WHERE product_code = '" + product_code + "'", null);
        String[] product = new String[cursor.getCount()];
        cursor.moveToFirst();
        for (int cc=0; cc < cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);
            product[cc] = cursor.getString(0).toString();
        }
        ModelProduct productLogged = new ModelProduct(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getInt(3),
                cursor.getString(4),
                cursor.getInt(5),
                cursor.getString(6),
                cursor.getString(7));
        return productLogged;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
