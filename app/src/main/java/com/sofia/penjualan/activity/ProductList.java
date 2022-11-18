package com.sofia.penjualan.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sofia.penjualan.R;
import com.sofia.penjualan.adapter.AdapterListProduct;
import com.sofia.penjualan.helper.DataHelper;
import com.sofia.penjualan.model.ModelProduct;
import com.sofia.penjualan.model.ModelTransactionDetail;
import com.sofia.penjualan.utility.PreferenceUtils;
import com.sofia.penjualan.utility.RecyclerItemClickListener;

import java.util.List;
import java.util.Random;

public class ProductList extends AppCompatActivity {

    RecyclerView rvListProduct;
    ImageButton btn_checkout, btn_logout;
    DataHelper dbCenter;
    List<ModelProduct> listProduct;
    List<ModelTransactionDetail> listTransaction;
    AdapterListProduct itemList;
    ModelProduct selectedProduct;

    String random_code_product, random_number_product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);

        rvListProduct = findViewById(R.id.rvListProduct);
        btn_checkout = findViewById(R.id.btn_checkout);
        btn_logout = findViewById(R.id.btn_logout);

        dbCenter = new DataHelper(this);
        listProduct = dbCenter.getAllProduct();

        randomize();

        if (listProduct.size()>0) {
            /*Uri path1 = Uri.parse("android.resource://com.sofia.penjualan/" + R.drawable.rinsocair);
            String path11 = path1.toString();
            Uri path2 = Uri.parse("android.resource://com.sofia.penjualan/" + R.drawable.sunlight);
            String path22 = path2.toString();
            SQLiteDatabase db = dbCenter.getWritableDatabase();
            db.execSQL("update product set product_image='"+path11+ "' " +
                    "where product_name='"+"Rinso Cair" +"'");
            db.execSQL("update product set product_image='"+path22+ "' " +
                    "where product_name='"+"Sunlight" +"'");*/

            itemList = new AdapterListProduct(listProduct, new AdapterListProduct.AdapterListProductListener() {
                @Override
                public void buy(View v, int pos, String product_code) {
                    checkTransaksi(product_code);
                }
            });
            rvListProduct.setLayoutManager(new LinearLayoutManager(ProductList.this));
            rvListProduct.setAdapter(itemList);
            rvListProduct.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rvListProduct,
                    new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Intent a = new Intent(ProductList.this, ProductDetail.class);
                            a.putExtra("product_code", listProduct.get(position).getProduct_code());
                            startActivity(a);
                        }

                        @Override
                        public void onLongItemClick(View view, int position) {

                        }
                    }));


        } else {
            Toast.makeText(this, "Product Tidak Ada !", Toast.LENGTH_SHORT).show();
        }

        btn_checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCheckout();
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

    }

    private void randomize(){
        int upperbound = 999;
        Random rand = new Random();
        int a  = rand.nextInt(upperbound);

        random_number_product = String.valueOf(a);
        random_code_product = random();

        if (PreferenceUtils.getCodeProduct(getApplicationContext()).equalsIgnoreCase("")){
            PreferenceUtils.saveCodeProduct(random_code_product, getApplicationContext());
        }
        if (PreferenceUtils.getNumberProduct(getApplicationContext()).equalsIgnoreCase("")){
            PreferenceUtils.saveNumberProduct(random_number_product, getApplicationContext());
        }
    }

    private void checkTransaksi(String product_code){

        selectedProduct = dbCenter.getProduct(product_code);

        listTransaction = dbCenter.getAllTransactionDetail();
        if (listTransaction.size()>0){
            for(int i=0; i<listTransaction.size(); i++){
                if (listTransaction.get(i).getDoc_code().equalsIgnoreCase(PreferenceUtils.getCodeProduct(getApplicationContext())) &&
                listTransaction.get(i).getDoc_number().equalsIgnoreCase(PreferenceUtils.getNumberProduct(getApplicationContext()))){
                    if (listTransaction.get(i).getProduct_code().equalsIgnoreCase(product_code)){

                        int quantity = listTransaction.get(i).getQuantity();
                        int new_quantity = quantity+1;

                        int subTotal = listTransaction.get(i).getSubtotal();
                        int new_subTotal = subTotal + listTransaction.get(i).getPrice();

                        updateTransaksi(product_code, new_quantity, new_subTotal);
                        break;
                    }
                } else {
                    inputTransaksi(product_code);
                    break;
                }
            }
        } else {
            inputTransaksi(product_code);
        }
    }

    private void inputTransaksi(String product_code){
        SQLiteDatabase db = dbCenter.getWritableDatabase();
        db.execSQL("insert into tdetail(doc_code, doc_number, product_code, price, quantity, unit, subtotal, currency) values('" +
                PreferenceUtils.getCodeProduct(getApplicationContext()) + "','" +
                PreferenceUtils.getNumberProduct(getApplicationContext()) + "','" +
                product_code + "','" +
                selectedProduct.getPrice() + "','" +
                1 + "','" +
                selectedProduct.getUnit() + "','" +
                selectedProduct.getPrice() + "','" +
                "IDR" + "')");
        Toast.makeText(getApplicationContext(), "Berhasil Masukkan Keranjang", Toast.LENGTH_SHORT).show();
    }

    private void updateTransaksi(String product_code, int new_quantity, int new_subTotal){
        SQLiteDatabase db = dbCenter.getWritableDatabase();
        db.execSQL("update tdetail set quantity='"+new_quantity+
                "', subtotal='"+new_subTotal+"' " +
                "where product_code='"+product_code +"'");
        Toast.makeText(getApplicationContext(), "Berhasil Masukkan Keranjang", Toast.LENGTH_SHORT).show();
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(3);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
    private void logOut(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Anda Mau Log Out ?")
                .setCancelable(false)
                .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        PreferenceUtils.saveUsername("", getApplicationContext());
                        PreferenceUtils.saveNumberProduct("", getApplicationContext());
                        PreferenceUtils.saveCodeProduct("", getApplicationContext());
                        goToLogin();
                    }
                })

                .setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog =builder.create();
        alertDialog.show();
    }
    public void goToCheckout(){
        Intent a = new Intent(ProductList.this, Checkout.class);
        startActivity(a);
        finish();
    }
    public void goToLogin(){
        Intent a = new Intent(ProductList.this, Login.class);
        startActivity(a);
        finish();
        finishAffinity();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Anda Mau Menutup Aplikasi ?")
                .setCancelable(false)
                .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        PreferenceUtils.saveNumberProduct("", getApplicationContext());
                        PreferenceUtils.saveCodeProduct("", getApplicationContext());
                        ProductList.super.onBackPressed();
                        finish();
                        finishAffinity();
                    }
                })

                .setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog =builder.create();
        alertDialog.show();
    }
}
