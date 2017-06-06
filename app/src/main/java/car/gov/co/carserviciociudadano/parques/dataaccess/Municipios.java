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
import car.gov.co.carserviciociudadano.parques.interfaces.IMunicipio;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.Municipio;


/**
 * Created by Olger on 27/11/2016.
 */

public class Municipios {
    public static final String TAG ="Municipios";
    public  List<Municipio> mLstMunicipios;
    public void list(final IMunicipio iMunicipio )
    {
        String url = Config.API_PARQUES_MUNICIPIOS;
        url = url.replace(" ", "%20");
        final String keycache = url + BuildConfig.VERSION_CODE;
        Municipios municipios  = null;
        if (Utils.existeCache(keycache)) {
            try {
                municipios = Reservoir.get(keycache, Municipios.class);
            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
            }
        }
        if (municipios != null ){
            Log.d(TAG,"Se lee de cache");
            iMunicipio.onSuccess(municipios.mLstMunicipios);
        }else {
            JsonArrayRequest objRequest = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                mLstMunicipios = JSONArrayToList(response);
                                try {
                                    Reservoir.put(keycache, Municipios.this, Enumerator.CacheTimeInMilliSeconds.MUNICIPIOS);
                                    Log.d(TAG," guaardado cache");
                                } catch (Exception e) {
                                    Log.e(TAG, "Municipios.list guardar cache " + e.toString());
                                }
                                iMunicipio.onSuccess(mLstMunicipios);
                            } catch (JSONException ex) {
                                iMunicipio.onError(new ErrorApi(ex));
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    iMunicipio.onError(new ErrorApi(error));
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

    private List<Municipio> JSONArrayToList(JSONArray response) throws JSONException{
        List<Municipio> lista = new ArrayList<>();
        for(int i = 0; i < response.length(); i++){
            JSONObject jresponse = response.getJSONObject(i);
            lista.add(new Municipio(jresponse.toString()));
        }
        return lista;
    }
}
