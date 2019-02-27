package william1099.com.polisiku;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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

public class Darurat extends PermissionActivity {
    double lat, lon;
    ApiInterface apiInterface;
    ApiInterface2 apiInterface2;
    FusedLocationProviderClient fusedLocationProviderClient;
    Call<MyPlace> myPlace;
    Call<MyPlace2> myPlace2;
    List<Result> datasource;
    List<String> phone;
    View v;
    TextView cl;
    int counter = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_darurat);

        phone = new ArrayList<>();
        cl = (TextView) findViewById(R.id.calling);
        runTimePermission(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_GROUP_LOCATION);
        runTimePermission(this, new String[]{android.Manifest.permission.CALL_PHONE}, MY_PERMISSION_GROUP_CALL);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        apiInterface2 = ApiClient.getApiClient().create(ApiInterface2.class);

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
                    final String id = datasource.get(i).getPlaceId();
                    //Log.d("testt", "" + id);
                    phone.add(id);

                }
                recursive();
            }
            @Override
            public void onFailure(Call<MyPlace> call, Throwable t) {
                Log.d("fail", ""+ t.getMessage() );
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        finish();
    }

    public void getPhone(String id) {

        myPlace2 = apiInterface2.getData("https://maps.googleapis.com/maps/api/place/details/json?placeid=" + id  + "&key=" + getResources().getString(R.string.api_key));
            myPlace2.enqueue(new Callback<MyPlace2>() {
                @Override
                public void onResponse(Call<MyPlace2> call, Response<MyPlace2> response) {
                    if (response.isSuccessful()) {
                        william1099.com.polisiku.Model2.Result place = response.body().getResult();
                        String telpp = place.getInternational_phone_number();
                        Log.d("testt", "" + telpp);
                        if (telpp != null) telpp = telpp.substring(1);
                        else telpp = "";
                        String new_tel = "";
                        for (int i = 0; i < telpp.length(); i++) {
                            if (Character.isDigit(telpp.charAt(i))) {
                                new_tel += telpp.charAt(i);
                            }
                        }

                        if (new_tel.length() >= 7) {
                            if (ActivityCompat.checkSelfPermission(Darurat.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + new_tel)));
                        } else {
                            recursive();
                        }

                    }
                }
                @Override
                public void onFailure(Call<MyPlace2> call, Throwable t) {

                }
            });
        }

        public void recursive() {
            int mx = datasource.size() - 1;
            counter++;
            if (counter == mx) finish();
            getPhone(phone.get(counter));
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

}
