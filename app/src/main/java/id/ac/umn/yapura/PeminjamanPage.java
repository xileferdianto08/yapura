package id.ac.umn.yapura;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PeminjamanPage extends AppCompatActivity {
    TextView namaUser;
    LinearLayout btnStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peminjaman_page);
        SharedPreferences sessions = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        btnStatus = (LinearLayout) findViewById(R.id.btnStatus);

        namaUser = (TextView) findViewById(R.id.namaUser);

        namaUser.setText(sessions.getString("nama", "User"));

        btnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(PeminjamanPage.this);
                dialog.setTitle("Status peminjaman apa yang ingin dibuka?");

                dialog.setPositiveButton("Status alat", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(PeminjamanPage.this, StatusBarangUser.class));
                    }
                });

                dialog.setPositiveButton("Status ruangan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(PeminjamanPage.this, StatusRuanganUser.class));
                    }
                });

                dialog.create();
                dialog.show();

            }
        });


    }


    public void toListRuangan(View view){
        startActivity(new Intent(PeminjamanPage.this, RuanganActivity.class));
    }

    public void toListBarang(View view){
        startActivity(new Intent(PeminjamanPage.this, BarangActivity.class));
    }

    public void toJadwalRuangan(View view){
        startActivity(new Intent(PeminjamanPage.this, JadwalRuangan.class));
    }

    public void toJadwalBarang(View view){
        startActivity(new Intent(PeminjamanPage.this, JadwalAlat.class));
    }

    public void toLogout(View view){
        SharedPreferences sessions = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sessions.edit();

        editor.clear();
        editor.commit();

        startActivity(new Intent(PeminjamanPage.this, MainActivity.class));
    }


}