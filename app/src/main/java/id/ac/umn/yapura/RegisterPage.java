package id.ac.umn.yapura;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterPage extends AppCompatActivity{
    EditText email, password, confPassword, namalengkap;
    Button btnRegister;

    private static String URL_REGISTER = "https://yapuraapi.000webhostapp.com/yapura_api/users/regist_user.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_page);

        email = (EditText) findViewById(R.id.emailstudent);
        namalengkap = (EditText) findViewById(R.id.namalengkap);
        password = (EditText) findViewById(R.id.password);
        confPassword = (EditText) findViewById(R.id.confpassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            String emailKey = email.getText().toString();
            String namaLengkapKey = namalengkap.getText().toString();
            String passwordKey = password.getText().toString();
            String confPwdKey = confPassword.getText().toString();

            if(emailKey.isEmpty()){
                email.setError("Email tidak boleh kosong");
            }else if(passwordKey.isEmpty()){
                password.setError("Password tidak boleh kosong");
            } else if(confPwdKey.isEmpty()){
                confPassword.setError("Confirm Password tidak boleh kosong");
            } else if(namaLengkapKey.isEmpty()){
                namalengkap.setError("Nama Lengkap tidak boleh kosong");
            }else {
                if(!passwordKey.equals(confPwdKey)){
                    Toast.makeText(RegisterPage.this, "Password tidak sama!", Toast.LENGTH_SHORT).show();
                }else {
                    Register(emailKey, passwordKey, namaLengkapKey);
                }

            }
        });
    }

    private void Register(String email, String password, String fullname){
        StringRequest request = new StringRequest(Request.Method.POST, URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject resp = obj.getJSONObject("server_response");

                            JSONArray keys = resp.names();

                            for (int i = 0; i < keys.length(); i++) {
                                String status = resp.getString("status").trim();

                                if(status.equals("OK")){

                                    Intent intent = new Intent(RegisterPage.this, LoginPage.class);
                                    startActivity(intent);

                                    Toast.makeText(RegisterPage.this, "Registrasi berhasil, silahkan login", Toast.LENGTH_SHORT).show();
                                }else if (status.equals("USER_ALREADY_EXIST")){
                                    Toast.makeText(RegisterPage.this, "Email atau Password salah", Toast.LENGTH_LONG).show();
                                }else if (status.equals("EMAIL_INCORRECT_FORMAT")){
                                    Toast.makeText(RegisterPage.this, "Mohon untuk menggunakan email student atau staf UMN!", Toast.LENGTH_LONG).show();
                                }else if(status.equals("FAILED") || status.equals("DB FAILED")){
                                    Toast.makeText(RegisterPage.this, "Terdapat kesalahan pada server", Toast.LENGTH_LONG).show();
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterPage.this, "Error "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params= new HashMap<>();
                params.put("email", email);
                params.put("nama",fullname);
                params.put("password", password);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    public void toMainAct(View view){
        startActivity(new Intent(RegisterPage.this, MainActivity.class));
    }
}