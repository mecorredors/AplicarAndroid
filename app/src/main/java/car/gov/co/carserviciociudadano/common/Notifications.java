package car.gov.co.carserviciociudadano.common;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.bicicar.activities.RegistrarActividadActivity;
import car.gov.co.carserviciociudadano.bicicar.services.LocationMonitoringService;

/**
 * Created by apple on 5/11/18.
 */

public  class Notifications {

    public static void showNotification(String mensaje){
        // Create an explicit intent for an Activity in your app

        createNotificationChannel();

        Intent intent = new Intent(AppCar.getContext(), RegistrarActividadActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity( AppCar.getContext(), 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder( AppCar.getContext(), LocationMonitoringService.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_directions_bike_black_24dp)
                .setContentTitle("BiciCAR")
                .setContentText(mensaje)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(false);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(AppCar.getContext());
        notificationManager.notify(LocationMonitoringService.NOTIFICATION_ID, mBuilder.build());
    }

    public static  void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = AppCar.getContext().getString(R.string.channel);
            String description =  AppCar.getContext().getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(LocationMonitoringService.CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager =  AppCar.getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}