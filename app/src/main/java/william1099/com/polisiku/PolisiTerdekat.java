package william1099.com.polisiku;

import android.*;
import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import william1099.com.polisiku.Model.ApiClient;
import william1099.com.polisiku.Model.ApiInterface;
import william1099.com.polisiku.Model.CMarker;
import william1099.com.polisiku.Model.MyPlace;
import william1099.com.polisiku.Model.Result;
import william1099.com.polisiku.Model2.ApiInterface2;
import william1099.com.polisiku.Model2.MyPlace2;

public class PolisiTerdekat extends PermissionActivity implements OnMapReadyCallback {
    SupportMapFragment supportMapFragment;
    GoogleMap gMap;
    double lat, lon;
    String foto2 = null;
    ApiInterface apiInterface;
    ApiInterface2 apiInterface2;
    FusedLocationProviderClient fusedLocationProviderClient;
    Call<MyPlace> myPlace;
    Call<MyPlace2> myPlace2;
    List<Result> datasource;
    View v;
    TextView nm, jl, telp;
    ImageView gm;
    Button btn;
    Dialog d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polisi_terdekat);

        runTimePermission(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_GROUP_LOCATION);
        runTimePermission(this, new String[]{Manifest.permission.CALL_PHONE}, MY_PERMISSION_GROUP_CALL);

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        d = new Dialog(this);
        v = getLayoutInflater().inflate(R.layout.custom_dialog, null);
        nm = (TextView) v.findViewById(R.id.d_nama);
        jl = (TextView) v.findViewById(R.id.d_jalan);
        telp = (TextView) v.findViewById(R.id.d_telp);
        gm = (ImageView) v.findViewById(R.id.d_gambar);
        btn = (Button) v.findViewById(R.id.d_btn);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        apiInterface2 = ApiClient.getApiClient().create(ApiInterface2.class);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        gMap.setMyLocationEnabled(true);

        Log.d("masuk", "map ready");
        deviceLocation();

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

                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 15f));

                    getNearby("police");
                }
            }
        });

    }

    public void getNearby(String type) {
        //gMap.clear();
        StringBuilder str = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        str.append("location=" + lat + "," + lon);
        str.append("&type=" + type);
        str.append("&radius=" + 5000);
        str.append("&sensor=true");
        str.append("&key=" + getResources().getString(R.string.api_key));
        Log.d("masuk", "nearby");

        myPlace = apiInterface.getData(str.toString());

        myPlace.enqueue(new Callback<MyPlace>() {
            @Override
            public void onResponse(Call<MyPlace> call, Response<MyPlace> response) {
                datasource = response.body().getResults();


                for (int i = 0; i < datasource.size(); i++) {

                    double lat2 = datasource.get(i).getGeometry().getLocation().getLat();
                    double lon2 = datasource.get(i).getGeometry().getLocation().getLng();
                    final String jalan = datasource.get(i).getVicinity();
                    final String nama = datasource.get(i).getName();
                    final String id = datasource.get(i).getPlaceId();

                    // INGAT INI FOTO2 NYA DIGANTI !!!!
                    //final String foto2 = datasource.get(11).getPhotos().get(0).getPhotoReference();
                    /*if (datasource.get(i).getPhotos() != null) {
                        Log.d("gm", "" + i);

                    }*/


                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(new LatLng(lat2, lon2)).title(jalan).icon(BitmapDescriptorFactory.fromResource(R.drawable.policecar));

                    Marker mr = gMap.addMarker(markerOptions);
                    mr.setTag(new CMarker(nama, jalan, i, id));

                    gMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            CMarker c = (CMarker) marker.getTag();
                            nm.setText(c.getNama());
                            jl.setText(c.getJalan());
                            getPhone(c.getPlaceID());
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (ActivityCompat.checkSelfPermission(PolisiTerdekat.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                                        return;
                                    }
                                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telp.getText().toString())));
                                }
                            });
                            if (datasource.get(c.getIdx()).getPhotos() != null) {
                                foto2 = datasource.get(c.getIdx()).getPhotos().get(0).getPhotoReference();
                            }
                            if (foto2 != null) { Picasso.with(PolisiTerdekat.this).load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=100&maxheight=100&photoreference=" + foto2+ "&key=" + getResources().getString(R.string.api_key)).into(gm); foto2 = null; }
                            else Picasso.with(PolisiTerdekat.this).load(R.drawable.ic_pic).resize(100, 100).into(gm);
                            d.setContentView(v);
                            d.show();
                            return false;
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<MyPlace> call, Throwable t) {
                Log.d("fail", ""+ t.getMessage() );
            }
        });

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions.length == 1 && grantResults[0] != PackageManager.PERMISSION_GRANTED && requestCode == MY_PERMISSION_GROUP_LOCATION) {
            Toast.makeText(this, "Permission not allowed to access your location", Toast.LENGTH_SHORT).show();
            finish();
        }
        if (permissions.length == 1 && grantResults[0] != PackageManager.PERMISSION_GRANTED && requestCode == MY_PERMISSION_GROUP_CALL) {
            Toast.makeText(this, "Permission not allowed to using phone call", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void getPhone(String id) {

        myPlace2 = apiInterface2.getData("https://maps.googleapis.com/maps/api/place/details/json?placeid=" + id  + "&key=" + getResources().getString(R.string.api_key));
        myPlace2.enqueue(new Callback<MyPlace2>() {
            @Override
            public void onResponse(Call<MyPlace2> call, Response<MyPlace2> response) {
                if (response.isSuccessful()) {
                    william1099.com.polisiku.Model2.Result place = response.body().getResult();
                    String telpp = place.getInternational_phone_number();
                    if (telpp != null) {
                        telpp = telpp.substring(1);
                        String new_tel = "";
                        for (int i = 0; i < telpp.length(); i++) {
                            if (Character.isDigit(telpp.charAt(i))) {
                                new_tel += telpp.charAt(i);
                            }
                        }
                        telp.setText(new_tel);
                    } else {
                        telp.setText("");
                    }


                }
            }
            @Override
            public void onFailure(Call<MyPlace2> call, Throwable t) {

            }
        });

    }
}
