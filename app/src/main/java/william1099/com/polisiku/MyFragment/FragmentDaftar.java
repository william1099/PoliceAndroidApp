package william1099.com.polisiku.MyFragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import william1099.com.polisiku.R;


public class FragmentDaftar extends Fragment {
    View v;
    EditText nama, email, pass, telp, alamat;
    Spinner jenis;
    String uid;
    Button daftar;
    objectFragment item;
    String snama, semail, spass, stelp, salamat, sjenis;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragmentdaftar, null);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        nama = (EditText) v.findViewById(R.id.nama);
        email = (EditText) v.findViewById(R.id.email2);
        pass = (EditText) v.findViewById(R.id.pass2);
        telp = (EditText) v.findViewById(R.id.telp);
        alamat = (EditText) v.findViewById(R.id.alamat);
        jenis = (Spinner) v.findViewById(R.id.jenisk);
        daftar = (Button) v.findViewById(R.id.daftar);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snama = nama.getText().toString().trim();
                semail = email.getText().toString().trim();
                spass = pass.getText().toString().trim();
                stelp = telp.getText().toString().trim();
                salamat = alamat.getText().toString().trim();
                sjenis = jenis.getSelectedItem().toString().trim();
                item = new objectFragment(snama, semail, stelp, salamat, sjenis, spass);

                if (!snama.equals("") && !semail.equals("") && !spass.equals("") && !stelp.equals("") && !salamat.equals("") && !sjenis.equals("")) {
                    signup();
                } else {
                    Toast.makeText(getActivity(), "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void signup() {
        mAuth.createUserWithEmailAndPassword(semail, spass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    nama.setText("");
                    email.setText("");
                    pass.setText("");
                    alamat.setText("");
                    telp.setText("");
                    mAuth.signInWithEmailAndPassword(semail, spass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sendData();
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frag, new FragmentLogin(), "Fragment").commit();
                            }
                        }
                    });
                    Toast.makeText(getActivity(), "Akun anda telah terdaftar", Toast.LENGTH_SHORT).show();
                    mAuth.signOut();

                } else {
                    Toast.makeText(getActivity(), "Terjadi masalah dalam registrasi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void sendData() {
        FirebaseUser user = mAuth.getCurrentUser();
        mDatabase.getReference().child("users").child(user.getUid()).setValue(item);
    }

}
