package william1099.com.polisiku.FragmentInfo;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import william1099.com.polisiku.R;

public class Adapter extends RecyclerView.Adapter<Adapter.viewHolder> {
    View v;
    Context context;
    List<objectA> datasource;

    public Adapter(Context ctx, List<objectA> data) {
        this.context = ctx;
        this.datasource = data;
    }
    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_penipuan, null);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, int position) {
        holder.nama.setText(datasource.get(position).nama);
        holder.rek.setText(datasource.get(position).norek);
        holder.telp.setText(datasource.get(position).notelp);
        holder.bank.setText(datasource.get(position).bank);
    }

    @Override
    public int getItemCount() {
        return datasource.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView nama, rek, telp, bank;
        public viewHolder(View itemView) {
            super(itemView);
            nama = (TextView) itemView.findViewById(R.id.namapenipu);
            rek = (TextView) itemView.findViewById(R.id.norek);
            telp = (TextView) itemView.findViewById(R.id.nohp);
            bank = (TextView) itemView.findViewById(R.id.namarek);
        }
    }
}
