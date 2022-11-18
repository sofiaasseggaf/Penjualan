package com.sofia.penjualan.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sofia.penjualan.R;
import com.sofia.penjualan.adapter.AdapterListTransactionDetail;
import com.sofia.penjualan.helper.DataHelper;
import com.sofia.penjualan.model.ModelProduct;
import com.sofia.penjualan.model.ModelTransactionDetail;
import com.sofia.penjualan.utility.PreferenceUtils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Checkout extends AppCompatActivity {

    RecyclerView rvTransactionDetail;
    ImageButton btn_confirm;
    TextView txt_total;
    DataHelper dbCenter;
    List<ModelTransactionDetail> listTransaction;
    List<ModelTransactionDetail> listTransactionNew = new ArrayList<>();
    List<ModelProduct> listProduct;
    AdapterListTransactionDetail itemList;
    int total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkout);

        rvTransactionDetail = findViewById(R.id.rvTransactionDetail);
        btn_confirm = findViewById(R.id.btn_confirm);
        txt_total = findViewById(R.id.txt_total);

        dbCenter = new DataHelper(this);
        listProduct = dbCenter.getAllProduct();
        listTransaction = dbCenter.getAllTransactionDetail();

        if (listTransaction.size()>0){
            for (int i=0; i<listTransaction.size(); i++){
                if (listTransaction.get(i).getDoc_code().equalsIgnoreCase(PreferenceUtils.getCodeProduct(getApplicationContext())) &&
                        listTransaction.get(i).getDoc_number().equalsIgnoreCase(PreferenceUtils.getNumberProduct(getApplicationContext()))){
                    listTransactionNew.add(listTransaction.get(i));
                }
            }
        } else {
            Toast.makeText(this, "Belum Ada Product yang Dibeli !", Toast.LENGTH_SHORT).show();
        }

        if (listTransactionNew.size()>0){
            if (listProduct.size()>0){
                setData();
            } else {
                Toast.makeText(this, "Belum Ada Product yang Dibeli !", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Belum Ada Product yang Dibeli !", Toast.LENGTH_SHORT).show();
        }

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Checkout.this);
                builder.setMessage("Anda Yakin Mau CheckOut ?")
                        .setCancelable(false)
                        .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                               inputTransaksiHeader();
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
        });
    }

    private void setData(){
        itemList = new AdapterListTransactionDetail(listTransactionNew, listProduct);
        rvTransactionDetail.setLayoutManager(new LinearLayoutManager(Checkout.this));
        rvTransactionDetail.setAdapter(itemList);

        for (int i=0; i<listTransactionNew.size(); i++){
            total = total + listTransactionNew.get(i).getSubtotal();
        }

        String a = checkDesimal(String.valueOf(total));
        txt_total.setText("Rp. "+a+",-");
    }

    private void hapusTransactionDetail(){
        SQLiteDatabase db = dbCenter.getWritableDatabase();
        for (int i=0; i<listTransaction.size(); i++){
            db.execSQL("delete from tdetail where doc_code = '"+PreferenceUtils.getCodeProduct(getApplicationContext())+"'");
        }
        PreferenceUtils.saveNumberProduct("", getApplicationContext());
        PreferenceUtils.saveCodeProduct("", getApplicationContext());
        goToProductList();
    }

    private void inputTransaksiHeader(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String now = formatter.format(new Date());

        SQLiteDatabase db = dbCenter.getWritableDatabase();
        db.execSQL("insert into theader(doc_code, doc_number, user, total, date) values('" +
                PreferenceUtils.getCodeProduct(getApplicationContext()) + "','" +
                PreferenceUtils.getNumberProduct(getApplicationContext()) + "','" +
                PreferenceUtils.getUsername(getApplicationContext()) + "','" +
                total + "','" +
                now + "')");
        Toast.makeText(getApplicationContext(), "Berhasil Check Out", Toast.LENGTH_SHORT).show();
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
        Intent a = new Intent(Checkout.this, ProductList.class);
        startActivity(a);
        finish();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Batal Check Out ?")
                .setCancelable(false)
                .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        hapusTransactionDetail();
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