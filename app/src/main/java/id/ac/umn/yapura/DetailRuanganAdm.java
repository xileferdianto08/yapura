package id.ac.umn.yapura;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailRuanganAdm extends AppCompatActivity {
    int ruangId, userId;
    private TextView nama,maxCapacity, desc;
    private Button btnDel, btnEdit;
    private ImageView fotoRuangan;
    List<barangList> barang;
    public Intent intent;

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
        btnEdit = (Button) findViewById(R.id.btnEdit);
        barang = new ArrayList<>();


            intent = getIntent();

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

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DetailRuanganAdm.this, EditRuanganAdm.class);
                    intent.putExtra("id", String.valueOf(id));
                    intent.putExtra("nama", namaRuangan);
                    intent.putExtra("maxQty", maxcapacity);
                    intent.putExtra("desc", descRuangan);
                    intent.putExtra("foto", foto);
                    startActivity(intent);

                }
            });

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

                    dialog.create();
                    dialog.show();
                }
            });

    }
    private void deleteRuangan(int id){
        StringRequest request = new StringRequest(Request.Method.POST, URL_DELETE_RUANG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("JSONResponse", response.toString());
                        Log.d("JSONResponseTotal", String.valueOf(response.length()));
                        try{
                            JSONObject obj = new JSONObject(response);
                            JSONObject resp = obj.getJSONObject("server_response");

                            JSONArray keys = resp.names();

                            for (int i = 0; i < keys.length(); i++) {
                                String status = resp.getString("status").trim();

                                if(status.equals("OK")){
                                    intent = getIntent();
                                    barangList delData = new barangList();
                                    barang.removeIf(n -> (n.getId() == Integer.parseInt(intent.getStringExtra("id"))));
                                    barang.removeIf(n -> (n.getNama() == intent.getStringExtra("nama")));
                                    barang.removeIf(n -> (n.getMaxQty() == Integer.parseInt(intent.getStringExtra("maxCapacity"))));
                                    barang.removeIf(n -> (n.getDescription() == intent.getStringExtra("desc")));
                                    barang.removeIf(n -> (n.getFoto() == intent.getStringExtra("foto")));
                                    Intent intent = new Intent(DetailRuanganAdm.this, ListRuanganAdmin.class);
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

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    public void backToMenu(View view){
        startActivity(new Intent(DetailRuanganAdm.this, ListRuanganAdmin.class));
    }


}
