package william1099.com.polisiku.FragmentInfo;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import william1099.com.polisiku.R;

public class AdapterB extends RecyclerView.Adapter<AdapterB.viewHolder> {
    View v;
    Context context;
    List<objectB> datasource;

    public AdapterB(Context ctx, List<objectB> data) {
        this.context = ctx;
        this.datasource = data;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_kendaraan, null);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        holder.nama.setText(datasource.get(position).nama);
        holder.bk.setText(datasource.get(position).BK);
        holder.jeniskendaraan.setText(datasource.get(position).jenis);
        if (!datasource.get(position).status.equals("ditemukan")) {
            holder.status.setBackgroundColor(context.getResources().getColor(R.color.merah));
            holder.status.setText("Hilang");
        }
    }

    @Override
    public int getItemCount() {
        return datasource.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder {
        TextView nama, bk, status, jeniskendaraan;
        public viewHolder(View itemView) {
            super(itemView);
            nama = (TextView) itemView.findViewById(R.id.namakendaraan);
            jeniskendaraan = (TextView) itemView.findViewById(R.id.jeniskendaraan);
            bk = (TextView) itemView.findViewById(R.id.bk);
            status = (TextView) itemView.findViewById(R.id.statuss);

        }
    }
}

