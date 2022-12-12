package id.ac.umn.yapura;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class DetailRuangan extends AppCompatActivity {
    int ruangId, userId;
    private TextView nama,maxCapacity, desc;
    private Button btnBook;
    private ImageView fotoRuangan;

//    SharedPreferences sessions = getSharedPreferences("user_data", Context.MODE_PRIVATE);


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
        String id = intent.getStringExtra("id");
        ruangId = Integer.parseInt(intent.getStringExtra("id"));
//            userId = sessions.getInt("userId", 98);

        Glide.with(this).load(foto).into(fotoRuangan);
        nama.setText(namaRuangan);
        maxCapacity.setText(maxcapacity);
        desc.setText(descRuangan);

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(DetailRuangan.this, BookRuangan.class);
                newIntent.putExtra("namaRuangan", namaRuangan);
                newIntent.putExtra("ruangId", String.valueOf(ruangId));
                startActivity(newIntent);
            }
        });

    }
    public void backToMenu(View view){
        startActivity(new Intent(DetailRuangan.this, RuanganActivity.class));
    }


}