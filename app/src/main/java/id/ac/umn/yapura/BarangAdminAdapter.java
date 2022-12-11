package id.ac.umn.yapura;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class BarangAdminAdapter extends RecyclerView.Adapter<BarangAdminAdapter.HolderItem> {
    List<barangList> barang;
    private Context context;

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
}
