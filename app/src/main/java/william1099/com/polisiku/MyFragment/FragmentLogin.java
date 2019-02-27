package william1099.com.polisiku.MyFragment;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import william1099.com.polisiku.Main2Activity;
import william1099.com.polisiku.MainActivity;
import william1099.com.polisiku.R;


public class FragmentLogin extends Fragment {
    View v;
    EditText email, pass;
    Button masuk;
    FirebaseAuth mAuth;
    FirebaseUser user;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragmentlogin, null);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user != null) startActivity(new Intent(getActivity(), Main2Activity.class));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (user != null) getActivity().finish();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        email = (EditText) v.findViewById(R.id.email);
        pass = (EditText) v.findViewById(R.id.password);
        masuk = (Button) v.findViewById(R.id.signin);
        masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               signin(email.getText().toString(), pass.getText().toString());
            }
        });

    }

    public void signin(String email, String password) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        user = mAuth.getCurrentUser();
                        startActivity(new Intent(getActivity(), Main2Activity.class));
                    } else {
                        Toast.makeText(getActivity(), "Email atau password salah", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
}
