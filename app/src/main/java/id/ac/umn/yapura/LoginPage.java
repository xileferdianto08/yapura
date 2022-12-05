package id.ac.umn.yapura;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class LoginPage extends AppCompatActivity {

    EditText username, password;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);


        btnLogin.setOnClickListener(v -> {
            String usernameKey = username.getText().toString();
            String passwordKey = password.getText().toString();

            if (usernameKey.equals("jovan") && passwordKey.equals("jovan123")) {
                Toast.makeText(getApplicationContext(), "Login Sukses", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginPage.this, PeminjamanPage.class);

                LoginPage.this.startActivity(intent);
                finish();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginPage.this);
                builder.setMessage("Username dan Password Anda salah!").setNegativeButton("Retry", null).create().show();
            }
        });
    }
}