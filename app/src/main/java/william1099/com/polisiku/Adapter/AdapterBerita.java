package william1099.com.polisiku.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import william1099.com.polisiku.BeritaPolisi;
import william1099.com.polisiku.Main3Activity;
import william1099.com.polisiku.R;

/**
 * Created by W I N D O W S   8 on 17/03/2018.
 */

public class AdapterBerita extends RecyclerView.Adapter<AdapterBerita.viewHolder> {
    Context context;
    List<objectBerita> datasource;
    View v;
    public static ProgressDialog progressDialog;
    public AdapterBerita(Context ctx, List<objectBerita> data) {
        this.context = ctx;
        this.datasource = data;
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setTitle("Memuat Berita...");
    }
    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_berita, null);
        return new viewHolder(v);
}

    @Override
    public void onBindViewHolder(viewHolder holder, final int position) {
        holder.judul.setText(datasource.get(position).judul);
        holder.desc.setText(datasource.get(position).desc);
        Picasso.with(context).load(datasource.get(position).gambar).placeholder(R.drawable.ic_image).into(holder.gambar);
        holder.ll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                progressDialog.show();
                Intent intent = new Intent(context, Main3Activity.class);
                intent.putExtra("link", datasource.get(position).link);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datasource.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView judul, desc;
        ImageView gambar;
        LinearLayout ll;
        public viewHolder(View itemView) {
            super(itemView);
            judul = (TextView) itemView.findViewById(R.id.bjudul);
            desc = (TextView) itemView.findViewById(R.id.bdesc);
            gambar = (ImageView) itemView.findViewById(R.id.bimage);
            ll = (LinearLayout) itemView.findViewById(R.id.blinearlay);
        }
    }
}
