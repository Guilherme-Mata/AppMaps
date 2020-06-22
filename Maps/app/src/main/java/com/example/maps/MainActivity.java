package com.example.maps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private static boolean LOCATION_PERMISSION_REQUEST_CODE = false;
    public GoogleMap map;
    public LatLng localizacao= new LatLng(-23.951137, -46.339025);
    private Button bt;
    private GeoDataClient geoDataClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;



    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(MainActivity.this);
        geoDataClient = Places.getGeoDataClient(MainActivity.this, null);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        metodoBotao();
    }

    @Override
    public void onMapReady (GoogleMap googleMap) {
        this.map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(localizacao,18);
        map.animateCamera(update);
        Circle circle = map.addCircle(new CircleOptions()
        .center(localizacao)
                .radius(100)
                .strokeColor(Color.RED)
                .fillColor(Color.TRANSPARENT));
        map.addMarker(new MarkerOptions().position(localizacao).title("SFC"));

    }
    private void metodoBotao(){
        bt = (Button)findViewById(R.id.btn);
        bt.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                enableMyLocation();
                atualizaSuaLocalizacao( );
            }
        });
    }
    private void enableMyLocation(){
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED));
        {
            LOCATION_PERMISSION_REQUEST_CODE = PermissionUtils.validate(this, 1, Manifest.permission.ACCESS_FINE_LOCATION);
            LOCATION_PERMISSION_REQUEST_CODE = PermissionUtils.validate(this, 1, Manifest.permission.ACCESS_COARSE_LOCATION);

        }
    }
    private void atualizaSuaLocalizacao (){
        try {
            LocationManager LocationManager = (android.location.LocationManager) getSystemService(Context.LOCATION_SERVICE);
            LocationListener LocationListener = new LocationListener() {
                @Override
                public void onLocationChanged (Location location) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    map.addMarker(new MarkerOptions().position(latLng).title("Diga oi !!!"));
                    CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 18);
                    map.animateCamera(update);
                    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }

                @Override
                public void onStatusChanged (String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled (String provider) {

                }

                @Override
                public void onProviderDisabled (String provider) {

                }
            };


            LocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, LocationListener);

        } catch (SecurityException ex ){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionFailed (@NonNull ConnectionResult connectionResult) {

    }

}
