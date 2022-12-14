package id.ac.umn.yapura;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;

import java.io.IOException;

public class AdminUploadImage extends AppCompatActivity {

    private Button uploadImage, captureImage;
    private ImageView uploadImageView;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private final int REQ = 2;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);
        captureImage = findViewById(R.id.captureImage);
        uploadImage = findViewById(R.id.uploadImageBtn);
        uploadImageView = findViewById(R.id.uploadImageView);

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });


        captureImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(takePictureIntent.resolveActivity(getPackageManager())!= null){
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
    }

    private void openGallery() {
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage, REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            uploadImageView.setImageBitmap(imageBitmap);
        }else if(requestCode == REQ && resultCode == RESULT_OK){
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            }catch (IOException e){
                e.printStackTrace();
            }
            uploadImageView.setImageBitmap(bitmap);
        }
    }
}