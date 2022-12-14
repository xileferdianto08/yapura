package id.ac.umn.yapura;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



public class AdminAddRuangan extends AppCompatActivity {
    private Button uploadImage, captureImage, btnAdd, btnCancel;
    private ImageView uploadImageView;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private EditText namaRuangan, capacityRuangan, description;

    private final int REQ = 2;
    public Bitmap gambarRuangan;

    private static String URL_ADD_RUANGAN = "https://yapuraapi.000webhostapp.com/yapura_api/ruangan/tambah_ruangan.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_ruangan);
        uploadImage = (Button) findViewById(R.id.uploadImageBtn);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        uploadImageView = (ImageView) findViewById(R.id.uploadImageView);
        namaRuangan = (EditText) findViewById(R.id.nama);
        capacityRuangan = (EditText) findViewById(R.id.capacity);
        description = (EditText) findViewById(R.id.desc);

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namaRuang = namaRuangan.getText().toString();
                String desc = description.getText().toString();
                String capacity = capacityRuangan.getText().toString();

                Bitmap gambar = getGambarFromGallery();

                if(namaRuang.isEmpty()){
                    namaRuangan.setError("Nama ruangan tidak boleh kosong");
                }else if (desc.isEmpty()){
                    description.setError("Deskripsi ruangan tidak boleh kosong");
                } else if(capacity.isEmpty()){
                    capacityRuangan.setError("Kapasitas ruangan tidak boleh kosong");
                } else if(gambar == null){
                    Toast.makeText(AdminAddRuangan.this, "Gambar tidak boleh kosong", Toast.LENGTH_SHORT);
                } else{
                    addNew(namaRuang, desc, Integer.parseInt(capacity), gambar);

                }

            }
        });
    }

    private void openGallery() {
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage, REQ);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }



    private void addNew(String namaRuangan, String desc, int capacity, Bitmap gambarRuangan){
        UploadMultipartData request = new UploadMultipartData(Request.Method.POST, URL_ADD_RUANGAN,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            JSONObject resp = obj.getJSONObject("server_response");

                            JSONArray keys = resp.names();

                            for (int i = 0; i < keys.length(); i++) {
                                String status = resp.getString("status").trim();

                                if(status.equals("OK")){
                                    Intent intent = new Intent(AdminAddRuangan.this, ListRuanganAdmin.class);
                                    startActivity(intent);

                                    Toast.makeText(AdminAddRuangan.this, "Data baru berhasil ditambahkan", Toast.LENGTH_SHORT);

                                }else if(status.equals("DATA_EXIST")){
                                    Toast.makeText(AdminAddRuangan.this, "Data sudah terdaftar!", Toast.LENGTH_SHORT);
                                }else if(status.equals("FAILED") || status.equals("DB FAILED")){
                                    Toast.makeText(AdminAddRuangan.this, "Terdapat kesalahan pada server", Toast.LENGTH_LONG).show();
                                }
                            }

                        } catch (JSONException ex){
                            ex.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminAddRuangan.this, "Error "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama", namaRuangan);
                params.put("maxCapacity", String.valueOf(capacity));
                params.put("desc", desc);


                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("gambar", new DataPart(imagename +".png", getFileDataFromDrawable(gambarRuangan)));

                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Bitmap newGambar = null;
            try {
                newGambar = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);//data gambar dari galeri
                this.gambarRuangan = newGambar;
            } catch (IOException e) {
                e.printStackTrace();
            }
            uploadImageView.setImageBitmap(newGambar);
        }
    }

    private Bitmap getGambarFromGallery(){
        return gambarRuangan;
    }

    public void backToList(View view){
        startActivity(new Intent(AdminAddRuangan.this, ListRuanganAdmin.class));
    }

}
