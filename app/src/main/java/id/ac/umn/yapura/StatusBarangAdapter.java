package id.ac.umn.yapura;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class StatusBarangAdapter extends RecyclerView.Adapter<StatusBarangAdapter.HolderItem> {
    List<jadwalAlatList2> jAlat;
    Context context;

    public StatusBarangAdapter(List<jadwalAlatList2> jAlat, Context context) {
        this.jAlat = jAlat;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.jadwal_alat_card3, parent, false);
        HolderItem holder = new HolderItem(layout);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderItem holder, int position) {
        jadwalAlatList2 lAlat = jAlat.get(position);

        holder.namaBarang.setText(lAlat.getNamaBarang());
        holder.qty.setText(String.valueOf(lAlat.getQty()));
        holder.startDate.setText(lAlat.getStartDate());
        holder.startTime.setText(lAlat.getStartTime());
        holder.endDate.setText(lAlat.getEndDate());
        holder.endTime.setText(lAlat.getEndTime());
        holder.necessity.setText(lAlat.getNecessity());
        holder.status.setText(lAlat.getStatus());


        Glide.with(context).load(lAlat.getFoto()).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.foto);


    }

    @Override
    public int getItemCount() {
        return jAlat.size();
    }

    public class HolderItem extends RecyclerView.ViewHolder{
        ImageView foto;
        TextView namaBarang, qty, startDate, startTime, endDate, endTime, necessity, status;
        Button btnBook;

        public HolderItem(View v){
            super(v);

            foto = (ImageView) v.findViewById(R.id.fotoBarang);
            namaBarang = (TextView) v.findViewById(R.id.namaBarang);
            qty = (TextView) v.findViewById(R.id.qty);
            startDate = (TextView) v.findViewById(R.id.startDate);
            startTime = (TextView) v.findViewById(R.id.startTime);
            endDate = (TextView) v.findViewById(R.id.endDate);
            endTime = (TextView) v.findViewById(R.id.endTime);
            necessity = (TextView) v.findViewById(R.id.necessity);
            status = (TextView) v.findViewById(R.id.status);

//            btnBook = (Button)  v.findViewById(R.id.btnBook);
        }

    }
}
