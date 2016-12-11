package car.gov.co.carserviciociudadano.Utils;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import car.gov.co.carserviciociudadano.AppCar;

/**
 * Created by Olger on 26/11/2016.
 */

public class Utils {

   //public  static final String FORMATO_FECHA = "yyyy-MM-dd";
   public  static final String FORMATO_FECHA = "dd-MM-yyyy";
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

    public static boolean isEqualsDate(Date fecha1, Date fecha2){
       if (fecha1 != null && fecha2 != null) {
           Calendar calendar1 = Calendar.getInstance();
           calendar1.setTime(fecha1);
           Calendar calendar2 = Calendar.getInstance();
           calendar2.setTime(fecha2);
           return (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) && calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH));
       }
        return false;
    }

    public static Date convertToDate(String dateString){
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMATO_FECHA);
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            Log.e("Utils.ConvertToDate",e.toString());
        }
        return convertedDate;
    }

    public static Calendar convertToCalendar(String dateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMATO_FECHA);
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(dateString);

            Calendar calendar =  new GregorianCalendar(); //Calendar.getInstance();
            calendar.setTime(convertedDate);
            return calendar;
        } catch (ParseException e) {
            Log.e("Utils.ConvertToDate",e.toString());
        }
       return  null;
    }

    public static Calendar convertToCalendar(Date date){
        if (date == null) return Calendar.getInstance();

         Calendar calendar =  Calendar.getInstance();
         calendar.setTime(date);
         return calendar;

    }

    public static String toStringFromDate(java.util.Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMATO_FECHA, Locale.getDefault());
        return dateFormat.format(date);
    }

    public static String toStringLargeFromDate(java.util.Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMATO_FECHA, Locale.getDefault());
        return dateFormat.format(date);
    }

    public static int difDaysDates(Calendar fechaInicio, Calendar fechaFin){
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(   fechaFin.getTime().getTime() - fechaInicio.getTime().getTime());
       return  c.get(Calendar.DAY_OF_YEAR);
    }


    public static  String formatoMoney(double valor){

        NumberFormat formatter = NumberFormat.getCurrencyInstance(getLocale());

        return  formatter.format(valor);
    }
    public static Locale getLocale(){
       return new Locale("es","CO");
    }

//    public static String formatoFecha(){
//        return "yyyy-MM-dd";
//    }
}
