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

public class JadwalRuanganAdapter extends RecyclerView.Adapter<JadwalRuanganAdapter.HolderItem> {
    List<jadwalRuanganList> jRuangan;
    Context context;

    public JadwalRuanganAdapter(List<jadwalRuanganList> jRuangan, Context context) {
        this.jRuangan = jRuangan;
        this.context = context;
    }

    @NonNull
    @Override
    public HolderItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.jadwal_ruangan_card, parent, false);
        HolderItem holder = new HolderItem(layout);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderItem holder, int position) {
        jadwalRuanganList lRuangan = jRuangan.get(position);

        holder.namaRuangan.setText(lRuangan.getNamaRuangan());
        holder.capacity.setText(String.valueOf(lRuangan.getCapacity()));
        holder.startDate.setText(lRuangan.getStartDate());
        holder.startTime.setText(lRuangan.getStartTime());
        holder.endDate.setText(lRuangan.getEndDate());
        holder.endTime.setText(lRuangan.getEndTime());
        holder.necessity.setText(lRuangan.getNecessity());


        Glide.with(context).load(lRuangan.getFoto()).thumbnail(0.5f).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.foto);


    }

    @Override
    public int getItemCount() {
        return jRuangan.size();
    }

    public class HolderItem extends RecyclerView.ViewHolder{
        ImageView foto;
        TextView namaRuangan, capacity, startDate, startTime, endDate, endTime, necessity;
        Button btnBook;

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

//            btnBook = (Button)  v.findViewById(R.id.btnBook);
        }

    }
}
