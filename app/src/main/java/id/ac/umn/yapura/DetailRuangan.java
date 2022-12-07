package id.ac.umn.yapura;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailRuangan extends AppCompatActivity {
    int ruangId, userId;
    private TextView nama,maxCapacity, desc;
    private Button btnBook;
    private ImageView fotoRuangan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ruangan);

        nama = (TextView) findViewById(R.id.namaRuangan);
        maxCapacity = (TextView) findViewById(R.id.maxCapacity);
        desc = (TextView) findViewById(R.id.desc);
        fotoRuangan = (ImageView) findViewById(R.id.foto);
        btnBook = (Button) findViewById(R.id.btnBook);

        Intent intent = getIntent();
        String namaRuangan = intent.getStringExtra("nama");
        String maxcapacity = intent.getStringExtra("maxCapacity");
        String descRuangan = intent.getStringExtra("desc");
        String foto = intent.getStringExtra("foto");
        ruangId = Integer.parseInt(intent.getStringExtra("id"));

        Glide.with(this).load(foto).into(fotoRuangan);
        nama.setText(namaRuangan);
        maxCapacity.setText(maxcapacity);
        desc.setText(descRuangan);







    }
}