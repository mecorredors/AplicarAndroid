package car.gov.co.carserviciociudadano.openweather.dataacces;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.openweather.interfaces.IOpenWeather;
import car.gov.co.carserviciociudadano.openweather.model.CurrentWeather;
import car.gov.co.carserviciociudadano.openweather.model.Forecast;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

/**
 * Created by INTEL on 11/04/2018.
 */

public class OpenWeather {

    public static final String TAG ="OpenWeather";
    public void currentWeather( double latitude, double longitude,final IOpenWeather iOpenWeather )
    {
        String url = "http://api.openweathermap.org/data/2.5/weather?lat="+ latitude  +"&lon="+ longitude+"&units=metric&appid="+ AppCar.getContext().getResources().getString(R.string.api_key_open_weather);

        JsonObjectRequest objRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try {
                            GsonBuilder builder = new GsonBuilder();
                          //  builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            Gson gson = builder.create();
                            CurrentWeather currentWeather = gson.fromJson(response.toString(), CurrentWeather.class);
                            iOpenWeather.onSuccessCurrentWeather(currentWeather);
                        }catch (JsonSyntaxException ex){
                            iOpenWeather.onError(new ErrorApi(ex));
                        }

                    }
                },	new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                iOpenWeather.onError(new ErrorApi(error));

            }
        }
        );

        objRequest.setTag(TAG);
        objRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        20000,
                        0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppCar.VolleyQueue().add(objRequest);
    }

    public void forecast(double latitude, double longitude, final boolean day5Hour3, final IOpenWeather iOpenWeather )
    {
        String url = "http://api.openweathermap.org/data/2.5/forecast/daily?lat="+ latitude  +"&lon="+ longitude+"&units=metric&cnt=10&appid="+ AppCar.getContext().getResources().getString(R.string.api_key_open_weather);
        if (day5Hour3) {
            url = "http://api.openweathermap.org/data/2.5/forecast?lat=" + latitude + "&lon=" + longitude + "&units=metric&appid=" + AppCar.getContext().getResources().getString(R.string.api_key_open_weather);
        }

        JsonObjectRequest objRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try {
                            GsonBuilder builder = new GsonBuilder();
                            //  builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            Gson gson = builder.create();
                            Forecast forecast = gson.fromJson(response.toString().replace("3h","h3"), Forecast.class);
                            if (day5Hour3)
                                iOpenWeather.onSuccessForecast5Day3Hour(forecast);
                            else
                                iOpenWeather.onSuccessForecast16Daily(forecast);
                        }catch (JsonSyntaxException ex){
                            iOpenWeather.onError(new ErrorApi(ex));
                        }
                    }
                },	new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                iOpenWeather.onError(new ErrorApi(error));
            }
        }
        );

        objRequest.setTag(TAG);
        objRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        20000,
                        0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppCar.VolleyQueue().add(objRequest);
    }
}
