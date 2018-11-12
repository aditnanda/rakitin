package com.nand_project.ngerakit.Simulasi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nand_project.ngerakit.Preview.PreviewActivity;
import com.nand_project.ngerakit.Simulasi.Database.DatabaseAdapter;
import com.nand_project.ngerakit.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SimulasiActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SimulasiAdapter myAdapter;
    private GridLayoutManager gridLayoutManager;
    private List<SimulasiModel> simulasiModels = new ArrayList<>();
    private DatabaseAdapter hddAdapter;

    public AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private ListView listView;
    private int totHarga = 0, Harga;
    private TextView tv_sub_harga_item_2;
    private int [] harga = new int[50] ,hargaSatuan = new int[50];
    private int position,totalHarga, satuanHarga;
    private Button preview;
    private String [] nama = new String[50];
    private String namaBarang;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulasi);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Simulation");

        preview = findViewById(R.id.preview);

        tv_sub_harga_item_2 = findViewById(R.id.tv_sub_harga_item_2);

        recyclerView = findViewById(R.id.recycler_view);
        if (simulasiModels !=null){
            simulasiModels.clear();
            init();
        }
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(hargaMessageReceiver,
                new IntentFilter("harga"));

        final Thread thread = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(500);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                totalHarga = 0;
                                for (int i =0 ;i<harga.length;i++){
                                    totalHarga = totalHarga + harga[i];
                                }
                                tv_sub_harga_item_2.setText("Rp"+totalHarga);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        thread.start();

        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalHarga = 0;
                for (int i =0 ;i<harga.length;i++){
                    totalHarga = totalHarga + harga[i];
                }
//                Toast.makeText(SimulasiActivity.this, totalHarga+"", Toast.LENGTH_SHORT).show();
                final JSONArray array = new JSONArray();

                for (int i = 0 ; i<harga.length;i++) {
                    JSONObject obj=new JSONObject();
                    try {

                        if (harga[i]==0){

                        }else {
                            obj.put("nama", nama[i] + "");
                            obj.put("hargaSatuan", hargaSatuan[i] + "");
                            obj.put("harga", harga[i] + "");
                        }
                    }
                    catch (JSONException e) {
                    }
                    if (harga[i]==0){

                    }else {
                        array.put(obj);
                    }

                }

                Log.d("array",array+"");
                //Toast.makeText(SimulasiActivity.this, array+"", Toast.LENGTH_SHORT).show();

                if (totalHarga != 0) {
                    Intent intent = new Intent(SimulasiActivity.this, PreviewActivity.class);
                    intent.putExtra("array", array + "");
                    intent.putExtra("totalseluruh", totalHarga + "");
                    intent.putExtra("nama", getIntent().getStringExtra("nama"));
                    intent.putExtra("hp", getIntent().getStringExtra("hp"));
                    intent.putExtra("alamat", getIntent().getStringExtra("alamat"));
                    startActivity(intent);
                }else{
                    Toast.makeText(SimulasiActivity.this, "Silahkan melakukan simulasi barang terlebih dahulu", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    public BroadcastReceiver hargaMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            Harga = Integer.parseInt(intent.getStringExtra("harga"));
            position = Integer.parseInt(intent.getStringExtra("position"));
            if (intent.getStringExtra("status").toString().equals("ok")) {
                namaBarang = intent.getStringExtra("nama");
                satuanHarga = Integer.parseInt(intent.getStringExtra("hargaSatuan"));
                hargaSatuan[position] = satuanHarga;
                nama[position] = namaBarang;
            }
            Log.d("pos harga",Harga+" "+position);

            harga[position] = Harga;



            Locale localeID = new Locale("in", "ID");
            NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
            String totHargaRP = formatRupiah.format((double)totHarga);
            if (myAdapter != null){

                tv_sub_harga_item_2.setText(totHargaRP+"");
            }else {

                tv_sub_harga_item_2.setText("0");
            }

        }
    };

    private void init() {
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray data = obj.getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonObject = data.getJSONObject(i);
                SimulasiModel simulasiModel = new SimulasiModel();
                simulasiModel.setKategori(jsonObject.getString("item"));
                simulasiModels.add(simulasiModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setAdapter(simulasiModels);
    }

    public void setAdapter (List<SimulasiModel> lst) {

        myAdapter = new SimulasiAdapter(this,lst) ;
        gridLayoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(myAdapter);

    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getApplicationContext().getAssets().open("items.json");
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
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
