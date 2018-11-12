package com.nand_project.ngerakit.Preview;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.nand_project.ngerakit.R;
import com.nand_project.ngerakit.Simulasi.Database.DatabaseAdapter;
import com.nand_project.ngerakit.Simulasi.Database.DatabaseModel;
import com.nand_project.ngerakit.Simulasi.SimulasiModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.MyHolder>{
    private Context mContext ;


    private List<PreviewModel> list=  new ArrayList<>();


    public PreviewAdapter(Context mContext, List<PreviewModel> list) {

        this.mContext = mContext;
        this.list = list;

    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_preview_list,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {

        final PreviewModel previewModel = list.get(position);

        holder.tv_nama.setText(previewModel.getNama());

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        String hargaRP = formatRupiah.format((double)Integer.parseInt(previewModel.getHarga().toString()));

        holder.tv_harga_total.setText(hargaRP);

        int qty = Integer.parseInt(previewModel.getHarga())/Integer.parseInt(previewModel.getHargaSatuan());

        holder.tv_harga.setText(qty+" x "+previewModel.getHargaSatuan());


    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyHolder extends RecyclerView.ViewHolder{

        TextView tv_nama,tv_harga, tv_harga_total;


        public MyHolder(View itemView) {
            super(itemView);

            tv_nama = itemView.findViewById(R.id.tv_nama);
            tv_harga = itemView.findViewById(R.id.tv_harga);
            tv_harga_total = itemView.findViewById(R.id.tv_harga_tot);

        }
    }

}
