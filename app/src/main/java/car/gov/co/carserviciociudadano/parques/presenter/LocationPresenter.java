package car.gov.co.carserviciociudadano.parques.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Olger on 01/05/2017.
 */

public class LocationPresenter {

    Context context;
    IViewLocation iViewLocation;
    private LocationManager locationManager;;
    public LocationPresenter(IViewLocation iViewLocation,Context context, LocationManager locationManager){
        this.iViewLocation = iViewLocation;
        this.context = context;
        this.locationManager = locationManager;
    }

    public void cancel() {
        if (locationManager != null) {
            if (Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("permiso", "Permiso denegado");
                return;
            }
            locationManager.removeUpdates(locationListenerGps);
            locationManager.removeUpdates(locationListenerNetwork);

        }
       // if (mProgressDialog != null) mProgressDialog.dismiss();
    }
    private void resultLocation(Location location) {
        cancel();
       // miPosicion = new LatLng(location.getLatitude(), location.getLongitude());
        // mapa.addMarker(new MarkerOptions().position(miPosicion).title("Mi posición").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_my_location_blue_16dp)));
      //  dibujarRuta();
        Log.d("latitude ", "" + location.getLatitude());

    }

    final LocationListener locationListenerGps = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            resultLocation(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivity(intent);
            Toast.makeText(context, "Gps is turned off!! ", Toast.LENGTH_SHORT).show();
        }
    };

    final LocationListener locationListenerNetwork = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            resultLocation(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivity(intent);
        }
    };

}
