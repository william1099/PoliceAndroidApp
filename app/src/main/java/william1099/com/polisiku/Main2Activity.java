package william1099.com.polisiku;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class Main2Activity extends AppCompatActivity {
    int available;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    public void showProfil(View v) {
        startActivity(new Intent(this, ProfilActivity.class));
    }

    public void nearestpolice(View v) {
        if (isServiceOK())
        startActivity(new Intent(this, PolisiTerdekat.class));
    }

    public boolean isServiceOK() {
        available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (available == ConnectionResult.SUCCESS) {
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Dialog d = GoogleApiAvailability.getInstance().getErrorDialog(this, available, 9001);
            d.show();
        } else {
            Toast.makeText(this, "You can do nothing", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void darurat(View v) {
        startActivity(new Intent(this, Darurat.class));
    }

    public void berita(View v) {
        startActivity(new Intent(this, BeritaPolisi.class));
    }

    public void informasi(View v) {
        startActivity(new Intent(this, Informasi.class));
    }

    public void lapor(View v) {
        startActivity(new Intent(this, LaporActivity.class));
    }
}
