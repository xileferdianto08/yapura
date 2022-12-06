package id.ac.umn.yapura;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class PeminjamanPage extends AppCompatActivity {
    private CardView pinjamRuangan, pinjamAlat, jadwalAlat, jadwalRuangan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peminjaman_page);
//
//        pinjamAlat = findViewById(R.id.pinjamAlat);
//        pinjamRuangan  = (LinearLayout) findViewById(R.id.pinjamRuangan);
//        jadwalAlat = (LinearLayout) findViewById(R.id.jadwalAlat);
//        jadwalRuangan = (LinearLayout) findViewById(R.id.jadwalRuangan);


    }

    public void toListRuangan(View view){
        startActivity(new Intent(PeminjamanPage.this, RuanganActivity.class));
    }

    public void toListBarang(View view){
        startActivity(new Intent(PeminjamanPage.this, BarangActivity.class));
    }
}