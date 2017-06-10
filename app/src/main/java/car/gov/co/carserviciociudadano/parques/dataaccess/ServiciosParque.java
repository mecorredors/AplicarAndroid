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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.BuildConfig;
import car.gov.co.carserviciociudadano.Utils.Config;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.parques.interfaces.IServicioParque;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.ServicioParque;

/**
 * Created by Olger on 27/11/2016.
 */

public class ServiciosParque {
    public static final String TAG ="ServiciosParque";
    public List<ServicioParque> mLstServiciosParque;
    public void list(final IServicioParque iServicioParque, final int idParque )
    {
        String url = Config.API_PARQUES_SERVICIOS + "?idParque=" + idParque;
        url = url.replace(" ", "%20");
        final String keycache = url + BuildConfig.VERSION_CODE;
        ServiciosParque serviciosParque  = null;
        if (Utils.existeCache(keycache)) {
            try {
                serviciosParque = Reservoir.get(keycache, ServiciosParque.class);
            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
            }
        }
        if (serviciosParque != null ){
            Log.d(TAG,"Se lee de cache");

            iServicioParque.onSuccess(serviciosParque.mLstServiciosParque);
        }else {
            JsonArrayRequest objRequest = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                mLstServiciosParque = JSONArrayToList(response);
                                try {
                                    Reservoir.put(keycache,ServiciosParque.this, Enumerator.CacheTimeInMilliSeconds.SERVICIOS_PARQUE);
                                    Log.d(TAG," guaardado cache");
                                } catch (Exception e) {
                                    Log.e(TAG, "ServiciosParques.list guardar cache " + e.toString());
                                }
                                iServicioParque.onSuccess(mLstServiciosParque);
                            } catch (JSONException ex) {
                                iServicioParque.onError(new ErrorApi(ex));
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    iServicioParque.onError(new ErrorApi(error));
                }
            }
            ) {
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
    }

    private List<ServicioParque> JSONArrayToList(JSONArray response) throws JSONException{
        List<ServicioParque> lista = new ArrayList<>();
        for(int i = 0; i < response.length(); i++){
            JSONObject jresponse = response.getJSONObject(i);
            lista.add(new ServicioParque(jresponse.toString()));
        }
        return lista;
    }
}
