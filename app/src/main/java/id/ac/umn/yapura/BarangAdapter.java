package id.ac.umn.yapura;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.LinkedList;
import java.util.List;

public class BarangAdapter extends RecyclerView.Adapter<BarangAdapter.HolderItem> {
    List<barangList> barang;
    Context context;

    public BarangAdapter(List<barangList> barang, Context context) {
        this.barang = barang;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.barang_card, parent, false);
        HolderItem holder = new HolderItem(layout);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderItem holder, int position) {
        barangList lBarang = barang.get(position);

        holder.nama.setText(lBarang.getNama());
        holder.maxQty.setText(String.valueOf(lBarang.getMaxQty()));

        Glide.with(context).load(lBarang.getFoto()).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.foto);


    }

    @Override
    public int getItemCount() {
        return barang.size();
    }

    public class HolderItem extends RecyclerView.ViewHolder{
        ImageView foto;
        TextView nama, maxQty;
        Button btnBook;

        public HolderItem(View v){
            super(v);

            foto = (ImageView) v.findViewById(R.id.fotoRuangan);
            nama = (TextView) v.findViewById(R.id.namaRuangan);
            maxQty =(TextView) v.findViewById(R.id.maxQty);
//            btnBook = (Button)  v.findViewById(R.id.btnBook);
        }

    }
}
