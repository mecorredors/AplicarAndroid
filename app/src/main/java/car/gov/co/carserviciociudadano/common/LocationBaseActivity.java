package car.gov.co.carserviciociudadano.common;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;
import car.gov.co.carserviciociudadano.R;

/**
 * Created by Olger on 21/09/2016.
 */
public class LocationBaseActivity extends BaseActivity {

    public final static int MY_PERMISSIONS_ACCESS_FINE_LOCATION = 2;
    private LocationManager locationManager;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    boolean isRunning = false;
    ProgressDialog mProgressDialog;
    private int REQUEST_CODE_SETTINGS_LOCATION = 101;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override protected void onDestroy() {
        super.onDestroy();
        cancel();
    }

    @Override protected void onPause(){
        super.onPause();
        cancel();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    //// location

    private void mostrarProgressDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Obteniendo mi ubicación...");
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                cancel();
            }
        });

    }

    protected void getPosition() {

        if (isRunning) {
            cancel();
            isRunning = false;
            if (mProgressDialog != null) mProgressDialog.dismiss();

        } else {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            try {
                gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ex) {
            }
            try {
                network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (Exception ex) {
            }

            if (Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_ACCESS_FINE_LOCATION);

            } else {
                //mapa.setMyLocationEnabled(true);
                if (gps_enabled == false) {

                    dialogUbicationSettings();

                } else {
                    mostrarProgressDialog();
                    if (gps_enabled) {
                        isRunning = true;
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListenerGps);
                        Log.d(" gps enabled", "gps ejecutadi");
                    }
                    if (network_enabled) {
                        isRunning = true;
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 1, locationListenerNetwork);
                        Log.d("network enable", "ejecutado");
                    }
                }
            }
        }

    }


    final LocationListener locationListenerGps = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.d("resultado gps", "" + location.getLatitude());
            Log.d("resultado gps altitude", "" + location.getAltitude());
            Log.d("resultado gps precision", "" + location.getAccuracy());
            if (mProgressDialog != null) mProgressDialog.dismiss();
            resultLocation(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            if (mProgressDialog != null) mProgressDialog.dismiss();
        }

        @Override
        public void onProviderEnabled(String s) {
            if (mProgressDialog != null) mProgressDialog.dismiss();
            Toast.makeText(getBaseContext(), "Gps is turned on!! ", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String s) {
            if (mProgressDialog != null) mProgressDialog.dismiss();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            getBaseContext().startActivity(intent);
            Toast.makeText(getBaseContext(), "Gps is turned off!! ", Toast.LENGTH_SHORT).show();
        }
    };

    final LocationListener locationListenerNetwork = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.d("resultado network", "" + location.getLatitude());
            Log.d("res network altitude", "" + location.getAltitude());
            Log.d("res network preci", "" + location.getAccuracy());
            if (mProgressDialog != null) mProgressDialog.dismiss();
            resultLocation(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            if (mProgressDialog != null) mProgressDialog.dismiss();
        }

        @Override
        public void onProviderEnabled(String s) {
            if (mProgressDialog != null) mProgressDialog.dismiss();
            Toast.makeText(getBaseContext(), "Gps is turned on!! ", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String s) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            getBaseContext().startActivity(intent);
            if (mProgressDialog != null) mProgressDialog.dismiss();
            Toast.makeText(getBaseContext(), "Gps is turned off!! ", Toast.LENGTH_SHORT).show();
        }
    };

    public void cancel() {
        if (locationManager != null) {
            if (Build.VERSION.SDK_INT >= 23 &&
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("permiso", "Permiso denegado");
                return;
            }
            locationManager.removeUpdates(locationListenerGps);
            locationManager.removeUpdates(locationListenerNetwork);

        }
        if (mProgressDialog != null) mProgressDialog.dismiss();
    }

    protected void resultLocation(Location location) {
        cancel();
      //  miPosicion = new LatLng(location.getLatitude(), location.getLongitude());

       // dibujarRuta();
        Log.d("latitude ", "" + location.getLatitude());

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case MY_PERMISSIONS_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getPosition();
                } else {
                    mostrarMensaje("Permiso denegado para obtener ubicación");
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("resultCode", "" + resultCode + "requestCode: " + requestCode);
        if (requestCode == REQUEST_CODE_SETTINGS_LOCATION) {
            getPosition();
        }
    }

    protected void dialogUbicationSettings() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.acceso_ubicacion));

        builder.setPositiveButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getString(R.string.configuracion), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                LocationBaseActivity.this.startActivityForResult(intent, REQUEST_CODE_SETTINGS_LOCATION);
                dialog.dismiss();
            }
        });

        builder.show();
    }


}
