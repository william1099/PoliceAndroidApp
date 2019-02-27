package william1099.com.polisiku;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;




public class AdapterLapor extends RecyclerView.Adapter<AdapterLapor.viewHolder> {
    Context context;
    List<objectLapor> datasource;
    View v;
    public static ProgressDialog progressDialog;
    public AdapterLapor(Context ctx, List<objectLapor> data) {
        this.context = ctx;
        this.datasource = data;
        progressDialog = new ProgressDialog(ctx);
        progressDialog.setTitle("Memuat Masalah...");
    }
    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_lapor, null);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(viewHolder holder, final int position) {
        holder.judul.setText(datasource.get(position).judul);
        holder.desc.setText(datasource.get(position).desc);
        holder.tanggal.setText(datasource.get(position).tanggal);
        holder.pengirim.setText(datasource.get(position).pengirim);
        holder.lokasi.setText(datasource.get(position).lokasi);
        Picasso.with(context).load(datasource.get(position).gambar).placeholder(R.drawable.ic_image).into(holder.gambar);
        /*if (datasource.get(position).type.equals("link")) {
            Picasso.with(context).load(datasource.get(position).gambar).placeholder(R.drawable.ic_image).into(holder.gambar);
        } else {
            byte[] decoded = Base64.decode(datasource.get(position).gambar, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
            holder.gambar.setImageBitmap(bitmap);
        }*/


        holder.ll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MapsActivity.class);
                intent.putExtra("lat", datasource.get(position).lat);
                intent.putExtra("lon", datasource.get(position).lon);
                Log.d("adapter lat", datasource.get(position).lat + "");
                Log.d("adapter lon", datasource.get(position).lon + "");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datasource.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView judul, desc, lokasi, tanggal, pengirim;
        ImageView gambar;
        LinearLayout ll;
        public viewHolder(View itemView) {
            super(itemView);
            judul = (TextView) itemView.findViewById(R.id.ljudul);
            desc = (TextView) itemView.findViewById(R.id.ldesc);
            gambar = (ImageView) itemView.findViewById(R.id.limage);
            tanggal = (TextView) itemView.findViewById(R.id.ltanggal);
            lokasi = (TextView) itemView.findViewById(R.id.llokasi);
            pengirim = (TextView) itemView.findViewById(R.id.lkirim);
            ll = (LinearLayout) itemView.findViewById(R.id.llinearlay);

        }
    }
}
