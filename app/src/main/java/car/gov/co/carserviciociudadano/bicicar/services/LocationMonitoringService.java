package car.gov.co.carserviciociudadano.bicicar.services;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
        import android.support.annotation.Nullable;
        import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
        import android.util.Log;

        import com.google.android.gms.common.ConnectionResult;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.location.LocationListener;
        import com.google.android.gms.location.LocationRequest;
        import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.PreferencesApp;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.bicicar.activities.RegistrarActividadActivity;
import car.gov.co.carserviciociudadano.bicicar.model.LogTrayecto;
import car.gov.co.carserviciociudadano.common.Notifications;


/**
 * Created by devdeeds.com on 27-09-2017.
 */

public class LocationMonitoringService extends Service implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String TAG = LocationMonitoringService.class.getSimpleName();
    GoogleApiClient mLocationClient;
    LocationRequest mLocationRequest = new LocationRequest();


    public static final String ACTION_LOCATION_BROADCAST = LocationMonitoringService.class.getName() + "LocationBroadcast";

    public static final String EXTRA_DISTANCIA = "extra_distancia";
    public static final String EXTRA_START_TIME = "extra_start_time";
    public static final String EXTRA_RUTA = "extra_start_ruta";
    public static final String EXTRA_DISTANCIA_IN_PAUSE = "extra_distancia_pause";
    public static final String EXTRA_IN_PAUSE = "extra_in_pause";
    public static final String EXTRA_TIEMPO_MILLIS_IN_PAUSE = "extra_tiempo_millis_in_pause";
    public static final String EXTRA_TIEMPO_MILLIS = "extra_tiempo_millis";


    float distancia = 0;
    float distancia_en_pausa = 0;
    float precision = 0;

    Location locationPreview;
    Location locationBest;

    long startTime = 0;
    private Handler _handler;
    List<LatLng> latLngs = new ArrayList<>();


    int numRequest = 0;

    final static int INTERVAL = 1400;
    final static int FASTEST_INTERVAL = 1200;
    final static int SMALLEST_DISPLACEMENT = 2;
    final static int MIN_DISTANCE = 2;

    long tiempo_millins_en_pausa = 0;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startTime = PreferencesApp.getDefault(PreferencesApp.READ).getLong(EXTRA_START_TIME , System.currentTimeMillis() );
        distancia = PreferencesApp.getDefault(PreferencesApp.READ).getFloat(EXTRA_DISTANCIA , 0);
        tiempo_millins_en_pausa = PreferencesApp.getDefault(PreferencesApp.READ).getLong(EXTRA_TIEMPO_MILLIS_IN_PAUSE , (long) 0);
        distancia_en_pausa = PreferencesApp.getDefault(PreferencesApp.READ).getFloat(EXTRA_DISTANCIA_IN_PAUSE ,  0);

        String ruta = PreferencesApp.getDefault(PreferencesApp.READ).getString(EXTRA_RUTA);
        latLngs.clear();
        if (ruta != null && ! ruta.isEmpty()){
            latLngs.addAll(PolyUtil.decode(ruta));
        }

        mLocationClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setSmallestDisplacement(SMALLEST_DISPLACEMENT);

        numRequest = 0;

        int priority = LocationRequest.PRIORITY_HIGH_ACCURACY; //by default
        //PRIORITY_BALANCED_POWER_ACCURACY, PRIORITY_LOW_POWER, PRIORITY_NO_POWER are the other priority modes


        mLocationRequest.setPriority(priority);
        mLocationClient.connect();


        _handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {
                if (_handler == _h0) {
                    tick();
                    _handler.postDelayed(this, 1000);
                }
            }

            private final Handler _h0 = _handler;
        };
        r.run();


        return START_STICKY;
    }


    private void tick() {

        long tiempo_millis = System.currentTimeMillis() - startTime;
        tiempo_millis = tiempo_millis + tiempo_millins_en_pausa;
        float total_distancia = distancia + distancia_en_pausa;
       /* int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        float tiempo_minutos = Utils.round (2, millis / (float)60000.0);*/
        sendMessageToUI(total_distancia , tiempo_millis);
    }

    @Override
    public void onDestroy(){
        disableLocationUpdates();
        locationPreview = null;
        _handler = null;
        super.onDestroy();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
     * LOCATION CALLBACKS
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "== Error On onConnected() Permission not granted");
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, mLocationRequest, this);

        Log.d(TAG, "Connected to Google API");
    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection suspended");
    }

    //to get the location change
    @Override
    public void onLocationChanged(Location location) {

        numRequest++;
        boolean calcular_distancia = false;
        precision = location.getAccuracy();

        if (locationPreview == null) {
            locationPreview = location;
        }

        if (locationBest == null) {
            locationBest = location;
        }

        if (precision < 7) {
            calcular_distancia = true;
            Log.d("calculo", "precicion < 7");
        }else if (precision  >= 7 && precision < 10 ) {
            if (isBetterLocation(locationBest, location)) {
                locationBest = location;
            }
            if (numRequest > 2) {
                calcular_distancia = true;
            }
            Log.d("calculo", "precicion > 10");
        }else if (precision  >= 10 && precision < 16 ) {
            if (isBetterLocation(locationBest, location)) {
                locationBest = location;
                calcular_distancia = true;
            }
            if (numRequest > 4) {
                calcular_distancia = true;
            }
            Log.d("calculo", "precicion > 10");
        }
        else if (precision  >= 16 && precision < 20 ) {
            if (isBetterLocation(locationBest, location)) {
                locationBest = location;
                calcular_distancia = true;
            }
            if (numRequest > 8) {
                calcular_distancia = true;
            }
            Log.d("calculo", "precicion > 16");
        }else if (precision  >= 20 ) {
            if (isBetterLocation(locationBest, location)) {
                locationBest = location;
                calcular_distancia = true;
            }
            if (numRequest > 16) {
                calcular_distancia = true;
            }
            Log.d("calculo", "precicion > 20");
        }

        if (locationPreview != null && locationBest != null && calcular_distancia){
            float recorrido = locationPreview.distanceTo(locationBest);
            Log.d(TAG, "distancia " +recorrido);
            if (recorrido > MIN_DISTANCE){
                distancia += recorrido;
                locationPreview = locationBest;
                if (latLngs.size() == 0)
                    addPuntoRuta(locationPreview);

                addPuntoRuta(locationBest);
                PreferencesApp.getDefault(PreferencesApp.WRITE).putFloat(EXTRA_DISTANCIA , distancia).commit();
            }

            locationBest = null;
            numRequest = 0;
            Log.d("calculo", "calculado ------");
        }
    }

    private void addPuntoRuta(Location location){
        latLngs.add(new LatLng(location.getLatitude(), location.getLongitude()));
        String ruta = PolyUtil.encode(latLngs);
        PreferencesApp.getDefault(PreferencesApp.WRITE).putString(EXTRA_RUTA , ruta).commit();
    }


    private void sendMessageToUI( float distancia, long tiempoMillis) {

        Log.d(TAG, "Sending info..." + distancia);


        int segundos = (int) (tiempoMillis / 1000);
        int minutos = segundos / 60;
        segundos = segundos % 60;
        //float tiempoMinutos = Utils.round (2, tiempoMillis / (float)60000.0);

        Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
        intent.putExtra(EXTRA_DISTANCIA, distancia);
        intent.putExtra(EXTRA_TIEMPO_MILLIS, tiempoMillis);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        Notifications.showNotification(" Distancia: " + Utils.round(2,(distancia/1000)) + " Kms: Duración "+ minutos + ":" + segundos + "  Pre: " + Utils.round(2, precision));

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Failed to connect to Google API");

    }

    protected void disableLocationUpdates() {
        if (mLocationClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mLocationClient, this);
        }
    }

    public static final String CHANNEL_ID = "canal_bicicar";
    public static final int NOTIFICATION_ID = 100;


    static final int TIME_DIFFERENCE_THRESHOLD = 1 * 60 * 1000;
    boolean isBetterLocation(Location oldLocation, Location newLocation) {
        // If there is no old location, of course the new location is better.
        if(oldLocation == null) {
            return true;
        }

        // Check if new location is newer in time.
        boolean isNewer = newLocation.getTime() > oldLocation.getTime();

        // Check if new location more accurate. Accuracy is radius in meters, so less is better.
        boolean isMoreAccurate = newLocation.getAccuracy() < oldLocation.getAccuracy();
        if(isMoreAccurate && isNewer) {
            // More accurate and newer is always better.
            return true;
        } else if(isMoreAccurate && !isNewer) {
            // More accurate but not newer can lead to bad fix because of user movement.
            // Let us set a threshold for the maximum tolerance of time difference.
            long timeDifference = newLocation.getTime() - oldLocation.getTime();

            // If time difference is not greater then allowed threshold we accept it.
            if(timeDifference > -TIME_DIFFERENCE_THRESHOLD) {
                return true;
            }
        }

        return false;
    }
}
