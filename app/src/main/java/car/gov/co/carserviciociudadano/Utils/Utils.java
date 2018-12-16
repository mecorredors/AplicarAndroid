package car.gov.co.carserviciociudadano.Utils;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;

import com.anupcowkur.reservoir.Reservoir;
import com.stacktips.view.utils.CalendarUtils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.parques.dataaccess.Parques;
import car.gov.co.carserviciociudadano.petcar.presenter.ContenedorPresenter;

/**
 * Created by Olger on 26/11/2016.
 */

public class Utils {

   //public  static final String FORMATO_FECHA = "yyyy-MM-dd";
   public  static final String  FORMATO_FECHA = "dd-MM-yyyy";
    public  static long convertLong(String value){
        if (value == null ) return  0;

        long res = 0;
        try{
            res = Long.parseLong(value);
        }catch (NumberFormatException e){

        }
        return res;
    }
    public  static int convertInt(String value){
        if (value == null ) return  0;
        if (value.trim().isEmpty()) return  0;

        int res = 0;
        try{
            res = Integer.parseInt(value);
        }catch (NumberFormatException e){

        }
        return res;
    }
    public  static float convertFloat(String value){
        if (value == null ) return  0;
        if (value.trim().isEmpty()) return  0;

        float res = 0;
        try{
            res = Float.parseFloat(value);
        }catch (NumberFormatException e){

        }
        return res;
    }
    public  static String getFechaActual(){
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }

    public  static String getFechaActualCorta(){
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
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
            pass=Security.Decrypt("ApiParquesKey", autorization);
        }catch(Exception ex){

        }
        return pass;
    }

    public static String getAuthorizationSIDCAR(){
        String pass="";
        try {
            String autorization = Security.mAutorizationSIDCAR;
            pass=Security.Decrypt("ApiSidCarKey", autorization);
        }catch(Exception ex){

        }
        return pass;
    }

    public static String getAuthorizationBICICAR(){
        String pass="";
        try {
            String autorization = Security.mAutorizationBICICAR;
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

           return isEqualsDate(calendar1,calendar2);

           //return (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) && calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) && calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH));
       }
        return false;
    }
    public static boolean isEqualsDate(Calendar fecha1, Calendar fecha2){
       if (fecha1 != null && fecha2 != null) {
           String fe1 = fecha1.get(Calendar.YEAR)+" "+ fecha1.get(Calendar.MONTH)+" "+fecha1.get(Calendar.DAY_OF_MONTH);
           String fe2 = fecha2.get(Calendar.YEAR)+" "+ fecha2.get(Calendar.MONTH)+" "+fecha2.get(Calendar.DAY_OF_MONTH);

           return fe1.equals(fe2);
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

    public static boolean isAfterDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return date.after(calendar.getTime());
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

        if(isEqualsDate(fechaInicio,fechaFin )) return 0;

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(   fechaFin.getTime().getTime() - fechaInicio.getTime().getTime());
       return  c.get(Calendar.DAY_OF_YEAR);
    }

    public static String formatoNumbero(Object valor){
        NumberFormat formatter = NumberFormat.getInstance(getLocale());
        return formatter.format(valor);
    }

    public static  String formatoMoney(double valor){

        NumberFormat formatter = NumberFormat.getCurrencyInstance(getLocale());

        return  formatter.format(valor);
    }
    public static Locale getLocale(){
       return new Locale("es","CO");
    }


    public static boolean existeCache(String tag){
        try {
            return  Reservoir.contains(tag);
        }catch (Exception ex){
            return false;
        }
    }


    public static void putFechaCache(String tag){
        try {
            Calendar c = Calendar.getInstance();
            String fecha = String.valueOf(c.get(Calendar.YEAR)) + String.valueOf(c.get(Calendar.MONTH) + 1) + String.valueOf(c.get(Calendar.DAY_OF_MONTH));

            PreferencesApp.getDefault(PreferencesApp.WRITE).putInt("EXPIRED" + tag, Integer.parseInt(fecha)).commit();
        }catch (Exception ex){

        }
    }

    public static int getFechaCache(String tag){
        return PreferencesApp.getDefault(PreferencesApp.READ).getInt("EXPIRED" + tag,0);
    }

    public static int getFechaActualInt(){
        Calendar c = Calendar.getInstance();
        String fecha =  String.valueOf(c.get(Calendar.YEAR)) +  String.valueOf(c.get(Calendar.MONTH)+1) +  String.valueOf(c.get(Calendar.DAY_OF_MONTH)) ;

        return Utils.convertInt(fecha);
    }

    public static boolean cacheExpiro(int dias,String tag){

        int fechaCache = Utils.getFechaCache(tag);
        int fechaActual = Utils.getFechaActualInt();

        return ((fechaActual - fechaCache) > dias);

    }

    public static boolean isCurrentMonth(Date fecha1){
        if (fecha1 != null ) {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(fecha1);
            Calendar calendar2 = Calendar.getInstance();

            return CalendarUtils.isSameMonth (calendar1,calendar2);

        }
        return false;
    }
    public static long getTimeInMillis(){
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    public static String getHour(long millis){

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        try {
            String _24HourTime = calendar.get(Calendar.HOUR_OF_DAY) + ":"+ calendar.get(Calendar.MINUTE);
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh a");
            Date _24HourDt = _24HourSDF.parse(_24HourTime);
           // System.out.println(_24HourDt);
           return _12HourSDF.format(_24HourDt);
        } catch (Exception e) {
            e.printStackTrace();
            return calendar.get(Calendar.HOUR_OF_DAY) + ":"+ calendar.get(Calendar.MINUTE);
        }
    }

    public static String getDayOfWeek(Calendar c){
        String weekDay = "";
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        if (Calendar.MONDAY == dayOfWeek) {
            weekDay = "Lunes";
        } else if (Calendar.TUESDAY == dayOfWeek) {
            weekDay = "Martes";
        } else if (Calendar.WEDNESDAY == dayOfWeek) {
            weekDay = "Miercoles";
        } else if (Calendar.THURSDAY == dayOfWeek) {
            weekDay = "Jueves";
        } else if (Calendar.FRIDAY == dayOfWeek) {
            weekDay = "Viernes";
        } else if (Calendar.SATURDAY == dayOfWeek) {
            weekDay = "Sabado";
        } else if (Calendar.SUNDAY == dayOfWeek) {
            weekDay = "Domingo";
        }
        return  weekDay;
    }

    public static String toStringSQLLite(java.util.Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }

    public static Date convertToDateSQLLite(String dateString){
        //   SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date convertedDate = new Date();
        try {

            convertedDate = dateFormat.parse(dateString);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertedDate;
    }

    public static float round(int numDecimales, float d ) {
        if (d == 0) return  0;

        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(numDecimales, BigDecimal.ROUND_HALF_EVEN);
        return bd.floatValue();
    }

    public static List<Integer> listColores(){
        ArrayList<Integer> colores = new ArrayList<>();
        colores.add(ContextCompat.getColor(AppCar.getContext(), R.color.red500));
        colores.add(ContextCompat.getColor(AppCar.getContext(), R.color.green500));
        colores.add(ContextCompat.getColor(AppCar.getContext(), R.color.amber500));
        colores.add(ContextCompat.getColor(AppCar.getContext(), R.color.blue500));
        colores.add(ContextCompat.getColor(AppCar.getContext(), R.color.teal500));
        colores.add(ContextCompat.getColor(AppCar.getContext(), R.color.pink500));
        colores.add(ContextCompat.getColor(AppCar.getContext(), R.color.yellow500));
        colores.add(ContextCompat.getColor(AppCar.getContext(), R.color.cyan500));
        colores.add(ContextCompat.getColor(AppCar.getContext(), R.color.deepred500));
        colores.add(ContextCompat.getColor(AppCar.getContext(), R.color.indigo500));
        colores.add(ContextCompat.getColor(AppCar.getContext(), R.color.amber500));
        colores.add(ContextCompat.getColor(AppCar.getContext(), R.color.deepred500));
        colores.add(ContextCompat.getColor(AppCar.getContext(), R.color.bluelight500));
        colores.add(ContextCompat.getColor(AppCar.getContext(), R.color.red500));
        colores.add(ContextCompat.getColor(AppCar.getContext(), R.color.green500));
        colores.add(ContextCompat.getColor(AppCar.getContext(), R.color.pink500));
        colores.add(ContextCompat.getColor(AppCar.getContext(), R.color.blue500));
        colores.add(ContextCompat.getColor(AppCar.getContext(), R.color.teal500));
        colores.add(ContextCompat.getColor(AppCar.getContext(), R.color.yellow500));
        colores.add(ContextCompat.getColor(AppCar.getContext(), R.color.cyan500));
        colores.add(ContextCompat.getColor(AppCar.getContext(), R.color.deepred500));
        colores.add(ContextCompat.getColor(AppCar.getContext(), R.color.indigo500));
        colores.add(ContextCompat.getColor(AppCar.getContext(), R.color.deepred500));
        colores.add(ContextCompat.getColor(AppCar.getContext(), R.color.bluelight500));
        return colores;
    }
}
