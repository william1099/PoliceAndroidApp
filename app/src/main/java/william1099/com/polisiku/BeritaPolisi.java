package william1099.com.polisiku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import william1099.com.polisiku.Adapter.AdapterBerita;
import william1099.com.polisiku.Adapter.objectBerita;

public class BeritaPolisi extends AppCompatActivity {
    RecyclerView recyclerView;
    List<objectBerita> data;
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_berita_polisi);

        data = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.berita);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();

        mRef.child("berita").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    Log.d("testt", child.child("Judul").getValue().toString());
                    data.add(new objectBerita(child.child("Judul").getValue().toString(), child.child("Deskripsi").getValue().toString(), child.child("Gambar").getValue().toString(), child.child("Link").getValue().toString()));

                }
                recyclerView.setAdapter(new AdapterBerita(BeritaPolisi.this, data));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("testt", databaseError.getMessage());
                Log.d("testt", databaseError.getDetails());
            }
        });


    }

}
