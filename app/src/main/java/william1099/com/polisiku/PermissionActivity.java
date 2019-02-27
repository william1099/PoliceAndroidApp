package william1099.com.polisiku;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;


public class PermissionActivity extends AppCompatActivity {

    protected static final int MY_PERMISSION_GROUP_LOCATION = 10;
    protected static final int MY_PERMISSION_GROUP_CALL = 20;
    public static int MY_PERMISSION_CAMERA = 30;
    public static int MY_PERMISSION_STORAGE = 40;

    public void runTimePermission(Activity activity, final String[] permission, final int code) {
        if (permission.length == 1 && code == MY_PERMISSION_GROUP_LOCATION) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                    Snackbar.make(findViewById(android.R.id.content), "Allow permission to access your location", Snackbar.LENGTH_INDEFINITE)
                            .setAction("enable", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ActivityCompat.requestPermissions(PermissionActivity.this, permission, code);
                                }
                            });
                } else {
                    ActivityCompat.requestPermissions(this, permission, code);
                }
            }
        }

        else if (permission.length == 1 && code == MY_PERMISSION_GROUP_CALL) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
                    Snackbar.make(findViewById(android.R.id.content), "Allow permission to access your location", Snackbar.LENGTH_INDEFINITE)
                            .setAction("enable", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ActivityCompat.requestPermissions(PermissionActivity.this, permission, code);
                                }
                            });
                } else {
                    ActivityCompat.requestPermissions(this, permission, code);
                }
            }
        }
        else if (permission.length == 1 && code == MY_PERMISSION_CAMERA) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

            } else {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                    Snackbar.make(findViewById(android.R.id.content), "Allow permission to access your camera", Snackbar.LENGTH_INDEFINITE)
                            .setAction("enable", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ActivityCompat.requestPermissions(PermissionActivity.this, permission, code);
                                }
                            });
                }

                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_PERMISSION_CAMERA);
            }
        }

        else if (permission.length == 1 && code == MY_PERMISSION_STORAGE) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Snackbar.make(findViewById(android.R.id.content), "Allow permission to access your external storage", Snackbar.LENGTH_INDEFINITE)
                            .setAction("enable", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ActivityCompat.requestPermissions(PermissionActivity.this, permission, code);
                                }
                            });
                }

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_STORAGE);
            }
        }

    }
}
