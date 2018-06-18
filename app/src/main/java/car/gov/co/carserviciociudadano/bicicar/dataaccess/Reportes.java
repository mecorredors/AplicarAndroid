package car.gov.co.carserviciociudadano.bicicar.dataaccess;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

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


    public void getEstadistica(String opcion, final IReportes iReportes)
    {
        String url = Config.API_BICICAR_REPORTES_ESTADISTICA +"?opcion="+opcion;
        url = url.replace(" ", "%20");

        JsonArrayRequest objRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            iReportes.onSuccessEstadistica(JSONArrayToList(response));
                        } catch (JSONException ex) {
                            iReportes.onErrorEstadistica(new ErrorApi(ex));
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                iReportes.onErrorEstadistica(new ErrorApi(error));
            }
        }
        ) {
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


    public void getEstadisticaPersona(int idLiderGrupo, int idUsuario, final IReportes iReportes)
    {
        String url = Config.API_BICICAR_REPORTES_ESTADISTICA_PERSONA +"?idLiderGrupo="+idLiderGrupo +"&idUsuario="+idUsuario;
        url = url.replace(" ", "%20");

        JsonArrayRequest objRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            iReportes.onSuccessEstadistica(JSONArrayToList(response));
                        } catch (JSONException ex) {
                            iReportes.onErrorEstadistica(new ErrorApi(ex));
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                iReportes.onErrorEstadistica(new ErrorApi(error));
            }
        }
        ) {
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

    private List<Estadistica> JSONArrayToList(JSONArray response) throws JSONException{
        List<Estadistica> lista = new ArrayList<>();
        for(int i = 0; i < response.length(); i++){
            JSONObject jresponse = response.getJSONObject(i);
            lista.add( Reportes.getItemFromJson(jresponse.toString()));
        }
        return lista;
    }

    public static Estadistica getItemFromJson(String json) throws JsonSyntaxException{
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Gson gson = builder.create();

        Estadistica element = gson.fromJson(json, Estadistica.class);
        return  element;
    }
}
