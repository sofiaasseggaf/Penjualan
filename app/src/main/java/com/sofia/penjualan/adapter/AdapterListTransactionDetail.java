package com.sofia.penjualan.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sofia.penjualan.R;
import com.sofia.penjualan.model.ModelProduct;
import com.sofia.penjualan.model.ModelTransactionDetail;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterListTransactionDetail extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ModelTransactionDetail> dataItemList;
    List<ModelProduct> listProduct;

    public AdapterListTransactionDetail(List<ModelTransactionDetail> dataItemList,  List<ModelProduct> listProduct) {
        this.dataItemList = dataItemList;
        this.listProduct = listProduct;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.x_list_transaction_detail, parent, false);
        Penampung penampung = new Penampung(view);
        return penampung;
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        for (int i=0; i<listProduct.size(); i++){
            if (listProduct.get(i).getProduct_code().equalsIgnoreCase(dataItemList.get(position).getProduct_code())){
                ((Penampung)holder).nama_product.setText(listProduct.get(i).getProduct_name());
                /*if (!listProduct.get(i).getProduct_image().equalsIgnoreCase("")){
                    String encodedImage = listProduct.get(i).getProduct_image();
                    byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    ((Penampung)holder).img_product.setImageBitmap(decodedByte);
                }*/
            }
        }
        String a = checkDesimal(String.valueOf(dataItemList.get(position).getSubtotal()));
        ((Penampung)holder).subtotal_product.setText("Rp. "+a+",-");
        ((Penampung)holder).quantity_product.setText(String.valueOf(dataItemList.get(position).getQuantity()));
    }

    @Override
    public int getItemCount() {
        return dataItemList == null ? 0 : dataItemList.size();
    }

    class Penampung extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nama_product, quantity_product, subtotal_product;
        public ImageView img_product;
        public Penampung(View itemView) {
            super(itemView);
            nama_product = itemView.findViewById(R.id.nama_product);
            quantity_product = itemView.findViewById(R.id.quantity_product);
            img_product = itemView.findViewById(R.id.img_product);
            subtotal_product = itemView.findViewById(R.id.subtotal_product);
        }
        @Override
        public void onClick(View v) {
            Log.d("onclick", "onClick " + getLayoutPosition() + " " + nama_product.getText());
        }
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
}
