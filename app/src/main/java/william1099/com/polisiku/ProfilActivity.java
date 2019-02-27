package william1099.com.polisiku;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import william1099.com.polisiku.MyFragment.objectFragment;

public class ProfilActivity extends AppCompatActivity {
    TextView snama, semail, salamat, stelp, sjenis;
    ProgressDialog d;
    FirebaseDatabase mDatabase;
    FirebaseUser user;
    FirebaseAuth mAuth;
    objectFragment item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        d = new ProgressDialog(this);
        d.setTitle("Memuat Profil");
        d.show();

        snama = (TextView) findViewById(R.id.nama2);
        semail = (TextView) findViewById(R.id.email3);
        salamat = (TextView) findViewById(R.id.alamat2);
        sjenis = (TextView) findViewById(R.id.jenisk2);
        stelp = (TextView) findViewById(R.id.telp2);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        user = mAuth.getCurrentUser();

        if (user != null) {
            mDatabase.getReference().child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    item = dataSnapshot.getValue(objectFragment.class);
                    snama.setText(item.nama);
                    salamat.setText(item.alamat);
                    semail.setText(item.email);
                    stelp.setText(item.telp);
                    sjenis.setText(item.jenisk);
                    d.dismiss();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(ProfilActivity.this, "Terjadi kesalahan dalam memuat data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void showLaporan(View v) {

    }

    public void logout(View v) {

    }
}
