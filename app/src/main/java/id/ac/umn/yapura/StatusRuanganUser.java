package id.ac.umn.yapura;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StatusRuanganUser extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager manager;
    List<jadwalRuanganList2> jadwalRuangan;


    private final String URL_JADWAL_RUANGAN_USER = "https://yapuraapi.000webhostapp.com/yapura_api/ruangan/user_borrow_ruang.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_ruangan_user);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
//        requestQueue = Volley.newRequestQueue(getApplicationContext());
        jadwalRuangan = new ArrayList<>();
        SharedPreferences sessions = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        int userId = Integer.parseInt(sessions.getString("userId", "0"));

        getData(userId);

        manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new StatusRuanganAdapter(jadwalRuangan, StatusRuanganUser.this);
        recyclerView.setAdapter(adapter);


    }

    private void getData(int userId){

        StringRequest request = new StringRequest(Request.Method.POST, URL_JADWAL_RUANGAN_USER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int i = 0;
                        Log.d("JSONResponse", response.toString());
                        Log.d("JSONResponseTotal", String.valueOf(response.length()));

                        try {

                            JSONArray arr = new JSONArray(response);
                            JSONObject obj = arr.getJSONObject(0);

                            JSONArray getArr = obj.getJSONArray("data_peminjaman_r");
                            String status = obj.getString("status");



                                for (i = 0; i < getArr.length(); i++) {
                                    JSONObject resp = getArr.getJSONObject(i);
                                    jadwalRuanganList2 newData = new jadwalRuanganList2();
                                    newData.setNamaRuangan(resp.getString("namaRuangan"));
                                    newData.setCapacity(resp.getInt("capacity"));
                                    newData.setStartDate(resp.getString("startDate"));
                                    newData.setEndDate(resp.getString("endDate"));
                                    newData.setStartTime(resp.getString("startTime"));
                                    newData.setEndTime(resp.getString("endTime"));
                                    newData.setStatus(resp.getString("status"));
                                    newData.setNecessity(resp.getString("necessity"));
                                    newData.setFoto(resp.getString("gambar"));

                                    jadwalRuangan.add(newData);
                                }

                                if(status.equals("DATA_UNAVAIL")){
                                    Toast.makeText(StatusRuanganUser.this, "Anda belum melakukan peminjaman ruangan!", Toast.LENGTH_LONG).show();
                                }else if (status.equals("DB FAILED")){
                                    Toast.makeText(StatusRuanganUser.this, "Terdapat kesalahan pada server", Toast.LENGTH_LONG).show();
                                }


                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERRORRequest", "Error "+error.getMessage());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userId", String.valueOf(userId));

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }


    public void backToMainMenu(View view){
        startActivity(new Intent(StatusRuanganUser.this, PeminjamanPage.class));
    }
}
