package id.ac.umn.yapura;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DetailBarangAdm extends AppCompatActivity {
    int barangId, userId;
    private TextView nama,maxQty, desc;
    private Button btnDel;
    private ImageView fotoBarang;

//    SharedPreferences sessions = getSharedPreferences("user_data", Context.MODE_PRIVATE);

    private final String URL_DELETE_BRG = "https://yapuraapi.000webhostapp.com/yapura_api/barang/delete_barang.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_barang_adm);
        String namaBarang;

        nama = (TextView) findViewById(R.id.namaBarang);
        maxQty = (TextView) findViewById(R.id.maxQty);
        desc = (TextView) findViewById(R.id.desc);
        fotoBarang = (ImageView) findViewById(R.id.foto);
        btnDel = (Button) findViewById(R.id.btnDel);

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

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(DetailBarangAdm.this);
                dialog.setTitle("Delte");
            }
        });


    }

    public void deleteBarang(int id){

    }






    public void backToMenu(View view){
        startActivity(new Intent(DetailBarangAdm.this, RuanganActivity.class));
    }
}