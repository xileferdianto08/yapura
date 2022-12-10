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

public class DetailBarang extends AppCompatActivity {
    int barangId, userId;
    private TextView nama,maxQty, desc;
    private Button btnBook;
    private ImageView fotoBarang;

//    SharedPreferences sessions = getSharedPreferences("user_data", Context.MODE_PRIVATE);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_barang);

        nama = (TextView) findViewById(R.id.namaBarang);
        maxQty = (TextView) findViewById(R.id.maxQty);
        desc = (TextView) findViewById(R.id.desc);
        fotoBarang = (ImageView) findViewById(R.id.foto);
        btnBook = (Button) findViewById(R.id.btnBook);

        try {
            Intent intent = getIntent();

            String namaBarang = intent.getStringExtra("nama");
            String maxqty = intent.getStringExtra("maxQty");
            String descBarang = intent.getStringExtra("desc");
            String foto = intent.getStringExtra("foto");
            String id = intent.getStringExtra("id");
            barangId = Integer.parseInt(intent.getStringExtra("id"));
//            userId = sessions.getInt("userId", 98);

            Glide.with(this).load(foto).into(fotoBarang);
            nama.setText(namaBarang);
            maxQty.setText(maxqty);
            desc.setText(descBarang);
        }catch (NumberFormatException ex){
            ex.printStackTrace();
        }
    }
    public void backToMenu(View view){
        startActivity(new Intent(DetailBarang.this, RuanganActivity.class));
    }
}