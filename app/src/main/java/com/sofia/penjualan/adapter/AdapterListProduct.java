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

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterListProduct extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ModelProduct> dataItemList;
    Context context;
    public AdapterListProductListener clickListener;

    public AdapterListProduct(List<ModelProduct> dataItemList, AdapterListProductListener clickListener) {
        this.dataItemList = dataItemList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.x_list_product, parent, false);
        Penampung penampung = new Penampung(view);
        return penampung;
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((Penampung)holder).nama_product.setText(dataItemList.get(position).getProduct_name());
        String a = checkDesimal(String.valueOf(dataItemList.get(position).getPrice()));
        ((Penampung)holder).harga_product.setText("Rp. "+a+",-");
        if (!dataItemList.get(position).getProduct_image().equalsIgnoreCase("")){
            String encodedImage = dataItemList.get(position).getProduct_image();
            byte[] decodedString = Base64.decode(encodedImage, Base64.NO_WRAP);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ((Penampung)holder).img_product.setImageBitmap(decodedByte);
        }
        ((Penampung)holder).btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.buy(v, position, dataItemList.get(position).getProduct_code());
            }
        });
    }

    public interface AdapterListProductListener {
        void buy(View v, int pos, String product_code);
    }

    @Override
    public int getItemCount() {
        return dataItemList == null ? 0 : dataItemList.size();
    }

    class Penampung extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nama_product, harga_product;
        public ImageView img_product;
        public ImageButton btn_buy;
        public Penampung(View itemView) {
            super(itemView);
            nama_product = itemView.findViewById(R.id.nama_product);
            harga_product = itemView.findViewById(R.id.harga_product);
            img_product = itemView.findViewById(R.id.img_product);
            btn_buy = itemView.findViewById(R.id.btn_buy);
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
