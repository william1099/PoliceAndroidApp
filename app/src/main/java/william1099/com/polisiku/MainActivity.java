package william1099.com.polisiku;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import william1099.com.polisiku.MyFragment.FragmentDaftar;
import william1099.com.polisiku.MyFragment.FragmentLogin;

public class MainActivity extends AppCompatActivity {
    FragmentManager manager;
    Fragment frag, frag_login, frag_daftar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        frag_login = new FragmentLogin();
        frag_daftar = new FragmentDaftar();


        manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.frag, frag_login, "Fragment").commit();


    }

    public void signin(View v) {
        
    }
    
    public void signup(View v) {
        manager.beginTransaction().replace(R.id.frag, frag_daftar, "Fragment").addToBackStack(null).commit();
    }
}
