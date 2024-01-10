package com.example.forpraktikan;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.Manifest;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int REQUEST_CODE_MAPS = 123;
    private GoogleMap mMap;
    private Marker currentLocationMarker;
    private boolean isFirstLocationUpdate = true;

    private TextView lat, lon, lokasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        lat = findViewById(R.id.editTextLatitude);
        lon = findViewById(R.id.editTextLongitude);
        lokasi = findViewById(R.id.editTextLocation);

        Button buttonMap = findViewById(R.id.buttonmap);
        buttonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Pastikan currentLocationMarker telah diinisialisasi
                if (currentLocationMarker != null && currentLocationMarker.getPosition() != null) {
                    // Set data lokasi yang akan dikirim kembali ke NewPostActivity
                    Intent resultIntent = new Intent();
                    LatLng currentLatLng = currentLocationMarker.getPosition();
                    resultIntent.putExtra("latitude", currentLatLng.latitude);
                    resultIntent.putExtra("longitude", currentLatLng.longitude);
                    resultIntent.putExtra("locationName", getAddressFromLocation(currentLatLng));

                    // Atur hasil dan kembali ke NewPostActivity
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(MapsActivity.this, "Lokasi belum ditemukan. Pastikan GPS aktif dan coba lagi.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                if (isFirstLocationUpdate) {
                    // Move the camera to the current location on the first location update
                    moveCameraToLocation(location);
                    isFirstLocationUpdate = false;

                    // Kirim data kembali ke aktivitas sebelumnya (NewPostActivity)
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("latitude", location.getLatitude());
                    resultIntent.putExtra("longitude", location.getLongitude());
                    resultIntent.putExtra("locationName", getAddressFromLocation(location));

                    setResult(RESULT_OK, resultIntent);
                }

                updateMarker(location);
            }

            private boolean getAddressFromLocation(Location location) {
                return false;
            }
            // Other LocationListener methods...

        });
    }

    private String getAddressFromLocation(LatLng location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addressList;

        try {
            addressList = geocoder.getFromLocation(location.latitude, location.longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder addressStringBuilder = new StringBuilder();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressStringBuilder.append(address.getAddressLine(i)).append("\n");
                }
                return addressStringBuilder.toString();
            } else {
                return "Alamat tidak ditemukan";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Error mengambil alamat";
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Enable location layer on map
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    private void moveCameraToLocation(Location location) {
        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f));
    }

    private void updateMarker(Location location) {
        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        // Remove the previous marker
        if (currentLocationMarker != null) {
            currentLocationMarker.remove();
        }

        // Add a new marker at the current location
        currentLocationMarker = mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Lokasi Sekarang"));
        lat.setText(String.valueOf(location.getLatitude()));
        lon.setText(String.valueOf(location.getLongitude()));

        // Update the address information
        updateAddressInfo(location);
    }

    private void updateAddressInfo(Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addressList;

        try {
            addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder addressStringBuilder = new StringBuilder();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressStringBuilder.append(address.getAddressLine(i)).append("\n");
                }
                lokasi.setText(addressStringBuilder.toString());
            } else {
                lokasi.setText("Alamat tidak ditemukan");
            }
        } catch (IOException e) {
            e.printStackTrace();
            lokasi.setText("Error mengambil alamat");
        }
    }
}