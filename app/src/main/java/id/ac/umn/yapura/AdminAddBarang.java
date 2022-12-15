package id.ac.umn.yapura;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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


public class AdminAddBarang extends AppCompatActivity {
    private Button uploadImage, captureImage, btnAdd, btnCancel;
    private ImageView uploadImageView;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private EditText namaBarang, qtyBarang, description;

    private final int REQ = 2;
    public Bitmap gambarBarang;

    private static String URL_ADD_BARANG = "https://yapuraapi.000webhostapp.com/yapura_api/barang/tambah_barang.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_barang);
        uploadImage = (Button) findViewById(R.id.uploadImageBtn);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        uploadImageView = (ImageView) findViewById(R.id.uploadImageView);
        namaBarang = (EditText) findViewById(R.id.nama);
        qtyBarang = (EditText) findViewById(R.id.qty);
        description = (EditText) findViewById(R.id.desc);
        captureImage = (Button) findViewById(R.id.captureImage);

        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(takePictureIntent.resolveActivity(getPackageManager())!= null){
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namaBarang1 = namaBarang.getText().toString();
                String desc = description.getText().toString();
                String qty = qtyBarang.getText().toString();

                Bitmap gambar = getGambarFromGallery();

                if(namaBarang1.isEmpty()){
                    namaBarang.setError("Nama ruangan tidak boleh kosong");
                }else if (desc.isEmpty()){
                    description.setError("Deskripsi ruangan tidak boleh kosong");
                } else if(qty.isEmpty()){
                    qtyBarang.setError("Kapasitas ruangan tidak boleh kosong");
                } else if(gambar == null){
                    Toast.makeText(AdminAddBarang.this, "Gambar tidak boleh kosong", Toast.LENGTH_SHORT);
                } else{
                    addNew(namaBarang1, desc, Integer.parseInt(qty), gambar);

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



    private void addNew(String namaBarang, String desc, int qty, Bitmap gambarBarang){
        UploadMultipartData request = new UploadMultipartData(Request.Method.POST, URL_ADD_BARANG,
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
                                    Intent intent = new Intent(AdminAddBarang.this, ListBarangAdmin.class);
                                    startActivity(intent);

                                    Toast.makeText(AdminAddBarang.this, "Data baru berhasil ditambahkan", Toast.LENGTH_SHORT);

                                }else if(status.equals("DATA_EXIST")){
                                    Toast.makeText(AdminAddBarang.this, "Data sudah terdaftar!", Toast.LENGTH_SHORT);
                                }else if(status.equals("FAILED") || status.equals("DB FAILED")){
                                    Toast.makeText(AdminAddBarang.this, "Terdapat kesalahan pada server", Toast.LENGTH_LONG).show();
                                }
                            }

                        } catch (JSONException ex){
                            ex.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminAddBarang.this, "Error "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nama", namaBarang);
                params.put("maxQty", String.valueOf(qty));
                params.put("desc", desc);


                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("gambar", new DataPart(imagename +".png", getFileDataFromDrawable(gambarBarang)));

                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            this.gambarBarang = imageBitmap;
            uploadImageView.setImageBitmap(imageBitmap);
        }else if(requestCode == REQ && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Bitmap newGambar = null;
            try {
                newGambar = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);//data gambar dari galeri
                this.gambarBarang = newGambar;
            } catch (IOException e) {
                e.printStackTrace();
            }
            uploadImageView.setImageBitmap(newGambar);
        }
    }

    private Bitmap getGambarFromGallery(){
        return gambarBarang;
    }

    public void backToList(View view){
        startActivity(new Intent(AdminAddBarang.this, ListBarangAdmin.class));
    }

}
