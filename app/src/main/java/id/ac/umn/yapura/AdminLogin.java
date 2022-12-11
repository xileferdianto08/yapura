package id.ac.umn.yapura;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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


public class AdminLogin extends AppCompatActivity {

    EditText email, password;
    Button btnLogin;

    private static String URL_LOGIN = "https://yapuraapi.000webhostapp.com/yapura_api/admin/check_login.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);


        btnLogin.setOnClickListener(v -> {
            String emailKey = email.getText().toString();
            String passwordKey = password.getText().toString();

            if(emailKey.isEmpty()){
                email.setError("Email tidak boleh kosong");
            }else if(passwordKey.isEmpty()){
                password.setError("Password tidak boleh kosong");
            } else {
                Login(emailKey, passwordKey);
            }

        });
    }


    private void Login(String email, String password){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject resp = obj.getJSONObject("server_response");

                            JSONArray keys = resp.names();
                            SharedPreferences sessions = getSharedPreferences("admin_data", Context.MODE_PRIVATE);


                            for (int i = 0; i < keys.length(); i++) {
                                String status = resp.getString("status").trim();


                                if(status.equals("LOGIN_SUCCESS")){
                                    SharedPreferences.Editor editor = sessions.edit();
                                    String nama = resp.getString("nama").trim();
                                    String email = resp.getString("email");
                                    int adminId = resp.getInt("adminId");

                                    editor.putString("nama", nama);
                                    editor.putString("email", email);
                                    editor.putInt("adminId", adminId);
                                    editor.apply();

                                    Intent intent = new Intent(AdminLogin.this, AdminPage.class);
                                    startActivity(intent);

                                    String name = sessions.getString("nama", "ga masuk");


                                    Toast.makeText(AdminLogin.this, "Welcome, "+name, Toast.LENGTH_SHORT).show();
                                }else if (status.equals("DATA_INCORRECT")){
                                    Toast.makeText(AdminLogin.this, "Email atau Password salah", Toast.LENGTH_LONG).show();
                                }else if (status.equals("EMAIL_INCORRECT_FORMAT")){
                                    Toast.makeText(AdminLogin.this, "Mohon untuk menggunakan email UMN!", Toast.LENGTH_LONG).show();
                                }else if(status.equals("DATA_NOT_EXIST")){
                                    Toast.makeText(AdminLogin.this, "Data tidak terdaftar", Toast.LENGTH_LONG).show();
                                }


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AdminLogin.this, "Error "+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminLogin.this, "Error "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void toMainAct(View view){
        startActivity(new Intent(AdminLogin.this, MainActivity.class));
    }


    public void toRegister(View view){
        startActivity(new Intent(AdminLogin.this, RegisterAdmin.class));
    }

}