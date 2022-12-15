package id.ac.umn.yapura;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DetailRuanganAdm extends AppCompatActivity {
    int ruangId, userId;
    private TextView nama,maxCapacity, desc;
    private Button btnDel;
    private ImageView fotoRuangan;

//    SharedPreferences sessions = getSharedPreferences("user_data", Context.MODE_PRIVATE);

    private final String URL_DELETE_RUANG = "https://yapuraapi.000webhostapp.com/yapura_api/ruangan/delete_ruangan.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_ruangan_adm);

        nama = (TextView) findViewById(R.id.namaRuangan);
        maxCapacity = (TextView) findViewById(R.id.maxCapacity);
        desc = (TextView) findViewById(R.id.desc);
        fotoRuangan = (ImageView) findViewById(R.id.foto);
        btnDel = (Button) findViewById(R.id.btnDel);

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

            btnDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(DetailRuanganAdm.this);
                    dialog.setTitle("Konfirmasi hapus "+namaRuangan);
                    dialog.setMessage("Apakah anda yakin untuk menghapus item ini?");

                    dialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteRuangan(ruangId);
                        }
                    });

                    dialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    dialog.create().show();
                }
            });
        }catch (NumberFormatException ex){
            ex.printStackTrace();
        }
    }
    public void deleteRuangan(int id){
        StringRequest request = new StringRequest(Request.Method.POST, URL_DELETE_RUANG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject obj = new JSONObject(response);
                            JSONObject resp = obj.getJSONObject("server_response");

                            JSONArray keys = resp.names();

                            for (int i = 0; i < keys.length(); i++) {
                                String status = resp.getString("status").trim();

                                if(status.equals("OK")){
                                    Intent intent = new Intent(DetailRuanganAdm.this, ListPeminjamanBarang.class);
                                    startActivity(intent);

                                    Toast.makeText(DetailRuanganAdm.this, "Item berhasil dihapus!", Toast.LENGTH_SHORT).show();
                                }else if(status.equals("FAILED") || status.equals("DB_FAILED")){
                                    Toast.makeText(DetailRuanganAdm.this, "Terdapat kesalahan pada server", Toast.LENGTH_LONG).show();
                                }
                            }
                        }catch (JSONException ex) {
                            ex.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailRuanganAdm.this, "Error "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ruangId", String.valueOf(id));

                return params;
            }
        };
    }

    public void backToMenu(View view){
        startActivity(new Intent(DetailRuanganAdm.this, RuanganActivity.class));
    }


}