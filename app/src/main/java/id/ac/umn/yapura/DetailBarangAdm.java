package id.ac.umn.yapura;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DetailBarangAdm extends AppCompatActivity {
    int barangId, userId;
    private TextView nama,maxQty, desc;
    private Button btnBook;
    private ImageView fotoBarang;

//    SharedPreferences sessions = getSharedPreferences("user_data", Context.MODE_PRIVATE);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_barang_adm);
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


    }

    public void bookingRuangan(View view){
        final Dialog dialog1 = new Dialog(DetailBarangAdm.this);
        dialog1.setContentView(R.layout.book_ruangan);
        TextView namaBrng = (TextView) dialog1.findViewById(R.id.namaBarang);
        EditText startDate = (EditText) dialog1.findViewById(R.id.startDate);
        EditText startTime = (EditText) dialog1.findViewById(R.id.startTime);
        EditText endDate = (EditText) dialog1.findViewById(R.id.endDate);
        EditText endTime = (EditText) dialog1.findViewById(R.id.endTime);
        EditText capacity = (EditText) dialog1.findViewById(R.id.capacity);
        EditText necessity = (EditText) dialog1.findViewById(R.id.necessity);

        dialog1.show();

    }

    public void backToMenu(View view){
        startActivity(new Intent(DetailBarangAdm.this, RuanganActivity.class));
    }
}