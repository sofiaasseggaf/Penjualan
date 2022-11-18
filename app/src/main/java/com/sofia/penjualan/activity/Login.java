package com.sofia.penjualan.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sofia.penjualan.R;
import com.sofia.penjualan.helper.DataHelper;
import com.sofia.penjualan.model.ModelUser;
import com.sofia.penjualan.utility.PreferenceUtils;

import java.util.List;

public class Login extends AppCompatActivity {

    EditText txt_username, txt_password;
    ImageButton btn_login;
    DataHelper dbCenter;
    List<ModelUser> listUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        txt_username = findViewById(R.id.txt_username);
        txt_password = findViewById(R.id.txt_password);
        btn_login = findViewById(R.id.btn_login);

        dbCenter = new DataHelper(this);
        listUser = dbCenter.getAllUser();

        if (!PreferenceUtils.getUsername(getApplicationContext()).equalsIgnoreCase("")){
            goToProductList();
        } else {
            // do nothing
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txt_username.getText().toString();
                String password = txt_password.getText().toString();
                if (!username.equalsIgnoreCase("") && !password.equalsIgnoreCase("")){
                    checkUser(username, password);
                } else {
                    Toast.makeText(Login.this, "Lengkapi Field !", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void checkUser(String username, String password){
        int right = 0;
        if(listUser.size()>0){
            try {
                for (int i = 0; i < listUser.size(); i++) {
                    String un = listUser.get(i).getUsername();
                    String pw = listUser.get(i).getPassword();
                    if (un.equalsIgnoreCase(username) && pw.equalsIgnoreCase(password)) {
                        right = 1;
                        PreferenceUtils.saveUsername(un, getApplicationContext());
                        goToProductList();
                        break;
                    }
                }
            } catch (Exception e){ }
            if (right!=1){
                Toast.makeText(Login.this, "Akun Belum Terdaftar", Toast.LENGTH_SHORT).show();
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Data User Belum Ada")
                    .setCancelable(false)
                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog =builder.create();
            alertDialog.show();
        }
    }

    private void goToProductList(){
        Intent a = new Intent(Login.this, ProductList.class);
        startActivity(a);
        finish();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Anda Mau Menutup Aplikasi")
                .setCancelable(false)
                .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Login.super.onBackPressed();
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