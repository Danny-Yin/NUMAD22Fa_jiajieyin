package edu.northeastern.NUMAD22Fa_jiajieyin;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.wifi.aware.PublishConfig;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class GpsMain extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button getCurrentLocation, resetDistanceTravel;

    TextView location_latitude, location_longitude, location_Changed;

    double previousLat, previousLong;
    Double latitude = null;
    Double longitude = null;

    double totalTravelDistance = 0.0;

    Spinner precisionDropdown;

//    boolean ResetOn = false;

//    int latitudeView , longitudeView;

    FusedLocationProviderClient fusedLocationProviderClient;

    LocationRequest locationRequest;

    LocationCallback locationCallback;

    boolean justStartUp = false;

    String[] fineLocation = {Manifest.permission.ACCESS_FINE_LOCATION};
    String[] coarseLocation = {Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_page);
        updateValuesFromBundle(savedInstanceState);

        location_latitude = findViewById(R.id.latit);
        location_longitude = findViewById(R.id.longit);
        location_Changed = findViewById(R.id.locationChange);
        getCurrentLocation = findViewById(R.id.get_locations);
        resetDistanceTravel = findViewById((R.id.reset_distance));
        precisionDropdown = findViewById(R.id.precision_down);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.planet_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        precisionDropdown.setAdapter(adapter);

        precisionDropdown.setOnItemSelectedListener(this);


        location_Changed.setText((int)totalTravelDistance + "meter");

        resetDistanceTravel.setOnClickListener(view -> resetTotalDistanceTraveled());

        getCurrentLocation.setOnClickListener(view ->{
            if (ContextCompat.checkSelfPermission(
                    getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(
                    getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(GpsMain.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                ActivityCompat.requestPermissions(GpsMain.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);

            } else {
                getCurrentLocation();
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        locationRequest = LocationRequest.create()
                .setInterval(500)
                .setFastestInterval(500)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }

            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);


                if (locationResult.getLocations().size()>0){
                    int lastLocationIndex = locationResult.getLocations().size()-1;

                    if (latitude != null && longitude != null){
                        previousLat = latitude;
                        previousLong = longitude;
                        justStartUp = false;
                    }
                    else{
                        justStartUp = true;
                    }

                    latitude = locationResult.getLocations().get(lastLocationIndex).getLatitude();
                    longitude = locationResult.getLocations().get(lastLocationIndex).getLongitude();

                    if (justStartUp){
                        location_latitude.setText("N    "+ latitude);
                        location_longitude.setText("E     " + longitude);
                        double distance = distanceCalculator(latitude,longitude,latitude,longitude);
                        totalTravelDistance += distance;
                        location_Changed.setText((int)totalTravelDistance + " meter");
                    }
                    else{
                        location_latitude.setText("N    "+ latitude);
                        location_longitude.setText("E     " + longitude);
                        double distance = distanceCalculator(latitude,longitude,previousLat,previousLong);
                        totalTravelDistance += distance;
                        location_Changed.setText((int)totalTravelDistance + " meter");
                    }
                }
            }
        };

    }

    @Override
    protected void onResume() {

        super.onResume();

        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            fusedLocationProviderClient.requestLocationUpdates(locationRequest,
                    locationCallback, Looper.getMainLooper());

        }
    }


    private double distanceCalculator(double newLat, double newLong, double oldLat, double oldLong) {
        Double result;

        double differenceLat = (newLat - oldLat) * Math.PI / 180;
        double differenceLong = (newLong - oldLong) * Math.PI / 180;

        oldLat = oldLat * Math.PI / 180;
        newLat = newLat * Math.PI / 180;

        double firstPart = (Math.sin(differenceLat / 2) * Math.sin(differenceLat / 2))
                + (Math.sin(differenceLong / 2) * Math.sin(differenceLong / 2) * Math.cos(oldLat)
                * Math.cos(newLat));

        double secondPart = 2 * Math.atan2(Math.sqrt(firstPart), Math.sqrt(1 - firstPart));
        result = 6371000 * secondPart;

        return result;
    }


    boolean checkCurrentPermissions(String[] permissions) {
        int numberOfPermissions = permissions.length;
        int numberOfPermissionsGranted = 0;

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) == PermissionChecker.PERMISSION_GRANTED) {
                numberOfPermissionsGranted++;
            }
        }

        return numberOfPermissions == numberOfPermissionsGranted;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();

        // if the user wants approximate location
        if (item.equals("Approximate Location")) {
            if (checkCurrentPermissions(coarseLocation)) {
                ActivityCompat.requestPermissions(this, coarseLocation, 2);
            } else {
                // setting the priority
                locationRequest.setPriority(Priority.PRIORITY_LOW_POWER);
                getCurrentLocation();

            }
        }
        // if the user wants accurate location
        else if (item.equals("Accurate Location")) {
            if (checkCurrentPermissions(fineLocation)) {
                ActivityCompat.requestPermissions(this, fineLocation, 1);
            } else {
                locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
                getCurrentLocation();

            }

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void getCurrentLocation() {

        LocationSettingsRequest.Builder lsb = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        SettingsClient client = LocationServices.getSettingsClient(getApplicationContext());
        Task<LocationSettingsResponse> task = client.checkLocationSettings(lsb.build());

        task.addOnSuccessListener(locationSettingsResponse -> {

        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);

    }

    private void resetTotalDistanceTraveled() {
        totalTravelDistance = 0.0;
        location_Changed.setText(totalTravelDistance + "meter");
    }


    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }
        if (savedInstanceState.keySet().contains("DISTANCE")) {
            totalTravelDistance = savedInstanceState.getInt("DISTANCE");
        }
    }

    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("DISTANCE", (int) totalTravelDistance);
        super.onSaveInstanceState(outState);

    }


//    private void updateGPS(){
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(GpsMain.this);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(location -> updateUIValue(location));
//        }
//        else{
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
//            }
//        }
//    }
//
//    private void resetTotalDistanceTraveled() {
//        totalTravelDistance = 0;
//        location_Changed.setText("Total Distance Traveled: " + totalTravelDistance + "m");
//    }

//    private void updateUIValue(Location location) {
//        location_latitude.setText(String.valueOf(location.getLatitude()));
//        location_longitude.setText(String.valueOf(location.getLongitude()));
//    }



}
