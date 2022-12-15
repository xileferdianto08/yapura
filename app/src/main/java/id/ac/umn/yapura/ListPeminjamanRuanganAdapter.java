package id.ac.umn.yapura;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListPeminjamanRuanganAdapter extends RecyclerView.Adapter<ListPeminjamanRuanganAdapter.HolderItem> {
    List<peminjamanRuanganList> jRuangan;
    Context context;

    private static String URL_EDIT_STATUS = "https://yapuraapi.000webhostapp.com/yapura_api/ruangan/update_status_peminjaman.php";

    public ListPeminjamanRuanganAdapter(List<peminjamanRuanganList> jRuangan, Context context) {
        this.jRuangan = jRuangan;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.jadwal_ruangan_card2, parent, false);
        HolderItem holder = new HolderItem(layout);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderItem holder, int position) {
        peminjamanRuanganList lRuangan = jRuangan.get(position);

        holder.namaRuangan.setText(lRuangan.getNamaRuangan());
        holder.capacity.setText(String.valueOf(lRuangan.getCapacity()));
        holder.startDate.setText(lRuangan.getStartDate());
        holder.startTime.setText(lRuangan.getStartTime());
        holder.endDate.setText(lRuangan.getEndDate());
        holder.endTime.setText(lRuangan.getEndTime());
        holder.necessity.setText(lRuangan.getNecessity());
        holder.namaUser.setText(lRuangan.getNamaUser());
        holder.status.setText(lRuangan.getStatus());


        Glide.with(context).load(lRuangan.getFoto()).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.foto);
        holder.ruanganCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Update status peminjaman");
                dialog.setMessage("Persetujuan request peminjaman" + lRuangan.getNamaRuangan()+ " oleh "+lRuangan.getNamaUser());

                dialog.setPositiveButton("Approve", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateStatus(lRuangan.getId(), 1);
                    }
                });
                dialog.setNegativeButton("Reject", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateStatus(lRuangan.getId(), 0);
                    }
                });

                dialog.create();
                dialog.show();


            }
        });


    }

    @Override
    public int getItemCount() {
        return jRuangan.size();
    }


    private void updateStatus(int id, int status){
        StringRequest request = new StringRequest(Request.Method.POST, URL_EDIT_STATUS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONObject resp = obj.getJSONObject("server_response");

                            JSONArray keys = resp.names();

                            for (int i = 0; i < keys.length(); i++) {
                                String status = resp.getString("status").trim();

                                if (status.equals("OK")){
                                    Intent intent = new Intent(context, ListPeminjamanRuangan.class);

                                    context.startActivity(intent);

                                    Toast.makeText(context, "Update status peminjaman berhasil!", Toast.LENGTH_SHORT);
                                }else if(status.equals("FAILED") || status.equals("DB_FAILED")){
                                    Toast.makeText(context, "Terdapat kesalahan pada server", Toast.LENGTH_LONG).show();
                                }

                            }

                        }catch (JSONException ex){
                            ex.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(id));
                params.put("status", String.valueOf(status));

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
    }

    public class HolderItem extends RecyclerView.ViewHolder{
        ImageView foto;
        TextView namaRuangan, capacity, startDate, startTime, endDate, endTime, necessity, namaUser, status;
        RelativeLayout ruanganCard;

        public HolderItem(View v){
            super(v);

            foto = (ImageView) v.findViewById(R.id.fotoRuangan);
            namaRuangan = (TextView) v.findViewById(R.id.namaRuangan);
            capacity = (TextView) v.findViewById(R.id.capacity);
            startDate = (TextView) v.findViewById(R.id.startDate);
            startTime = (TextView) v.findViewById(R.id.startTime);
            endDate = (TextView) v.findViewById(R.id.endDate);
            endTime = (TextView) v.findViewById(R.id.endTime);
            necessity = (TextView) v.findViewById(R.id.necessity);
            namaUser = (TextView)  v.findViewById(R.id.namaUser);
            status = (TextView) v.findViewById(R.id.status);
            ruanganCard = (RelativeLayout) v.findViewById(R.id.ruanganCard);

//            btnBook = (Button)  v.findViewById(R.id.btnBook);
        }

    }
}
