package id.ac.umn.yapura;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toLogin(View view){
        startActivity(new Intent(MainActivity.this, LoginPage.class));
    }

    public void toRegister(View view){
        startActivity(new Intent(MainActivity.this, RegisterPage.class));
    }
    public void toAdminLogin(View view){
        startActivity(new Intent(MainActivity.this, AdminLogin.class));

    }

}