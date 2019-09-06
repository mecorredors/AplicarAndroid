package car.gov.co.carserviciociudadano.bicicar.services;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class LocationRetriever
        extends LocationCallback {

    private Context context;
    private FusedLocationProviderClient mLocationClient;
    private LocationListener mLocationListener;
    private LocationRequest mLocationRequest;
    final static int INTERVAL = 1000;
    final static int FASTEST_INTERVAL = 700;
    final static int SMALLEST_DISPLACEMENT = 2;
    private static final String LOG_TAG = "LocationRetriever";

    /**
     * LocationRetriever
     */
    public LocationRetriever(
            Context context,
            LocationListener locationListener) {
        this.context = context;
        mLocationListener = locationListener;
    }

    /**
     * getLocationAvailability
     */
    private void getLocationAvailability() {
        try {
            mLocationClient.getLocationAvailability().addOnCompleteListener(
                    new OnCompleteListener<LocationAvailability>() {
                        @Override
                        public void onComplete(@NonNull Task<LocationAvailability> task) {
                            LocationRetriever.this.onLocationAvailability(task.getResult());
                        }
                    });
        }
        catch (IllegalStateException ex) {
            Log.w(LOG_TAG, ex.toString());
        }
        catch (SecurityException ex) {
            Log.w(LOG_TAG, ex.toString());
        }
    }

    @Override
    public void onLocationAvailability(LocationAvailability availability) {
        if (availability.isLocationAvailable()) {
            //requestLocationUpdates();
        }
    }

    @Override
    public void onLocationResult(LocationResult result) {
        if (mLocationListener != null) {
            Location location = result.getLastLocation();
            mLocationListener.onLocationChanged(location);
        }
    }

    /**
     * requestLocationUpdate
     */
    private void requestLocationUpdates()
            throws SecurityException {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setSmallestDisplacement(SMALLEST_DISPLACEMENT); // habilitar para presicion

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this.context);
        settingsClient.checkLocationSettings(locationSettingsRequest);
        mLocationClient.requestLocationUpdates(mLocationRequest, this, Looper.myLooper());

    }

    /**
     * start
     */
    public void start() {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("erro permiso", "== Error On onConnected() Permission not granted");
            return;
        }
        mLocationClient = LocationServices.getFusedLocationProviderClient(context);
        //getLocationAvailability();
        requestLocationUpdates();
    }

    /**
     * stop
     */
    public void stop() {
        if (mLocationClient != null) {
            mLocationClient.removeLocationUpdates(this);
        }
    }
}
