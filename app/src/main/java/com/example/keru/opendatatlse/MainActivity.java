package com.example.keru.opendatatlse;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.octo.android.robospice.GsonGoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static final int DEFAULT_RADIUS = 1000;

    protected SpiceManager spiceManager;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private Double lat;
    private Double lon;
    private int radius;

    private SeekBar seekBarDistance;

    private Button buttonRequest;
    private FloatingActionButton fab;
    private TextView textViewCommune;
    private TextView textViewSeekBar;
    private TextView textViewClosestPoint;
    private MapView mapView;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonRequest = (Button) findViewById(R.id.buttonRequest);
        textViewCommune = (TextView) findViewById(R.id.textViewCommune);
            textViewCommune.setVisibility(View.INVISIBLE);
        textViewClosestPoint = (TextView) findViewById(R.id.textViewClosestPoint);
            textViewClosestPoint.setVisibility(View.INVISIBLE);
        textViewSeekBar = (TextView) findViewById(R.id.textViewSeekBar);
        mapView = (MapView) findViewById(R.id.mapView);
        seekBarDistance = (SeekBar) findViewById(R.id.seekBar);
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        textViewSeekBar.setText(String.valueOf(DEFAULT_RADIUS) + " M");
        seekBarDistance.setProgress(DEFAULT_RADIUS);
        radius = DEFAULT_RADIUS;

        spiceManager = new SpiceManager(GsonGoogleHttpClientSpiceService.class);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                lat = location.getLatitude();
                lon = location.getLongitude();
                final LatLng newLocation = new LatLng(lat, lon);
                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        map = googleMap;
                        map.clear();
                        map.addMarker(new MarkerOptions().position(newLocation).title("vous êtes ici"));
                        map.moveCamera(CameraUpdateFactory.newLatLng(newLocation));
                        mapView.onResume();
                    }
                });

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;

                LatLng userPosition = new LatLng(lat, lon);
                map.addMarker(new MarkerOptions().position(userPosition).title("Vous êtes ici").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                map.moveCamera(CameraUpdateFactory.newLatLng(userPosition));
                CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
                map.animateCamera(zoom);
                mapView.onResume();
            }
        });


        buttonRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performRequest(lat, lon, seekBarDistance.getProgress());
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        LatLng userPosition = new LatLng(lat, lon);
                        map = googleMap;
                        map.moveCamera(CameraUpdateFactory.newLatLng(userPosition));
                    }
                });
            }
        });

        seekBarDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewSeekBar.setText(progress + " M");
                radius = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();

        spiceManager.start(this);

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, 0);
        } else {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            lat = lastLocation.getLatitude();
            lon = lastLocation.getLongitude();

        }
    }

    @Override
    protected void onStop(){
        spiceManager.shouldStop();
        super.onStop();

        locationManager.removeUpdates(locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {

        switch (requestCode) {
            case 0: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        lat = lastLocation.getLatitude();
                        lon = lastLocation.getLongitude();


                    }


                } else {

                    final AlertDialog needGPSDialog = new AlertDialog.Builder(MainActivity.this)
                            .setMessage("Veuillez activer la géolocalisation")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            }
        }
    }

    private void performRequest(Double lat, Double lon, int radius){

        RecupRequestModel request = new RecupRequestModel(lat, lon, radius);

        spiceManager.execute(request, new RecupRequestListener());
    }

    private class RecupRequestListener implements RequestListener<RecupVerreModel> {


        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Log.d(TAG, "Request Failed");
        }

        @Override
        public void onRequestSuccess(RecupVerreModel recupVerreModel) {

            Log.d(TAG, "Request Succeded");
            final List<Record> record = recupVerreModel.getRecords();

            if (record.size() > 0) {
                String commune = record.get(0).getFields().getCommune();
                String adresse = record.get(0).getFields().getAdresse();
                textViewCommune.setText(commune + " " + adresse);
                textViewCommune.setVisibility(View.VISIBLE);
                textViewClosestPoint.setVisibility(View.VISIBLE);

                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {

                        map = googleMap;
                        LatLng userPosition = new LatLng(lat, lon);
                        map.clear();

                        for (int i = 0; i < record.size(); i ++){

                            List coords = record.get(i).getGeometry().getCoordinates();
                            Double collectLon = (Double) coords.get(0);
                            Double collectLat = (Double) coords.get(1);
                            String adresse = record.get(i).getFields().getAdresse();
                            LatLng coord = new LatLng(collectLat, collectLon);

                            map.addMarker(new MarkerOptions().position(coord).title(adresse));
                        }
                        map.addMarker(new MarkerOptions().position(userPosition).title("vous êtes ici").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        map.moveCamera(CameraUpdateFactory.newLatLng(userPosition));
                        CameraUpdate zoom= CameraUpdateFactory.zoomTo(15);
                        map.animateCamera(zoom);
                        mapView.onResume();
                    }
                });
            } else {
                Toast.makeText(MainActivity.this, "Aucun dépôt disponible", Toast.LENGTH_SHORT).show();

                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        map = googleMap;

                        LatLng userPosition = new LatLng(lat, lon);
                        map.clear();
                        map.addMarker(new MarkerOptions().position(userPosition).title("Vous êtes ici").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        map.moveCamera(CameraUpdateFactory.newLatLng(userPosition));
                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
                        map.animateCamera(zoom);
                        mapView.onResume();
                    }
                });
            }
        }
    }
}