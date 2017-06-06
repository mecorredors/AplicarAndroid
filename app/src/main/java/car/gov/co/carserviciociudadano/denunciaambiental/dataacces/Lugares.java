package car.gov.co.carserviciociudadano.denunciaambiental.dataacces;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
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
import car.gov.co.carserviciociudadano.denunciaambiental.interfaces.ILugar;
import car.gov.co.carserviciociudadano.denunciaambiental.model.Lugar;
import car.gov.co.carserviciociudadano.parques.dataaccess.Municipios;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;


/**
 * Created by Olger on 26/05/2017.
 */

public class Lugares {
    public static final String TAG ="Lugares";
    public List<Lugar> mLstLugares;
    public void list(String idLugarPadre, int tipoLugar, final ILugar iLugar )
    {
        String url = Config.API_SIDCAR_LUGARES +"?tipo="+tipoLugar+"&idlugarpadre="+idLugarPadre;
        url = url.replace(" ", "%20");
        final String keycache = url + BuildConfig.VERSION_CODE;
        Lugares lugares  = null;
        if (Utils.existeCache(keycache)) {
            try {
                lugares = Reservoir.get(keycache, Lugares.class);
            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
            }
        }
        if (lugares != null ){
            Log.d(TAG,"Se lee de cache");
            iLugar.onSuccess(lugares.mLstLugares);
        }else {
            JsonArrayRequest objRequest = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                mLstLugares = JSONArrayToList(response);
                                try {
                                    Reservoir.put(keycache,Lugares.this,  Enumerator.CacheTimeInMilliSeconds.MUNICIPIOS);
                                    Log.d(TAG," guaardado cache");
                                } catch (Exception e) {
                                    Log.e(TAG, " guardar cache " + e.toString());
                                }
                                iLugar.onSuccess(mLstLugares);
                            } catch (JSONException ex) {
                                iLugar.onError(new ErrorApi(ex));
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    iLugar.onError(new ErrorApi(error));
                }
            }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String, String> headerMap = new HashMap<>();
                    headerMap.put("Authorization", "Basic " + Utils.getAuthorizationSIDCAR());
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

    public void getIdxCoordenada(String norte, String este, final ILugar iLugar )
    {
        String url = Config.API_SIDCAR_ID_LUGAR_X_COORDENADA +"?norte="+norte+"&este="+este;
        url = url.replace(" ", "%20");

        StringRequest objRequest = new StringRequest( url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response)
                    {
                       iLugar.onSuccessIdXCoordenada(response.replace("\"",""));
                    }
                },	new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                iLugar.onError(new ErrorApi(error));
            }
        }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerMap = new HashMap<>();
                headerMap.put("Authorization", "Basic " + Utils.getAuthorizationSIDCAR());
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

    private List<Lugar> JSONArrayToList(JSONArray response) throws JSONException{
        List<Lugar> lista = new ArrayList<>();
        for(int i = 0; i < response.length(); i++){
            JSONObject jresponse = response.getJSONObject(i);
            lista.add(new Lugar(jresponse.toString()));
        }
        return lista;
    }
}
