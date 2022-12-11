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

public class RuanganAdminAdapter extends RecyclerView.Adapter<RuanganAdminAdapter.HolderItem> {
    List<ruanganList> ruangan;
    private Context context;

    public RuanganAdminAdapter(List<ruanganList> ruangan, Context context) {
        this.ruangan = ruangan;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.ruangan_admin_card, parent, false);
        HolderItem holder = new HolderItem(layout);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderItem holder, int position) {
        ruanganList lRuangan = ruangan.get(position);

        holder.nama.setText(lRuangan.getNama());
        holder.maxCapacity.setText(String.valueOf(lRuangan.getMaxCapacity()));

        Glide.with(context).load(lRuangan.getFoto()).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.foto);

        holder.ruanganCard.setOnClickListener( new  View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailRuanganAdm.class);
                intent.putExtra("id", String.valueOf(lRuangan.getId()));
                intent.putExtra("nama", lRuangan.getNama());
                intent.putExtra("maxCapacity", String.valueOf(lRuangan.getMaxCapacity()));
                intent.putExtra("desc", lRuangan.getDescription());
                intent.putExtra("foto", lRuangan.getFoto());
                context.startActivity(intent);

            }
        });

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }

    @Override
    public int getItemCount() {
        return ruangan.size();
    }

    public class HolderItem extends RecyclerView.ViewHolder {
        RelativeLayout ruanganCard;
        ImageView foto, btnUpdate, btnDel;
        TextView nama, maxCapacity;
        Button btnBook;

        public HolderItem(View v){
            super(v);

            foto = (ImageView) v.findViewById(R.id.fotoRuangan);
            nama = (TextView) v.findViewById(R.id.namaRuangan);
            maxCapacity =(TextView) v.findViewById(R.id.maxCapacity);
            ruanganCard = (RelativeLayout) v.findViewById(R.id.ruanganCard);
            btnUpdate = (ImageView) v.findViewById(R.id.btnUpdate);
            btnDel = (ImageView) v.findViewById(R.id.btnDel);
        }

    }
}
