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
import car.gov.co.carserviciociudadano.parques.interfaces.IBanco;
import car.gov.co.carserviciociudadano.parques.model.Banco;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

/**
 * Created by Olger on 27/11/2016.
 */

public class Bancos {

    public static final String TAG ="Bancos";
    public  List<Banco> mLstBancos;
    public void list(final IBanco iBanco )
    {
        String url = Config.API_PARQUES_BANCOS;
        url = url.replace(" ", "%20");
        final String keycache = url + BuildConfig.VERSION_CODE;
        Bancos bancos  = null;
        if (Utils.existeCache(keycache)) {
            try {
                bancos = Reservoir.get(keycache, Bancos.class);
            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
            }
        }
        if (bancos != null ){
            Log.d(TAG,"Se lee de cache");
            iBanco.onSuccess(bancos.mLstBancos);
        }else {
            JsonArrayRequest objRequest = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                mLstBancos= JSONArrayToList(response);
                                try {
                                    Reservoir.put(keycache, Bancos.this, Enumerator.CacheTimeInMilliSeconds.BANCOS);
                                    Log.d(TAG," guaardado cache");
                                } catch (Exception e) {
                                    Log.e(TAG, "Bancos.list guardar cache " + e.toString());
                                }
                                iBanco.onSuccess(mLstBancos);
                            } catch (JSONException ex) {
                                iBanco.onError(new ErrorApi(ex));
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    iBanco.onError(new ErrorApi(error));
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

    private List<Banco> JSONArrayToList(JSONArray response) throws JSONException{
        List<Banco> lista = new ArrayList<>();
        for(int i = 0; i < response.length(); i++){
            JSONObject jresponse = response.getJSONObject(i);
            lista.add(new Banco(jresponse.toString()));
        }
        return lista;
    }
}
