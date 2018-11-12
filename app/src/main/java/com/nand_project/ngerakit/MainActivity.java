package com.nand_project.ngerakit;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nand_project.ngerakit.Simulasi.SimulasiActivity;

public class MainActivity extends AppCompatActivity {
    private EditText nama, hp, alamat;
    private Button btnProses;
    private TextView about;
    private Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.setTitle("Beranda");

        nama = findViewById(R.id.nama);
        hp = findViewById(R.id.hp);
        alamat = findViewById(R.id.alamat);
        about = findViewById(R.id.about);

        btnProses = findViewById(R.id.btnProses);

        btnProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nama.getText().toString().equals("")){
                    nama.setError("Name must be filled");
                }else if (hp.getText().toString().equals("")){
                    hp.setError("Phone Number must be filled");
                }else if (alamat.getText().toString().equals("")){
                    alamat.setError("Address must be filled");
                }else {
                    Intent intent = new Intent(MainActivity.this,SimulasiActivity.class);
                    intent.putExtra("nama",nama.getText().toString());
                    intent.putExtra("hp",hp.getText().toString());
                    intent.putExtra("alamat",alamat.getText().toString());
                    startActivity(intent);
                }
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog = new Dialog(MainActivity.this);
                myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                myDialog.setContentView(R.layout.popup_tentang);


                myDialog.setCancelable(true);
                myDialog.show();
                Window window = myDialog.getWindow();
                window.setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
            }
        });
    }
}
