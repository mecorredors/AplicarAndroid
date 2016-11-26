package car.gov.co.carserviciociudadano.Utils;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import car.gov.co.carserviciociudadano.AppCar;

/**
 * Created by Olger on 26/11/2016.
 */

public class Utils {
    public  static long convertLong(String value){
        if (value == null ) return  0;

        long res = 0;
        try{
            res = Long.parseLong(value);
        }catch (NumberFormatException e){

        }
        return res;
    }
    public  static long convertInt(String value){
        if (value == null ) return  0;

        long res = 0;
        try{
            res = Integer.parseInt(value);
        }catch (NumberFormatException e){

        }
        return res;
    }

    public  static String getFechaActual(){
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }

    public static   int dimenDpi(int dimen){
        float scale = AppCar.getContext().getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (dimen * scale + 0.5f);
        return  dpAsPixels;
    }

    public static boolean isValidFormat(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    public static String getAuthorizationParques(){
        String pass="";
        try {
            String autorization = Security.mAutorization;
            pass=Security.Decrypt("ApiSidCarKey", autorization);
        }catch(Exception ex){

        }
        return pass;
    }
}
