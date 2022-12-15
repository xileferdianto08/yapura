package id.ac.umn.yapura;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;


public class ListBarangAdmin extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager manager;
    List<barangList> barang;


    private final String URL_LIST_BARANG = "https://yapuraapi.000webhostapp.com/yapura_api/barang/display_barang.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_barang_admin);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
//        requestQueue = Volley.newRequestQueue(getApplicationContext());
        barang = new ArrayList<>();

        getData();

        manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new BarangAdminAdapter(barang, ListBarangAdmin.this);
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
                            for (i = 0; i < getArr.length(); i++) {
                                JSONObject resp = getArr.getJSONObject(i);
                                barangList newData = new barangList();
                                newData.setId(resp.getInt("id"));
                                newData.setNama(resp.getString("nama").trim());
                                newData.setMaxQty(resp.getInt("maxQty"));
                                newData.setDescription(resp.getString("description").trim());
                                newData.setFoto(resp.getString("gambar"));

                                barang.add(newData);
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
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }


    public void toAddBarang(View view){
        startActivity(new Intent(ListBarangAdmin.this, AdminAddBarang.class));
    }


    public void backToMain(View view){
        startActivity(new Intent(ListBarangAdmin.this, AdminPage.class));
    }
    public void toLogout(View view){
        AlertDialog.Builder dialog = new AlertDialog.Builder(ListBarangAdmin.this);
        dialog.setTitle("Anda yakin ingin keluar?");

        dialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sessions = getSharedPreferences("admin_data", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sessions.edit();

                editor.clear();
                editor.apply();


                startActivity(new Intent(ListBarangAdmin.this, MainActivity.class));
            }
        });

        dialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.create();
        dialog.show();
    }
}
