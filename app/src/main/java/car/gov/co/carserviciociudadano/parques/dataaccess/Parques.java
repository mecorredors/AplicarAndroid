package car.gov.co.carserviciociudadano.parques.dataaccess;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.anupcowkur.reservoir.Reservoir;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.Utils.Config;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.parques.interfaces.IParque;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.Parque;
import java.util.HashMap;
import java.util.Map;
/**
 * Created by Olger on 26/11/2016.
 */

public class Parques {

    public static final String TAG ="Parques";

    public void list(final IParque iParque )
    {
        String url = Config.API_PARQUES_PARQUES;
        url = url.replace(" ", "%20");

        JsonArrayRequest objRequest = new JsonArrayRequest( url,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try {
                            List<Parque> lstParques = JSONArrayToList(response);
                            try {
                                Reservoir.put(TAG, lstParques);
                                Utils.putFechaCache(TAG);
                            } catch (Exception e) {
                                Log.e(TAG, "Parques.list guardar cache "+e.toString());
                            }
                            iParque.onSuccess(lstParques);
                        }catch (JSONException ex){
                            iParque.onError(new ErrorApi(ex));
                        }

                    }
                },	new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
               iParque.onError(new ErrorApi(error));
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

    private List<Parque> JSONArrayToList(JSONArray response) throws JSONException{
        List<Parque> lista = new ArrayList<>();
            for(int i = 0; i < response.length(); i++){
                JSONObject jresponse = response.getJSONObject(i);
                lista.add(new Parque(jresponse.toString()));
            }
        return lista;
    }

}
