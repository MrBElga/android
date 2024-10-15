package com.example.appmap;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private TextView lat, longitude, alt;
    private Button button;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private final PermissionsMarshmallow permissionsMashmallow = new PermissionsMarshmallow(this);
    private final String[] PERMISSIONS = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        lat = findViewById(R.id.lat);
        longitude = findViewById(R.id.longitude);
        alt = findViewById(R.id.alt);

        button = findViewById(R.id.button);
        CheckPermissionGranted();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(@NonNull Location location) {
                longitude.setText(""+location.getLongitude());
                alt.setText(""+location.getAltitude());
                lat.setText(""+location.getLatitude());


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                //LocationListener.super.onStatusChanged(provider, status, extras);
            }
        };
        button.setOnClickListener(e-> {
            if (button.getText().toString().equals("iniciar")) {
                iniciarMonitoramento();
            } else {
                pararMonit();
            }
        });
    }



     private void pararMonit() {
        button.setText("iniciar");
        locationManager.removeUpdates(locationListener);
    }




    private void iniciarMonitoramento(){
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0,
                locationListener
        );

        button.setText("pausar");
    }


    private void CheckPermissionGranted() {
        if (permissionsMashmallow.hasPermissions(PERMISSIONS)) {
              Manifest.permission granted;
        } else {
           permissionsMashmallow.requestPermissions(PERMISSIONS, 2);
       }
    }
}