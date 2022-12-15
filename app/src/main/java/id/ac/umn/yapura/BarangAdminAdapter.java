package id.ac.umn.yapura;

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
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BarangAdminAdapter extends RecyclerView.Adapter<BarangAdminAdapter.HolderItem> {
    List<barangList> barang;
    private Context context;
    private final String URL_DELETE_BRG = "https://yapuraapi.000webhostapp.com/yapura_api/barang/delete_barang.php";
    public BarangAdminAdapter(List<barangList> barang, Context context) {
        this.barang = barang;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.barang_admin_card, parent, false);
        HolderItem holder = new HolderItem(layout);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderItem holder, int position) {
        barangList lBarang = barang.get(position);

        holder.nama.setText(lBarang.getNama());
        holder.maxQty.setText(String.valueOf(lBarang.getMaxQty()));

        Glide.with(context).load(lBarang.getFoto()).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.foto);

        holder.barangCard.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailBarangAdm.class);
                intent.putExtra("id", String.valueOf(lBarang.getId()));
                intent.putExtra("nama", lBarang.getNama());
                intent.putExtra("maxQty", String.valueOf(lBarang.getMaxQty()));
                intent.putExtra("desc", lBarang.getDescription());
                intent.putExtra("foto", lBarang.getFoto());
                context.startActivity(intent);
            }
        });

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, EditBarangAdm.class);
                intent.putExtra("id", String.valueOf(lBarang.getId()));
                intent.putExtra("nama", lBarang.getNama());
                intent.putExtra("maxQty", String.valueOf(lBarang.getMaxQty()));
                intent.putExtra("desc", lBarang.getDescription());
                intent.putExtra("foto", lBarang.getFoto());
                context.startActivity(intent);

            }
        });

        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Konfirmasi hapus "+lBarang.getNama());
                dialog.setMessage("Apakah anda yakin untuk menghapus item ini?");

                dialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteBarang(lBarang.getId());
                    }
                });

                dialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.create().show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return barang.size();
    }

    public class HolderItem extends RecyclerView.ViewHolder{
        ImageView foto, btnUpdate, btnDel;
        RelativeLayout barangCard;
        TextView nama, maxQty;


        public HolderItem(View v){
            super(v);

            foto = (ImageView) v.findViewById(R.id.fotoRuangan);
            nama = (TextView) v.findViewById(R.id.namaRuangan);
            maxQty =(TextView) v.findViewById(R.id.maxQty);
            barangCard = (RelativeLayout) v.findViewById(R.id.barangCard);
            btnUpdate = (ImageView) v.findViewById(R.id.btnUpdate);
            btnDel = (ImageView) v.findViewById(R.id.btnDel);
        }

    }

    public void deleteBarang(int id){
        StringRequest request = new StringRequest(Request.Method.POST, URL_DELETE_BRG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject obj = new JSONObject(response);
                            JSONObject resp = obj.getJSONObject("server_response");

                            JSONArray keys = resp.names();

                            for (int i = 0; i < keys.length(); i++) {
                                String status = resp.getString("status").trim();

                                if(status.equals("OK")){
                                    Intent intent = new Intent(context, ListPeminjamanBarang.class);
                                    context.startActivity(intent);

                                    Toast.makeText(context, "Item berhasil dihapus!", Toast.LENGTH_SHORT).show();
                                }else if(status.equals("FAILED") || status.equals("DB_FAILED")){
                                    Toast.makeText(context, "Terdapat kesalahan pada server", Toast.LENGTH_LONG).show();
                                }
                            }
                        }catch (JSONException ex) {
                            ex.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("barangId", String.valueOf(id));

                return params;
            }
        };
    }
}
