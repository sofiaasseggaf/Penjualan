package com.sofia.penjualan.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sofia.penjualan.R;
import com.sofia.penjualan.adapter.AdapterListProduct;
import com.sofia.penjualan.helper.DataHelper;
import com.sofia.penjualan.model.ModelProduct;
import com.sofia.penjualan.model.ModelTransactionDetail;
import com.sofia.penjualan.utility.PreferenceUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ProductDetail extends AppCompatActivity {

    ImageView img_product;
    TextView nama_product, harga_product, dimension_product, unit_product;
    ImageButton btn_buy;
    ModelProduct selectedProduct;
    DataHelper dbCenter;
    List<ModelTransactionDetail> listTransaction;
    String product_code, random_code_product, random_number_product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);

        img_product = findViewById(R.id.img_product);
        nama_product = findViewById(R.id.nama_product);
        harga_product = findViewById(R.id.harga_product);
        dimension_product = findViewById(R.id.dimension_product);
        unit_product = findViewById(R.id.unit_product);
        btn_buy = findViewById(R.id.btn_buy);

        Intent intent = getIntent();
        product_code = intent.getStringExtra("product_code");

        dbCenter = new DataHelper(this);
        listTransaction = dbCenter.getAllTransactionDetail();
        selectedProduct = dbCenter.getProduct(product_code);

        randomize();

        if (selectedProduct!=null){
            setData();
        } else {
            Toast.makeText(this, "Data Product Tidak Ditemukan !", Toast.LENGTH_SHORT).show();
        }

        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkTransaksi(product_code);
            }
        });

    }

    private void setData(){
        /*if (!selectedProduct.getProduct_image().equalsIgnoreCase("")){
            String encodedImage = selectedProduct.getProduct_image();
            byte[] decodedString = Base64.decode(encodedImage, Base64.NO_WRAP);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            img_product.setImageBitmap(decodedByte);
        }*/
        nama_product.setText(selectedProduct.getProduct_name());
        String a = checkDesimal(String.valueOf(selectedProduct.getPrice()));
        harga_product.setText("Rp. "+a+",-");
        dimension_product.setText(selectedProduct.getDimension());
        unit_product.setText(selectedProduct.getUnit());
    }

    private void checkTransaksi(String product_code){

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

    private String checkDesimal(String a){
        DecimalFormat formatter;
        formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator('.');
        formatter = new DecimalFormat("###,###.##", symbols);

        if(a!=null || !a.equalsIgnoreCase("")){
            if(a.length()>3){
                a = formatter.format(Double.valueOf(a));
            }
        }
        return a;
    }

    private void goToProductList(){
        Intent a = new Intent(ProductDetail.this, ProductList.class);
        startActivity(a);
        finish();
    }

    @Override
    public void onBackPressed() {
        goToProductList();
    }
}
