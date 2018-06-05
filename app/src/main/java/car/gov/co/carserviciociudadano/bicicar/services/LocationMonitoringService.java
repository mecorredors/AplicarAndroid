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

import java.math.BigDecimal;
import java.util.Calendar;

import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.PreferencesApp;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.bicicar.activities.RegistrarActividadActivity;


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
    public static final String EXTRA_MINUTOS = "extra_minutos";
    public static final String EXTRA_SEGUNDOS = "extra_segundos";
    public static final String EXTRA_DISTANCIA = "extra_distancia";
    public static final String EXTRA_TIEMPO_MINUTOS = "extra_tiempo_minutos";
    public static final String EXTRA_START_TIME = "extra_start_time";

    float distancia = 0;

    Location locationPreview;
    long startTime = 0;
    private Handler _handler;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        createNotificationChannel();

        startTime = PreferencesApp.getDefault(PreferencesApp.READ).getLong(EXTRA_START_TIME , System.currentTimeMillis() );
        distancia = PreferencesApp.getDefault(PreferencesApp.READ).getFloat(EXTRA_DISTANCIA , 0);

        mLocationClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest.setInterval(15000);
        mLocationRequest.setFastestInterval(15000);


        int priority = LocationRequest.PRIORITY_HIGH_ACCURACY; //by default
        //PRIORITY_BALANCED_POWER_ACCURACY, PRIORITY_LOW_POWER, PRIORITY_NO_POWER are the other priority modes


        mLocationRequest.setPriority(priority);
        mLocationClient.connect();

        //Make it stick to the notification panel so it is less prone to get cancelled by the Operating System.



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
        System.out.println("Tick " + System.currentTimeMillis());

        long millis = System.currentTimeMillis() - startTime;
        int seconds = (int) (millis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        float tiempo_minutos = Utils.round (2, millis / (float)60000.0);
      //  textView.setText(String.format("%d:%02d", minutes, seconds));

     //   time =  ( System.currentTimeMillis() - startTime) / 1000.0;
        sendMessageToUI(distancia, tiempo_minutos, minutes, seconds);
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
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            Log.d(TAG, "== Error On onConnected() Permission not granted");
            //Permission not granted by user so cancel the further execution.

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

        if (locationPreview != null){
            float recorrido = locationPreview.distanceTo(location);
            Log.d(TAG, "distancia " +recorrido);
            if (recorrido > 12){
                distancia += recorrido;
                locationPreview = location;
                PreferencesApp.getDefault(PreferencesApp.WRITE).putFloat(EXTRA_DISTANCIA , distancia).commit();
            }

        }

        if (locationPreview == null)
            locationPreview = location;

    }



    private void sendMessageToUI( float distancia, float tiempoMinutos, int minutos, int segundos) {

        Log.d(TAG, "Sending info..." + distancia);

        Intent intent = new Intent(ACTION_LOCATION_BROADCAST);
        intent.putExtra(EXTRA_MINUTOS, minutos);
        intent.putExtra(EXTRA_SEGUNDOS, segundos);
        intent.putExtra(EXTRA_DISTANCIA, distancia);
        intent.putExtra(EXTRA_TIEMPO_MINUTOS, tiempoMinutos);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

        showNotification("Distancia: " + Utils.round(2,(distancia/1000)) + " Kms: Duración "+ minutos + ":" + segundos );

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

    private void showNotification(String mensaje){
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, RegistrarActividadActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_directions_bike_black_24dp)
                .setContentTitle("BiciCAR")
                .setContentText(mensaje)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(false);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
