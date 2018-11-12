package com.nand_project.ngerakit.Preview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.nand_project.ngerakit.R;
import com.nand_project.ngerakit.Simulasi.SimulasiAdapter;
import com.nand_project.ngerakit.Simulasi.SimulasiModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PreviewActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PreviewAdapter myAdapter;
    private GridLayoutManager gridLayoutManager;
    private List<PreviewModel> previewModels = new ArrayList<>();
    private TextView tv_nama, tv_hp, tv_alamat, tv_totharga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.setTitle("Preview");

        tv_nama = findViewById(R.id.tv_nama);
        tv_alamat = findViewById(R.id.tv_alamat);
        tv_hp = findViewById(R.id.tv_hp);
        tv_totharga = findViewById(R.id.tv_sub_harga_item_2);
        recyclerView = findViewById(R.id.recycler_view);

        tv_nama.setText("Nama : "+getIntent().getStringExtra("nama"));
        tv_hp.setText("No. Hp : "+getIntent().getStringExtra("hp"));
        tv_alamat.setText("Alamat : "+getIntent().getStringExtra("alamat"));

        Locale localeID = new Locale("in", "ID");
        NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);
        String hargaRP = formatRupiah.format((double)Integer.parseInt(getIntent().getStringExtra("totalseluruh").toString()));
        tv_totharga.setText(hargaRP);

        if (previewModels !=null){
            previewModels.clear();
            init();
        }
    }

    private void init() {
        try {
            JSONArray jsonArray = new JSONArray(getIntent().getStringExtra("array"));

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                PreviewModel previewModel = new PreviewModel();
                previewModel.setHarga(jsonObject.getString("harga"));
                previewModel.setNama(jsonObject.getString("nama"));
                previewModel.setHargaSatuan(jsonObject.getString("hargaSatuan"));
                previewModels.add(previewModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setAdapter(previewModels);
    }

    public void setAdapter (List<PreviewModel> lst) {

        myAdapter = new PreviewAdapter(this,lst) ;
        gridLayoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(myAdapter);

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
