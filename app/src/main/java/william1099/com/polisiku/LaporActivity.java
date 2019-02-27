package william1099.com.polisiku;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LaporActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<objectLapor> data;
    AdapterLapor adapter;
    String currentPermission;
    SharedPreferences sharedPreferences;
    FirebaseDatabase mDatabase;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lapor);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Memuat Laporan...");
        progressDialog.show();

        data = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance();

        recyclerView = (RecyclerView) findViewById(R.id.lapor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mDatabase.getReference("laporan1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {

                    String lat = child.child("lat").getValue().toString();
                    String lon = child.child("lon").getValue().toString();
                    lat.replace(',', '.');
                    lon.replace(',', '.');
                    data.add(new objectLapor(child.child("judul").getValue().toString(), child.child("gambar").getValue().toString(),
                            child.child("desc").getValue().toString(), child.child("tanggal").getValue().toString(),
                            child.child("lokasi").getValue().toString(), child.child("pengirim").getValue().toString(),
                            Double.parseDouble(lat),Double.parseDouble(lon), child.child("type").getValue().toString()));
                }
                adapter = new AdapterLapor(LaporActivity.this, data);
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Log.d("test", "oncreate");
    }


    public void buatlaporan(View v) {
        startActivity(new Intent(this, AddActivity.class));
    }
}
