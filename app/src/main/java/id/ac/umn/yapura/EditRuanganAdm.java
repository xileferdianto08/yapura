package id.ac.umn.yapura;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class EditRuanganAdm extends AppCompatActivity {
    private Button uploadImage, captureImage, btnAdd, btnCancel;
    private ImageView uploadImageView;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private EditText namaRuangan, capacityRuangan, description;
    private TextView tvNamaRuangan;

    private final int REQ = 2;
    public Bitmap gambarRuangan;

    private static String URL_EDIT_RUANGAN = "https://yapuraapi.000webhostapp.com/yapura_api/ruangan/update_ruangan.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ruangan_adm);
        uploadImage = (Button) findViewById(R.id.uploadImageBtn);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        uploadImageView = (ImageView) findViewById(R.id.uploadImageView);
        namaRuangan = (EditText) findViewById(R.id.nama);
        capacityRuangan = (EditText) findViewById(R.id.capacity);
        description = (EditText) findViewById(R.id.desc);
        tvNamaRuangan = (TextView) findViewById(R.id.tvNamaRuangan);

            Intent intent = getIntent();
            tvNamaRuangan.setText(intent.getStringExtra("nama"));
            namaRuangan.setText(intent.getStringExtra("nama"));
            capacityRuangan.setText(intent.getStringExtra("maxCapacity"));

            description.setText(intent.getStringExtra("desc"));
            int ruanganId = Integer.parseInt(intent.getStringExtra("id"));
            Glide.with(this).load(intent.getStringExtra("foto")).into(uploadImageView);
            Glide.with(this).asBitmap().load(intent.getStringExtra("foto")).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    gambarRuangan = resource;
                }
                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {
                }
            });
                //this.gambarRuangan = Glide.with(this).asBitmap().load(intent.getStringExtra("foto")).submit().get();



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
                        Toast.makeText(EditRuanganAdm.this, "Gambar tidak boleh kosong", Toast.LENGTH_SHORT);
                    } else{
                        editRuangan(ruanganId, namaRuang, desc, Integer.parseInt(capacity), gambar);

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



    private void editRuangan(int ruanganId, String namaRuangan, String desc, int capacity, Bitmap gambarRuangan){
        UploadMultipartData request = new UploadMultipartData(Request.Method.POST, URL_EDIT_RUANGAN,
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
                                    Intent intent = new Intent(EditRuanganAdm.this, ListRuanganAdmin.class);
                                    startActivity(intent);

                                    Toast.makeText(EditRuanganAdm.this, "Data berhasil diubah!", Toast.LENGTH_SHORT);

                                }else if(status.equals("FAILED") || status.equals("DB FAILED")){
                                    Toast.makeText(EditRuanganAdm.this, "Terdapat kesalahan pada server", Toast.LENGTH_LONG).show();
                                }
                            }

                        } catch (JSONException ex){
                            ex.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditRuanganAdm.this, "Error "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ruanganId", String.valueOf(ruanganId));
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
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
        startActivity(new Intent(EditRuanganAdm.this, ListRuanganAdmin.class));
    }

}
