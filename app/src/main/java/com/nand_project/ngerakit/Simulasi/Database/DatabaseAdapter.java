package com.nand_project.ngerakit.Simulasi.Database;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.nand_project.ngerakit.R;

import java.util.ArrayList;
import java.util.List;


public class DatabaseAdapter extends BaseAdapter {
    private Context context;
    private List<DatabaseModel> databaseModels;
    private EditText pilih, display;
    private TextView tv_harga,tv_harga_total;
    private String hargaHDD;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private int position;

    public DatabaseAdapter(Context context, List<DatabaseModel> databaseModels, EditText pilih, TextView tv_harga,TextView tv_harga_total, EditText display, String hargaHDD, int position,AlertDialog alertDialog){

        this.databaseModels = new ArrayList<>();
        this.pilih         = pilih;
        this.tv_harga = tv_harga;
        this.tv_harga_total = tv_harga_total;
        this.display = display;
        this.hargaHDD = hargaHDD;
        this.builder      = new AlertDialog.Builder(context);
        this.alertDialog  = builder.create();
        this.alertDialog  = alertDialog;

        this.context = context;
        this.databaseModels = databaseModels;
        this.position = position;
    }

    @Override
    public int getCount() {
        return databaseModels.size();
    }

    @Override
    public Object getItem(int i) {
        return databaseModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.text_list, viewGroup, false);
        TextView txtNama = v.findViewById(R.id.list_text);

        final DatabaseModel databaseModel = databaseModels.get(i);
        txtNama.setText(databaseModel.getJudul());

        txtNama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pilih.setText(databaseModel.getJudul());
                int total = Integer.parseInt(databaseModel.getHarga());
                hargaHDD = total+"";

                tv_harga.setText(total+"");
                tv_harga_total.setText(total+"");

                Intent intent = new Intent("harga");
                //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
                intent.putExtra("harga",total+"");
                intent.putExtra("position",position+"");
                intent.putExtra("nama",databaseModel.getJudul()+"");
                intent.putExtra("hargaSatuan",total+"");
                intent.putExtra("status","ok");
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                alertDialog.dismiss();
            }
        });

        return v;
    }
}
