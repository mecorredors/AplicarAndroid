package car.gov.co.carserviciociudadano.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by apple on 9/09/18.
 */

public class APIClient {
    private static Retrofit retrofit = null;

   public static Retrofit getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

       GsonBuilder builder = new GsonBuilder();
       builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
       Gson gson = builder.create();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.17/aplicar/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();



        return retrofit;
    }
}
