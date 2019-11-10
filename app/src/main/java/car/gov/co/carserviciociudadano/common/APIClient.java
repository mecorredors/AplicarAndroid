package car.gov.co.carserviciociudadano.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import car.gov.co.carserviciociudadano.Utils.Server;
import car.gov.co.carserviciociudadano.Utils.Utils;
import okhttp3.Headers;
import okhttp3.Interceptor;
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

       //Create a new Interceptor.
        Interceptor headerAuthorizationInterceptor = new Interceptor() {
           @Override
           public okhttp3.Response intercept(Chain chain) throws IOException {
               okhttp3.Request request = chain.request();
               Headers headers = request.headers().newBuilder().add("Authorization", "Basic " + Utils.getAuthorizationBICICAR()).build();
               request = request.newBuilder().headers(headers).build();

               return chain.proceed(request);
           }
       };


        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

      //  OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

       OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
       clientBuilder.addInterceptor(headerAuthorizationInterceptor);
       clientBuilder.addInterceptor(interceptor);
       OkHttpClient client = clientBuilder.connectTimeout(20, TimeUnit.MINUTES)
               .readTimeout(20, TimeUnit.SECONDS)
               .writeTimeout(20, TimeUnit.SECONDS) .build();

       GsonBuilder builder = new GsonBuilder();
       builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
       Gson gson = builder.create();

        retrofit = new Retrofit.Builder()
                .baseUrl(Server.ServerBICICAR())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();

        return retrofit;
    }
}
