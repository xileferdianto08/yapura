package id.ac.umn.yapura;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class BarangActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager manager;;
    List<ruanganList> ruangan;


    private final String URL_LIST_BARANG = "https://yapuraapi.000webhostapp.com/yapura_api/barang/display_barang.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barang);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
//        requestQueue = Volley.newRequestQueue(getApplicationContext());
        ruangan = new ArrayList<>();

        getData();

        manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new RuanganAdapter(ruangan, BarangActivity.this);
        recyclerView.setAdapter(adapter);


    }

    private void getData(){

        StringRequest request = new StringRequest(Request.Method.GET, URL_LIST_BARANG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int i = 0;
                        Log.d("JSONResponse", response.toString());
                        Log.d("JSONResponseTotal", String.valueOf(response.length()));

                        try {

                            JSONArray arr = new JSONArray(response);
                            JSONObject obj = arr.getJSONObject(0);

                            JSONArray getArr = obj.getJSONArray("server_response");

                            Toast.makeText(BarangActivity.this, "ada: "+getArr.length(), Toast.LENGTH_SHORT).show();
                            for (i = 0; i < getArr.length(); i++) {
                                JSONObject resp = getArr.getJSONObject(i);
                                Toast.makeText(BarangActivity.this, "di mana gw: "+i, Toast.LENGTH_SHORT).show();

                                ruanganList newData = new ruanganList();
                                newData.setId(resp.getInt("id"));
                                newData.setNama(resp.getString("nama").trim());
                                newData.setMaxCapacity(resp.getInt("maxQty"));
                                newData.setDescription(resp.getString("description").trim());
                                newData.setFoto(resp.getString("gambar"));



                                ruangan.add(newData);



                                Toast.makeText(BarangActivity.this, "adaa: "+ruangan.size(), Toast.LENGTH_SHORT).show();


                            }


                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                        Toast.makeText(BarangActivity.this, "di mana gww woi: "+i, Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERRORRequest", "Error "+error.getMessage());
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }


    public void backToMainMenu(View view){
        startActivity(new Intent(BarangActivity.this, PeminjamanPage.class));
    }
}
