package id.ac.umn.yapura;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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


public class RuanganActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager manager;;
    List<ruanganList> ruangan;


    private final String URL_LIST_RUANGAN = "https://yapuraapi.000webhostapp.com/yapura_api/ruangan/display_ruangan.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruangan);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
//        requestQueue = Volley.newRequestQueue(getApplicationContext());
        ruangan = new ArrayList<>();

        getData();

        manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        adapter = new RuanganAdapter(ruangan, RuanganActivity.this);
        recyclerView.setAdapter(adapter);


    }

    private void getData(){

        StringRequest request = new StringRequest(Request.Method.GET, URL_LIST_RUANGAN,
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


                                ruanganList newData = new ruanganList();
                                newData.setId(resp.getInt("id"));
                                newData.setNama(resp.getString("nama").trim());
                                newData.setMaxCapacity(resp.getInt("maxCapacity"));
                                newData.setDescription(resp.getString("description").trim());
                                newData.setFoto(resp.getString("gambar"));

                                ruangan.add(newData);
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
    public void toStatus(View view){
        AlertDialog.Builder dialog = new AlertDialog.Builder(RuanganActivity.this);
        dialog.setTitle("Status peminjaman apa yang ingin dibuka?");

        dialog.setPositiveButton("Status alat", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(RuanganActivity.this, StatusBarangUser.class));
            }
        });

        dialog.setNegativeButton("Status ruangan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(RuanganActivity.this, StatusRuanganUser.class));
            }
        });

        dialog.create();
        dialog.show();
    }

    public void backToMainMenu(View view){
        startActivity(new Intent(RuanganActivity.this, PeminjamanPage.class));
    }
}