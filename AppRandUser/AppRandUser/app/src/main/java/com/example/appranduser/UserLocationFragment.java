package com.example.appranduser;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class UserLocationFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapView mapView;
    private TextView distanceTextView;
    private String latitude;
    private String longitude;
    private FusedLocationProviderClient locationClient;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_location, container, false);

        distanceTextView = view.findViewById(R.id.distance_text_view);
        mapView = view.findViewById(R.id.map_view);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        if (getArguments() != null) {
            latitude = getArguments().getString("latitude");
            longitude = getArguments().getString("longitude");
        }

        locationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        return view;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (latitude != null && longitude != null) {
            LatLng userLocation = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
            mMap.clear(); // Limpa o mapa antes de adicionar o marcador
            mMap.addMarker(new MarkerOptions().position(userLocation).title("User Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 10));

            // Obtém a localização atual e calcula a distância
            locationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    LatLng deviceLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    double distance = calculateDistance(deviceLocation, userLocation);
                    distanceTextView.setText("Distance: " + String.format("%.2f", distance) + " km");
                } else {
                    distanceTextView.setText("Distance: Not available");
                }
            });
        } else {
            distanceTextView.setText("Coordinates not available");
        }
    }

    private double calculateDistance(LatLng start, LatLng end) {
        double radius = 6371; // Raio da Terra em km
        double latDiff = Math.toRadians(end.latitude - start.latitude);
        double lngDiff = Math.toRadians(end.longitude - start.longitude);

        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2)
                + Math.cos(Math.toRadians(start.latitude)) * Math.cos(Math.toRadians(end.latitude))
                * Math.sin(lngDiff / 2) * Math.sin(lngDiff / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return radius * c;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState); // Salva o estado do mapa
    }
}
