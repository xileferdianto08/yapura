package id.ac.umn.yapura;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
}