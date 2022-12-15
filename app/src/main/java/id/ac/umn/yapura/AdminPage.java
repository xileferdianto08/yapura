package id.ac.umn.yapura;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class AdminPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);



    }

    public void toListRuangan(View view){
        startActivity(new Intent(AdminPage.this, ListRuanganAdmin.class));
    }

    public void toListBarang(View view){
        startActivity(new Intent(AdminPage.this, ListBarangAdmin.class));
    }

    public void toJadwalRuangan(View view){
        startActivity(new Intent(AdminPage.this, ListPeminjamanRuangan.class));
    }

    public void toJadwalBarang(View view){
        startActivity(new Intent(AdminPage.this, ListPeminjamanBarang.class));
    }

    public void toLogout(View view){
        AlertDialog.Builder dialog = new AlertDialog.Builder(AdminPage.this);
        dialog.setTitle("Anda yakin ingin keluar?");

        dialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sessions = getSharedPreferences("admin_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sessions.edit();

                editor.clear();
                editor.apply();


                startActivity(new Intent(AdminPage.this, MainActivity.class));
            }
        });

        dialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.create();
        dialog.show();
    }
}