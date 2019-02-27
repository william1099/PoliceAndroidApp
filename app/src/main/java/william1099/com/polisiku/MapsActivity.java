package william1099.com.polisiku;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends PermissionActivity {
    private GoogleMap gMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static String lastOperation;
    String currentPermission = "falsee", tr = "truee";
    SharedPreferences sharedPreferences;
    List<Address> listAddress;
    AutoCompleteTextView searchForm;
    GeoDataClient mGeoDataClient;
    LatLngBounds latLngBounds;
    double elat, elon;
    double slat, slon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        elat = getIntent().getExtras().getDouble("lat");
        elon = getIntent().getExtras().getDouble("lon");

        searchForm = (AutoCompleteTextView) findViewById(R.id.edit1);

        // autocomplete
        mGeoDataClient = Places.getGeoDataClient(this, null);
        latLngBounds =  new LatLngBounds(new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));
        searchForm.setAdapter(new PlaceAutoCompleteAdapter(this, mGeoDataClient,latLngBounds, null));

        if (Build.VERSION.SDK_INT <= 22) {
            currentPermission = "truee";
            sharedPreferences = getSharedPreferences("maps",Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString("currentPermission", currentPermission);
            edit.commit();
            Log.d("testt", "21");
        }
        currentPermission = getSharedPreferences("maps", MODE_PRIVATE).getString("currentPermission", null);
        if (currentPermission.equals("truee"))  beginMap();

        loadMap();

        searchForm.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    geoCode();
                }
                return false;
            }
        });

    }

    public void beginMap() {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                gMap = googleMap;
                if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                MapsActivity.this.deviceLocation();
                gMap.setMyLocationEnabled(true);
                gMap.getUiSettings().setMyLocationButtonEnabled(false);

            }
        });
    }

    public void deviceLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task location = fusedLocationProviderClient.getLastLocation();
        location.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                Location currentLocation = (Location) task.getResult();
                if (currentLocation != null)
                    moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 17f);
                else Toast.makeText(MapsActivity.this, "Make sure you activate your GPS", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void moveCamera(LatLng latLng, float zoom) {
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        slat = latLng.latitude;
        slon = latLng.longitude;

        MapsActivity.this.drawPath();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("test", "on request");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSION_GROUP_LOCATION  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            switch(lastOperation) {
                case "LOAD_MAP" : {
                    Log.d("test", "on request load map");
                    beginMap();
                    currentPermission = "truee";
                    sharedPreferences = getSharedPreferences("maps",Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putString("currentPermission", currentPermission);
                    edit.commit();
                    break;
                }
                default : break;
            }
        }
    }

    public void geoCode() {
        Geocoder geocoder = new Geocoder(this);
        try {
            listAddress = geocoder.getFromLocationName(searchForm.getText().toString(), 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (listAddress.size() > 0) {
            Address address = listAddress.get(0);
            MarkerOptions marker = new MarkerOptions();
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            marker.position(latLng).title(address.getAddressLine(0));
            gMap.addMarker(marker);
            moveCamera(latLng, 17f);
        }
    }

    public void loadMap() {
        lastOperation = "LOAD_MAP";
        runTimePermission(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_GROUP_LOCATION);
    }

    public void getMyLocation(View v) {
        deviceLocation();
    }

    public void drawPath() {
        new getPathData().execute(getUrl());
    }

    public String getUrl() {
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + slat + "," + slon + "&destination=" + elat + "," + elon + "&key=AIzaSyCEHm10hetF0-oslpGZhzMJMkYdb6bg468";
        return url;
    }

    private class getPathData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MapsActivity.this);
            progressDialog.setTitle("Sedang Diproses...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            String data = downloadData(urls[0]);

            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            new ParserTask().execute(s);
            progressDialog.dismiss();
        }
    }

    public String downloadData(String urll) {
        InputStream in = null;
        HttpURLConnection connection = null;
        StringBuilder sb = new StringBuilder();

         try {
            URL url = new URL(urll);
            connection = (HttpURLConnection) url.openConnection();
            in = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            Log.d("data", "" + sb.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            connection.disconnect();
        }

        return sb.toString();
    }

    private class ParserTask extends AsyncTask<String, Void, List<List<HashMap<String,String>>>> {
        JSONObject json;
        DirectionsJSONParser directionsJSONParser;
        List<List<HashMap<String, String>>> lists;
        List<LatLng> points = new ArrayList<>();
        PolylineOptions line = new PolylineOptions();
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            try {
                json = new JSONObject(strings[0]);
                directionsJSONParser = new DirectionsJSONParser();
                lists = directionsJSONParser.parse(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return lists;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            super.onPostExecute(lists);
            for (int i = 0; i < lists.size(); i++) {

                List<HashMap<String, String>> lists2 = lists.get(i);

                for (int j = 0; j < lists2.size(); j++) {

                    HashMap<String, String> map = lists2.get(j);
                    LatLng latLng = new LatLng(Double.parseDouble(map.get("lat")), Double.parseDouble(map.get("lng")));
                    points.add(latLng);
                }
            }

            line.addAll(points).width(15.0f).color(Color.RED);
            gMap.addPolyline(line);
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(slat, slon), 13f));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.title("Tujuan").position(new LatLng(elat, elon));
            gMap.addMarker(markerOptions);

        }
    }
}
