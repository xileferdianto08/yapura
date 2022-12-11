package id.ac.umn.yapura;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DetailRuanganAdm extends AppCompatActivity {
    int ruangId, userId;
    private TextView nama,maxCapacity, desc;
    private Button btnBook;
    private ImageView fotoRuangan;

//    SharedPreferences sessions = getSharedPreferences("user_data", Context.MODE_PRIVATE);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ruangan_adm);

        nama = (TextView) findViewById(R.id.namaRuangan);
        maxCapacity = (TextView) findViewById(R.id.maxCapacity);
        desc = (TextView) findViewById(R.id.desc);
        fotoRuangan = (ImageView) findViewById(R.id.foto);
        btnBook = (Button) findViewById(R.id.btnBook);

        try {
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
        }catch (NumberFormatException ex){
            ex.printStackTrace();
        }
    }
    public void backToMenu(View view){
        startActivity(new Intent(DetailRuanganAdm.this, RuanganActivity.class));
    }


}