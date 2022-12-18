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


public class StatusBarangUser extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager manager;
    List<jadwalAlatList2> jadwalAlat;


    private final String URL_JADWAL_BARANG = "https://yapuraapi.000webhostapp.com/yapura_api/barang/user_borrow_barang.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_barang_user);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
//        requestQueue = Volley.newRequestQueue(getApplicationContext());
        jadwalAlat = new ArrayList<>();
        SharedPreferences session = getSharedPreferences("user_data", Context.MODE_PRIVATE);
        int userId = Integer.parseInt(session.getString("userId", "0"));

        getData(userId);

        manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new StatusBarangAdapter(jadwalAlat, StatusBarangUser.this);
        recyclerView.setAdapter(adapter);


    }

    private void getData(int userId){

        StringRequest request = new StringRequest(Request.Method.POST, URL_JADWAL_BARANG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int i = 0;
                        Log.d("JSONResponse", response.toString());
                        Log.d("JSONResponseTotal", String.valueOf(response.length()));

                        try {

                                JSONArray arr = new JSONArray(response);
                                JSONObject obj = arr.getJSONObject(0);

                                JSONArray getArr = obj.getJSONArray("data_peminjaman_b");

                                for (i = 0; i < getArr.length(); i++) {
                                    JSONObject resp = getArr.getJSONObject(i);
                                    jadwalAlatList2 newData = new jadwalAlatList2();
                                    newData.setNamaBarang(resp.getString("namaBarang"));
                                    newData.setQty(resp.getInt("qty"));
                                    newData.setStartDate(resp.getString("startDate"));
                                    newData.setEndDate(resp.getString("endDate"));
                                    newData.setStartTime(resp.getString("startTime"));
                                    newData.setEndTime(resp.getString("endTime"));
                                    newData.setStatus(resp.getString("status"));
                                    newData.setNecessity(resp.getString("necessity"));
                                    newData.setFoto(resp.getString("gambar"));

                                    jadwalAlat.add(newData);
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
        startActivity(new Intent(StatusBarangUser.this, PeminjamanPage.class));
    }
}
