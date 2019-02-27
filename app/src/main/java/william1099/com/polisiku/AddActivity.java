package william1099.com.polisiku;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import william1099.com.polisiku.MyFragment.objectFragment;

public class AddActivity extends PermissionActivity {
    double lat, lon;
    FusedLocationProviderClient fusedLocationProviderClient;
    FirebaseAuth mAuth;
    FirebaseDatabase mDatabase;
    FirebaseUser user;
    FirebaseStorage storage;
    String username, address;
    EditText judul, desc;
    ImageView gambar;
    Uri uri;
    ImageView img;
    Bitmap bitmap;
    int cnt = 0;
    String mCurrentPhotoPath;
    ByteArrayOutputStream out;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mDatabase = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        judul = (EditText) findViewById(R.id.tjudul);
        desc = (EditText) findViewById(R.id.tdesc);
        gambar = (ImageView) findViewById(R.id.timage);
        gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runTimePermission(AddActivity.this, new String[] {Manifest.permission.CAMERA}, MY_PERMISSION_CAMERA);

                if (ActivityCompat.checkSelfPermission(AddActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(AddActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    openCam();
                }
            }


        });

        runTimePermission(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_GROUP_LOCATION);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        user = mAuth.getCurrentUser();

        new task().execute();
    }


    public void deviceLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        final Task location = fusedLocationProviderClient.getLastLocation();
        location.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful() && task.getResult() != null) {

                    Location currentLocation = (Location) task.getResult();
                    lat = currentLocation.getLatitude();
                    lon = currentLocation.getLongitude();
          //          Log.d("test dalam device", "masuk 2");
                    geoCode(lat, lon);

                }
            }
        });

    }


    public void openCam() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= 24) uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider" , getFileLoc());
        else uri = Uri.fromFile(getFileLoc());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, 100);
    }

    public void getUser() {
        mDatabase.getReference("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                objectFragment data = dataSnapshot.getValue(objectFragment.class);
                username = data.nama;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void tambah(View v) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Calendar date = Calendar.getInstance();
        final String time = dateFormat.format(date.getTime());
        final String waktu = new SimpleDateFormat("dd_MM_yyyy_hh:mm:ss").format(date.getTime());
        byte[] encoded = out.toByteArray();
        UploadTask uploadTask = storage.getReference("image/IMG_" + waktu).putBytes(encoded);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                String imgg = taskSnapshot.getDownloadUrl().toString();

                objectLapor objectLapor = new objectLapor(judul.getText().toString(), imgg, desc.getText().toString(), time,
                        address, username, lat, lon, "bitmap");
                String d = mDatabase.getReference("laporan1").push().getKey();
                FirebaseDatabase.getInstance().getReference("laporan1").child(d).setValue(objectLapor);
                Toast.makeText(AddActivity.this, "Laporan berhasil dibuat", Toast.LENGTH_SHORT).show();
                //final String imgg = Base64.encodeToString(encoded, Base64.DEFAULT);
                /*mDatabase.getReference("laporan1").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int size = (int) (dataSnapshot.getChildrenCount() + 1);
                        Log.d("test", "loop ke " + size);
                        *//*objectLapor objectLapor = new objectLapor(judul.getText().toString(), imgg, desc.getText().toString(), time,
                                address, username, lat, lon, "bitmap");
                        FirebaseDatabase.getInstance().getReference("laporan1").child(size + "").setValue(objectLapor);*//*
                        Toast.makeText(AddActivity.this, "Laporan berhasil dibuat", Toast.LENGTH_SHORT).show();
                        AddActivity.this.finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
*/
            }
        });



    }

    public void geoCode(double lat, double lon) {
            Geocoder geocoder = new Geocoder(getApplicationContext());
        //Log.d("test dalam geo", "masuk ");
            List<Address> listAddress = new ArrayList<>();
            try {
                listAddress = geocoder.getFromLocation(lat, lon, 1);
                //Log.d("test dalam geo", "masuk 2");
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (listAddress.size() > 0) {
                //Log.d("test dalam geo", "masuk 3");
                Address alamat = listAddress.get(0);
                address = alamat.getAddressLine(0);
                //Log.d("test dalam geo", "adress : " + address);
            }
    }



    private File getFileLoc() {
        File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        String time = new SimpleDateFormat("ddMMyyyy_hhmmss").format(new Date());

        File image = null;
        try {
            image = File.createTempFile(time, ".jpg", f);

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("test", "" + e.getMessage());
            Log.d("test", "exception");
        }

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }
    private class task extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            deviceLocation();
            getUser();
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSION_CAMERA && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                requestCode == MY_PERMISSION_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCam();
        }
        if (requestCode == MY_PERMISSION_CAMERA && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            runTimePermission(AddActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION_STORAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {

            Uri imageUri = Uri.parse(mCurrentPhotoPath);
            File file = new File(imageUri.getPath());
            try {
                InputStream ims = new FileInputStream(file);
                bitmap = BitmapFactory.decodeStream(ims);
                gambar.setImageBitmap(bitmap);
                out = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.JPEG, 30, out);
            } catch (FileNotFoundException e) {
                return;
            }

            // ScanFile so it will be appeared on Gallery
            MediaScannerConnection.scanFile(AddActivity.this,
                    new String[]{imageUri.getPath()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        }
    }

}
