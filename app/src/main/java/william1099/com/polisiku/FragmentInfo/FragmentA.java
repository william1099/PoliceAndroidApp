package william1099.com.polisiku.FragmentInfo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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


public class FragmentA extends Fragment {
    RecyclerView recyclerView;
    View v;
    Adapter adapter;
    ProgressDialog progressDialog;
    List<objectA> data;
    FirebaseDatabase mDatabase;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragmenta, null);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Memuat data...");
        progressDialog.show();
        mDatabase = FirebaseDatabase.getInstance();

        recyclerView = (RecyclerView) v.findViewById(R.id.recycleA);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        data = new ArrayList<>();
        mDatabase.getReference("penipuan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    data.add(new objectA(child.child("nama").getValue().toString(), child.child("norek").getValue().toString(),
                            child.child("notelp").getValue().toString(), child.child("bank").getValue().toString()));
                }
                adapter = new Adapter(getActivity(), data);
                recyclerView.setAdapter(adapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
