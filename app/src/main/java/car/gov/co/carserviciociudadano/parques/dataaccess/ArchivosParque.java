package car.gov.co.carserviciociudadano.parques.dataaccess;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.anupcowkur.reservoir.Reservoir;
import com.anupcowkur.reservoir.ReservoirPutCallback;

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
import car.gov.co.carserviciociudadano.parques.interfaces.IArchivoParque;
import car.gov.co.carserviciociudadano.parques.model.ArchivoParque;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

/**
 * Created by Olger on 27/11/2016.
 */

public class ArchivosParque {

    public static final String TAG ="ArchivosParque";
    public List<ArchivoParque> mlstArchivosParque;
    public void list(final IArchivoParque iArchivosParque, final int idParque )
    {
        String url = Config.API_PARQUES_ARCHIVOS + "?idParque="+ idParque;
        url = url.replace(" ", "%20");
        final String keycache = url + BuildConfig.VERSION_CODE;
        ArchivosParque archivosParque  = null;
        if (Utils.existeCache(keycache)) {
            try {
                archivosParque = Reservoir.get(keycache, ArchivosParque.class);
            } catch (Exception ex) {
                Log.e(TAG, ex.toString());
            }
        }
        if (archivosParque != null ){
            Log.d(TAG,"Se lee de cache");
            iArchivosParque.onSuccess(archivosParque.mlstArchivosParque);
        }else {
            JsonArrayRequest objRequest = new JsonArrayRequest(url,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                 mlstArchivosParque = JSONArrayToList(response);

                                try{
                                    Reservoir.put(keycache,ArchivosParque.this, Enumerator.CacheTimeInMilliSeconds.ARCHIVOS_PARQUE);
                                    Log.d(TAG," guaardado cache");
                                } catch (Exception e) {
                                    Log.e(TAG, "Bancos.list guardar cache "+e.toString());
                                }

                                iArchivosParque.onSuccess(mlstArchivosParque);
                            } catch (JSONException ex) {
                                iArchivosParque.onError(new ErrorApi(ex));
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    iArchivosParque.onError(new ErrorApi(error));
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

    private List<ArchivoParque> JSONArrayToList(JSONArray response) throws JSONException{
        List<ArchivoParque> lista = new ArrayList<>();
        for(int i = 0; i < response.length(); i++){
            JSONObject jresponse = response.getJSONObject(i);
            lista.add(new ArchivoParque(jresponse.toString()));
        }
        return lista;
    }
}
