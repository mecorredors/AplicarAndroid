package car.gov.co.carserviciociudadano;
import android.app.Application;
import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Olger on 26/11/2016.
 */

public class AppCar  extends Application{
    private static Context context;
    private static RequestQueue mVolleyQueue;
    @Override
    public void onCreate(){

        super.onCreate();
        //  if (BuildConfig.DEBUG == false)//Solamente enviar crashes de produccion, no esta funcionando


        AppCar.context = getApplicationContext();

        mVolleyQueue = Volley.newRequestQueue(AppCar.context);

    }

    public static Context getContext() {
        return AppCar.context;
    }
    public static RequestQueue VolleyQueue(){
        return mVolleyQueue;
    }
}