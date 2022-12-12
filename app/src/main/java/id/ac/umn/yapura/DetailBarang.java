package id.ac.umn.yapura;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
        String namaBarang;

        nama = (TextView) findViewById(R.id.namaBarang);
        maxQty = (TextView) findViewById(R.id.maxQty);
        desc = (TextView) findViewById(R.id.desc);
        fotoBarang = (ImageView) findViewById(R.id.foto);
        btnBook = (Button) findViewById(R.id.btnBook);
        Intent intent = getIntent();


        String maxqty = intent.getStringExtra("maxQty");
        String descBarang = intent.getStringExtra("desc");
        String foto = intent.getStringExtra("foto");
        String id = intent.getStringExtra("id");
        barangId = Integer.parseInt(intent.getStringExtra("id"));
        namaBarang = intent.getStringExtra("nama");


//            userId = sessions.getInt("userId", 98);

        Glide.with(this).load(foto).into(fotoBarang);
        nama.setText(namaBarang);
        maxQty.setText(maxqty);
        desc.setText(descBarang);

        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(DetailBarang.this, BookBarang.class);
                newIntent.putExtra("namaBarang", namaBarang);
                newIntent.putExtra("barangId", String.valueOf(barangId));
                startActivity(newIntent);
            }
        });

    }


    public void backToMenu(View view){
        startActivity(new Intent(DetailBarang.this, RuanganActivity.class));
    }
}