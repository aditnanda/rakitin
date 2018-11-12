package com.nand_project.ngerakit.Simulasi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.nand_project.ngerakit.R;
import com.nand_project.ngerakit.Simulasi.Database.DatabaseAdapter;
import com.nand_project.ngerakit.Simulasi.Database.DatabaseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SimulasiAdapter extends RecyclerView.Adapter<SimulasiAdapter.MyHolder>{
    private Context mContext ;


    List<SimulasiModel> list=  new ArrayList<>();
    private List<DatabaseModel> databaseModels = new ArrayList<>();
    private String harga;
    private int hargaInt, diskonInt, totDiskon, totHarga ;
    public int totSeluruhHarga = 0;
    public AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private ListView listView;
    private DatabaseAdapter databaseAdapter;


    public SimulasiAdapter(Context mContext, List<SimulasiModel> list) {

        this.mContext = mContext;
        this.list = list;
        //sharedPreference = new SharedPreference();

    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_simulasi_list,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {

        final SimulasiModel simulasiModel = list.get(position);

        holder.tv_kategori.setText(simulasiModel.getKategori());

        holder.et_pilih.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (databaseModels!=null){
                    databaseModels.clear();
                    initHDD(position, holder);
                }


            }
        });

        holder.pick_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = Integer.parseInt(holder.display.getText().toString());
                if (!holder.et_pilih.getText().toString().equals("")){
                    if (qty > 1){
                        qty = qty - 1;
                        holder.display.setText(qty+"");
                        holder.tv_harga_total.setText(Integer.parseInt(holder.tv_harga.getText().toString()) * qty+"");
                        Intent intent = new Intent("harga");
                        //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
                        intent.putExtra("harga",Integer.parseInt(holder.tv_harga.getText().toString()) * qty+""+"");
                        intent.putExtra("position",position+"");
                        intent.putExtra("status","no");
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                    }
                }

            }
        });

        holder.pick_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = Integer.parseInt(holder.display.getText().toString());
                if (!holder.et_pilih.getText().toString().equals("")) {
                    qty = qty + 1;
                    holder.display.setText(qty + "");
                    holder.tv_harga_total.setText(Integer.parseInt(holder.tv_harga.getText().toString()) * qty + "");
                    Intent intent = new Intent("harga");
                    //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
                    intent.putExtra("harga",Integer.parseInt(holder.tv_harga.getText().toString()) * qty+""+"");
                    intent.putExtra("position",position+"");
                    intent.putExtra("status","no");
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }

            }
        });

        holder.reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.et_pilih.setText("");
                holder.tv_harga.setText("");
                holder.tv_harga_total.setText("");
                holder.display.setText("1");
                Intent intent = new Intent("harga");
                //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
                intent.putExtra("harga","0");
                intent.putExtra("position",position+"");
                intent.putExtra("status","yes");
                intent.putExtra("nama","");
                intent.putExtra("hargaSatuan","0");
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
            }
        });

        Intent intent = new Intent("harga-message");
        //            intent.putExtra("quantity",Integer.parseInt(quantity.getText().toString()));
        intent.putExtra("totHarga",totSeluruhHarga+"");
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);


    }



    private void initHDD(final int position, MyHolder holder) {
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray data = obj.getJSONArray("data");
            JSONObject objdata = data.getJSONObject(position);
            JSONArray dataitem = objdata.getJSONArray("dataitem");


            for (int i = 0; i < dataitem.length(); i++) {
                JSONObject jsonObject = dataitem.getJSONObject(i);
                DatabaseModel databaseModel = new DatabaseModel();
                databaseModel.setHarga(jsonObject.getString("harga"));
                databaseModel.setJudul(jsonObject.getString("nama"));
                databaseModels.add(databaseModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        selectHDD(mContext, databaseModels, holder, position);
    }

    private void selectHDD(Context context, List<DatabaseModel> databaseModels, MyHolder holder, int position){

        builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_view_dialog, null);

        builder.setView(view);
        builder.setCancelable(true);

        alertDialog = builder.create();
        alertDialog.setTitle("Pilih");

        alertDialog.show();

        databaseAdapter = new DatabaseAdapter(context, databaseModels, holder.et_pilih,holder.tv_harga, holder.tv_harga_total,holder.display,harga, position, alertDialog);

        listView = view.findViewById(R.id.list_view_text);
        listView.setAdapter(databaseAdapter);


    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = mContext.getAssets().open("items.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public static class MyHolder extends RecyclerView.ViewHolder{

        TextView tv_name,tv_harga,tv_kategori, tv_harga_total, reset;
        EditText et_pilih, display;
        Button pick_plus, pick_minus;

        public MyHolder(View itemView) {
            super(itemView);

            et_pilih = itemView.findViewById(R.id.et_pilih);
            tv_harga = itemView.findViewById(R.id.tv_harga);
            tv_kategori = itemView.findViewById(R.id.tv_kategori);
            display = itemView.findViewById(R.id.display);
            tv_harga_total = itemView.findViewById(R.id.tv_harga_tot);
            pick_minus = itemView.findViewById(R.id.decrement);
            pick_plus = itemView.findViewById(R.id.increment);
            reset = itemView.findViewById(R.id.reset);

        }
    }

}
