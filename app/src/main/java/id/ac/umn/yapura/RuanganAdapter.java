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

public class RuanganAdapter extends RecyclerView.Adapter<RuanganAdapter.HolderItem> {
    List<ruanganList> ruangan;
    Context context;

    public RuanganAdapter(List<ruanganList> ruangan, Context context) {
        this.ruangan = ruangan;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.ruangan_card, parent, false);
        HolderItem holder = new HolderItem(layout);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderItem holder, int position) {
        ruanganList lRuangan = ruangan.get(position);

        holder.nama.setText(lRuangan.getNama());
        holder.maxCapacity.setText(String.valueOf(lRuangan.getMaxCapacity()));

        Glide.with(context).load(lRuangan.getFoto()).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.foto);


    }

    @Override
    public int getItemCount() {
        return ruangan.size();
    }

    public class HolderItem extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView foto;
        TextView nama, maxCapacity;
        Button btnBook;

        public HolderItem(View v){
            super(v);

            foto = (ImageView) v.findViewById(R.id.fotoRuangan);
            nama = (TextView) v.findViewById(R.id.namaRuangan);
            maxCapacity =(TextView) v.findViewById(R.id.maxCapacity);
//            btnBook = (Button)  v.findViewById(R.id.btnBook);
        }


        @Override
        public void onClick(View v) {
            String post = String.valueOf(getLayoutPosition());
            int id = ruangan.get(getLayoutPosition()).getId();
            String nama = ruangan.get(getLayoutPosition()).getNama();
            String desc = ruangan.get(getLayoutPosition()).getDescription();
            int maxCapacity = ruangan.get(getLayoutPosition()).getMaxCapacity();
            String foto = ruangan.get(getLayoutPosition()).getFoto();

            Intent ruanganDetail = new Intent(itemView.getContext(), DetailRuangan.class);
            ruanganDetail.putExtra("id", id);
            ruanganDetail.putExtra("nama", nama);
            ruanganDetail.putExtra("desc", desc);
            ruanganDetail.putExtra("maxCapacity", maxCapacity);
            ruanganDetail.putExtra("foto", foto);
            itemView.getContext().startActivity(ruanganDetail);

        }
    }
}
