package car.gov.co.carserviciociudadano.denunciaambiental.dataacces;


import android.util.Log;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.crashlytics.android.Crashlytics;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.denunciaambiental.interfaces.IElevation;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;


/**
 * Created by Olger on 29/05/2017.
 */
public class Elevation {

    public static final String TAG ="ElevationApi";
    public void getElevation( double latitude, double longitude,final IElevation iElevation )
    {
        String url = "https://maps.googleapis.com/maps/api/elevation/json?locations="+ latitude  +","+ longitude+"&key="+ AppCar.getContext().getResources().getString(R.string.api_key_map);

        JsonObjectRequest objRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        double elevation = 0.0;
                        try {
                            String status = response.getString("status");
                            if (status.equals("OK")){
                                JSONArray result = response.getJSONArray("results");
                                if(result.length()>0){
                                    JSONObject  oElevation = result.getJSONObject(0);
                                    elevation = oElevation.getDouble("elevation");
                                }
                            }else{
                                //Informar en fabric si hay algo mal con la consulta api google o vence cuota diaria
                                Exception exception =   new Exception("Elevation.getElevation consulta api google elevation , statusCode="+status);
                                Crashlytics.logException(exception);
                            }
                        }catch (JSONException ex){
                            ex.printStackTrace();
                            Crashlytics.logException(ex);
                        }
                        iElevation.onResult(elevation);
                    }
                },	new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                iElevation.onErrror(new ErrorApi(error));

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
