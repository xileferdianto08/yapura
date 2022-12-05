package id.ac.umn.yapura;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
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


public class LoginPage extends AppCompatActivity {

    EditText email, password;
    Button btnLogin;
    private static String URL_LOGIN = "http://192.168.1.180/yapura_api/users/check_login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);


        btnLogin.setOnClickListener(v -> {
            String emailKey = email.getText().toString();
            String passwordKey = password.getText().toString();

            if(!emailKey.isEmpty() || !passwordKey.isEmpty()){
                Login(emailKey, passwordKey);
            } else {
                email.setError("Email tidak boleh kosong");
                password.setError("Password tidak boleh kosong");
            }


        });
    }


    private void Login(String email, String password){
//        btnLogin.setVisibility(View.GONE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject resp = obj.getJSONObject("server_response");

                            JSONArray keys = resp.names();

                            for (int i = 0; i < keys.length(); i++) {
//                                String key = keys.getString(i);
//                                String value = resp.getString(key);
//
//                                Toast.makeText(LoginPage.this, key+" : " + value, Toast.LENGTH_LONG).show();

                                String status = resp.getString("status").trim();


                                if(status.equals("LOGIN_SUCCESS")){
                                    String nama = resp.getString("nama").trim();
                                    String email = resp.getString("email");
                                    int userId = resp.getInt("userId");
                                    Toast.makeText(LoginPage.this, "Welcome, "+nama+" UserID: "+userId, Toast.LENGTH_SHORT).show();
                                }else if (status.equals("DATA_INCORRECT")){
                                    Toast.makeText(LoginPage.this, "Email atau Password salah", Toast.LENGTH_LONG).show();
                              }else if (status.equals("EMAIL_INCORRECT_FORMAT")){
                                    Toast.makeText(LoginPage.this, "Mohon untuk menggunakan email student atau staf UMN!", Toast.LENGTH_LONG).show();
                                } else if(status.equals("DATA_NOT_EXIST")){
                                    Toast.makeText(LoginPage.this, "Data tidak ada", Toast.LENGTH_LONG).show();
                                }


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginPage.this, "Error "+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginPage.this, "Error "+error.toString(), Toast.LENGTH_SHORT).show();
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

}