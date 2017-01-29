package car.gov.co.carserviciociudadano;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.anupcowkur.reservoir.Reservoir;
import com.crashlytics.android.Crashlytics;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Olger on 26/11/2016.
 */

public class AppCar  extends Application{
    private static Context context;
    private static RequestQueue mVolleyQueue;
    @Override
    public void onCreate(){

        super.onCreate();

        if (BuildConfig.DEBUG == false)//Solamente enviar crashes de produccion, no esta funcionando
              Fabric.with(this, new Crashlytics());

        try {
            Reservoir.init(this, 51200);//en bytes = 50 mb
        } catch (Exception e) {
            e.printStackTrace();
        }

        AppCar.context = getApplicationContext();

        mVolleyQueue = Volley.newRequestQueue(AppCar.context);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(false)
                .cacheOnDisk(true)
                //  .showImageOnLoading(R.drawable.sin_foto)
                .showImageForEmptyUri(R.drawable.sin_foto)
                .showImageOnFail(R.drawable.sin_foto)
                .delayBeforeLoading(0)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 1)
                .threadPoolSize(1)
                .denyCacheImageMultipleSizesInMemory()
                //.memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024) // 100 Mb
                .build();

        ImageLoader.getInstance().init(config);

    }

    public static Context getContext() {
        return AppCar.context;
    }
    public static RequestQueue VolleyQueue(){
        return mVolleyQueue;
    }
}