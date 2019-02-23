package car.gov.co.carserviciociudadano.parques.dataaccess;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.anupcowkur.reservoir.Reservoir;
import com.anupcowkur.reservoir.ReservoirPutCallback;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.BuildConfig;
import car.gov.co.carserviciociudadano.Utils.Config;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.denunciaambiental.dataacces.Lugares;
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
    public List<Parque> mLstParques;
    public void list(final IParque iParque )
    {
        String url = Config.API_PARQUES_PARQUES;

        url = url.replace(" ", "%20");
        final String keycache = url + BuildConfig.VERSION_CODE;
        Parques parques  = null;
        if (Utils.existeCache(keycache) && !Utils.isOnline(AppCar.getContext())) {
            try {
                parques = Reservoir.get(keycache, Parques.class);
            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
            }
        }
        if (parques != null ){
            Log.d(TAG,"Se lee de cache");
            iParque.onSuccess(parques.mLstParques);
        }else {

            JsonArrayRequest objRequest = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                mLstParques = JSONArrayToList(response);
                                try {
                                    Reservoir.put(keycache,Parques.this, Enumerator.CacheTimeInMilliSeconds.PARQUES);
                                    Log.d(TAG," guaardado cache");
                                } catch (Exception e) {
                                    Log.e(TAG, " guardar cache " + e.toString());
                                }
                               /* Reservoir.putAsync(keycache,Parques.this , new ReservoirPutCallback() {
                                    @Override
                                    public void onSuccess() {
                                       Log.d(TAG,"Parques guaardados cache");
                                    }
                                    @Override
                                    public void onFailure(Exception e) {
                                        Log.e(TAG,"Error guaardar parques cache");
                                    }
                                }, Enumerator.CacheTimeInMilliSeconds.PARQUES);*/

                                iParque.onSuccess(mLstParques);
                            } catch (JSONException ex) {
                                iParque.onError(new ErrorApi(ex));
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    iParque.onError(new ErrorApi(error));
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

    private List<Parque> JSONArrayToList(JSONArray response) throws JSONException{
        List<Parque> lista = new ArrayList<>();
            for(int i = 0; i < response.length(); i++){
                JSONObject jresponse = response.getJSONObject(i);
                lista.add(new Parque(jresponse.toString()));
            }
        return lista;
    }

}
