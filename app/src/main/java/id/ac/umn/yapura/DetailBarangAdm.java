package id.ac.umn.yapura;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class DetailBarangAdm extends AppCompatActivity {
    int barangId, userId;
    private TextView nama,maxQty, desc;
    private Button btnDel, btnEdit;
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
        btnEdit = (Button) findViewById(R.id.btnEdit);


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

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailBarangAdm.this, EditBarangAdm.class);
                intent.putExtra("id", String.valueOf(id));
                intent.putExtra("nama", namaBarang);
                intent.putExtra("maxQty", maxqty);
                intent.putExtra("desc", descBarang);
                intent.putExtra("foto", foto);
                startActivity(intent);

            }
        });

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(DetailBarangAdm.this);
                dialog.setTitle("Konfirmasi hapus "+namaBarang);
                dialog.setMessage("Apakah anda yakin untuk menghapus item ini?");

                dialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteBarang(barangId);
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


    }

    public void deleteBarang(int id){
        StringRequest request = new StringRequest(Request.Method.POST, URL_DELETE_BRG,
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
                                    Intent intent = new Intent(DetailBarangAdm.this, ListPeminjamanBarang.class);
                                    startActivity(intent);

                                    Toast.makeText(DetailBarangAdm.this, "Item berhasil dihapus!", Toast.LENGTH_SHORT).show();
                                }else if(status.equals("FAILED") || status.equals("DB_FAILED")){
                                    Toast.makeText(DetailBarangAdm.this, "Terdapat kesalahan pada server", Toast.LENGTH_LONG).show();
                                }
                            }
                        }catch (JSONException ex) {
                            ex.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetailBarangAdm.this, "Error "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("barangId", String.valueOf(id));

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }






    public void backToMenu(View view){
        startActivity(new Intent(DetailBarangAdm.this, ListBarangAdmin.class));
    }
}