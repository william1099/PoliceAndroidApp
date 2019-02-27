package william1099.com.polisiku.FragmentInfo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import william1099.com.polisiku.R;


public class FragmentB extends Fragment {
    View v;
    FirebaseDatabase mDatabase;
    RecyclerView recyclerView;
    List<objectB> data;
    AdapterB adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragmentb, null);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.onActivityCreated(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance();

        recyclerView = (RecyclerView) v.findViewById(R.id.recycleB);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        data = new ArrayList<>();
        mDatabase.getReference("kendaraan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    data.add(new objectB(child.child("nama").getValue().toString(),child.child("plat").getValue().toString(),
                            child.child("jenis").getValue().toString(), child.child("status").getValue().toString()));


                }
                adapter = new AdapterB(getActivity(), data);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
