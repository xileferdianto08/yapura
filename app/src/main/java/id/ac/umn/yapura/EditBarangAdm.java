package id.ac.umn.yapura;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class EditBarangAdm extends AppCompatActivity {
    private Button uploadImage, captureImage, btnAdd, btnCancel;
    private ImageView uploadImageView;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private EditText namaBarang, qtyBarang, description;
    private TextView tvNamaBarang;

    private final int REQ = 2;
    public Bitmap gambarBarang;

    private static String URL_ADD_BARANG = "https://yapuraapi.000webhostapp.com/yapura_api/barang/update_barang.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_barang_adm);
        uploadImage = (Button) findViewById(R.id.uploadImageBtn);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        uploadImageView = (ImageView) findViewById(R.id.uploadImageView);
        namaBarang = (EditText) findViewById(R.id.nama);
        qtyBarang = (EditText) findViewById(R.id.qty);
        description = (EditText) findViewById(R.id.desc);
        captureImage = (Button) findViewById(R.id.captureImage);
        tvNamaBarang = (TextView) findViewById(R.id.tvNamaBarang);

        Intent intent = getIntent();
        tvNamaBarang.setText(intent.getStringExtra("nama"));
        namaBarang.setText(intent.getStringExtra("nama"));
        qtyBarang.setText(intent.getStringExtra("maxQty"));
        description.setText(intent.getStringExtra("desc"));
        int barangId = Integer.parseInt(intent.getStringExtra("id"));
        Glide.with(this).load(intent.getStringExtra("foto")).into(uploadImageView);
        Glide.with(this).asBitmap().load(intent.getStringExtra("foto")).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                gambarBarang = resource;
            }
            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {
            }
        });

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
                    Toast.makeText(EditBarangAdm.this, "Gambar tidak boleh kosong", Toast.LENGTH_SHORT);
                } else{
                    editBrg(barangId,namaBarang1, desc, Integer.parseInt(qty), gambar);

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



    private void editBrg(int barangId, String namaBarang, String desc, int qty, Bitmap gambarBarang){
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
                                    Intent intent = new Intent(EditBarangAdm.this, ListBarangAdmin.class);
                                    startActivity(intent);

                                    Toast.makeText(EditBarangAdm.this, "Data berhasil diedit!", Toast.LENGTH_SHORT);

                                }else if(status.equals("FAILED") || status.equals("DB FAILED")){
                                    Toast.makeText(EditBarangAdm.this, "Terdapat kesalahan pada server", Toast.LENGTH_LONG).show();
                                }
                            }

                        } catch (JSONException ex){
                            ex.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditBarangAdm.this, "Error "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("barangId", String.valueOf(barangId));
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
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
        startActivity(new Intent(EditBarangAdm.this, ListBarangAdmin.class));
    }

}
