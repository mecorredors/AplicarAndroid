package car.gov.co.carserviciociudadano.parques.dataaccess;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.Utils.Config;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.parques.interfaces.IParametro;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.ParametroReserva;

/**
 * Created by Olger on 27/11/2016.
 */

public class ParametrosReserva {

    public static final String TAG ="ParametrosReserva";

    public void list(final IParametro iParametro )
    {
        String url = Config.API_PARQUES_PARAMETROS;
        url = url.replace(" ", "%20");

        JsonArrayRequest objRequest = new JsonArrayRequest( url,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try {
                            iParametro.onSuccess(JSONArrayToList(response));
                        }catch (JSONException ex){
                            iParametro.onError(new ErrorApi(ex));
                        }
                    }
                },	new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                iParametro.onError(new ErrorApi(error));
            }
        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> headerMap = new HashMap<>();
                headerMap.put("Authorization", "Basic " + Utils.getAuthorizationParques());
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

    private List<ParametroReserva> JSONArrayToList(JSONArray response) throws JSONException{
        List<ParametroReserva> lista = new ArrayList<>();
        for(int i = 0; i < response.length(); i++){
            JSONObject jresponse = response.getJSONObject(i);
            lista.add(new ParametroReserva(jresponse.toString()));
        }
        return lista;
    }
}
