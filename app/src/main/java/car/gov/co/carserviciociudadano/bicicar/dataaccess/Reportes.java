package car.gov.co.carserviciociudadano.bicicar.dataaccess;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.Utils.Config;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.bicicar.interfaces.IReportes;
import car.gov.co.carserviciociudadano.bicicar.model.Estadistica;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

/**
 * Created by apple on 16/06/18.
 */

public class Reportes {

    public static final String TAG ="Reportes";

    public void getGranTotal( final IReportes iReportes)
    {
        String url = Config.API_BICICAR_REPORTES_GRAN_TOTAL;
        url = url.replace(" ", "%20");

        JsonObjectRequest objRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try {
                            iReportes.onSuccessGranTotal(Reportes.getItemFromJson(response.toString()));
                        }catch (JsonSyntaxException ex){
                            iReportes.onErrorGranTotal(new ErrorApi(ex));
                        }
                    }
                },	new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                iReportes.onErrorGranTotal(new ErrorApi(error));
            }
        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> headerMap = new HashMap<>();
                headerMap.put("Authorization", "Basic " + Utils.getAuthorizationBICICAR());
                return headerMap;
            }
        };

        objRequest.setTag(TAG);
        objRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        20000,
                        0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppCar.VolleyQueue().add(objRequest);
    }


    public static Estadistica getItemFromJson(String json) throws JsonSyntaxException{
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Gson gson = builder.create();

        Estadistica element = gson.fromJson(json, Estadistica.class);
        return  element;
    }
}
