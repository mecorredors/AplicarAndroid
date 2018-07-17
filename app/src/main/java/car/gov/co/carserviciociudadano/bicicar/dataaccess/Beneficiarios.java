package car.gov.co.carserviciociudadano.bicicar.dataaccess;


import android.util.Log;

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
import car.gov.co.carserviciociudadano.Utils.PreferencesApp;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.bicicar.interfaces.IBeneficiario;
import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.bicicar.model.RespuestaApi;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class Beneficiarios {
    public static final String TAG ="Beneficiarios";

    public void login(String numeroID, String claveApp , final IBeneficiario iBeneficiario)
    {
        String url = Config.API_BICICAR_LOGIN + "?numeroID=" + numeroID +"&claveApp=" + claveApp;
        url = url.replace(" ", "%20");

        JsonObjectRequest objRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try {
                            iBeneficiario.onSuccess(Beneficiarios.getItemFromJson(response.toString()));
                        }catch (JsonSyntaxException ex){
                            iBeneficiario.onError(new ErrorApi(ex));
                        }
                    }
                },	new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                iBeneficiario.onError(new ErrorApi(error));
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

    public void obtenerItem(String serial, String rin , final IBeneficiario iBeneficiario)
    {
        String url = Config.API_BICICAR_OBTENER_BENEFICIARIO + "?serial=" + serial +"&rin=" + rin;
        url = url.replace(" ", "%20");

        JsonObjectRequest objRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try {
                            iBeneficiario.onSuccess(Beneficiarios.getItemFromJson(response.toString()));
                        }catch (JsonSyntaxException ex){
                            iBeneficiario.onError(new ErrorApi(ex));
                        }
                    }
                },	new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                iBeneficiario.onError(new ErrorApi(error));
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


    public static Beneficiario getItemFromJson(String json) throws JsonSyntaxException{
            GsonBuilder builder = new GsonBuilder();
            builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Gson gson = builder.create();

            Beneficiario element = gson.fromJson(json, Beneficiario.class);
            return  element;
    }

    public static Beneficiario readBeneficio(){
        PreferencesApp preferencesApp = new PreferencesApp(PreferencesApp.READ, PreferencesApp.BICIAR_NAME);
        String json = preferencesApp.getString(Beneficiario.BICICAR_USUARIO, null);
        if (json != null){
            Log.d("json", json);
            try {
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                Beneficiario element = gson.fromJson(json, Beneficiario.class);
                return element;
            }catch (JsonSyntaxException ex) {
            }
        }
        return  null;
    }

    public void recordarClave(final Beneficiario beneficiario, final IBeneficiario iBeneficiario )
    {
        String url = Config.API_BICICAR__RECORDAR_CLAVE;

        JsonObjectRequest objRequest = new JsonObjectRequest (
                Request.Method.POST, url,   beneficiario.toJSONObject() ,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            GsonBuilder builder = new GsonBuilder();
                            Gson gson = builder.create();
                            RespuestaApi respuesta = gson.fromJson(response.toString(), RespuestaApi.class);
                            iBeneficiario.onSuccessRecordarClave(respuesta.Mensaje);
                        }catch (JsonSyntaxException ex){
                            iBeneficiario.onErrorRecordarClave(new ErrorApi(ex));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        iBeneficiario.onErrorRecordarClave(new ErrorApi(error));
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Basic " + Utils.getAuthorizationBICICAR());
                return headers;
            }
        };

        objRequest.setTag(TAG);
        objRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        40000,
                        0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppCar.VolleyQueue().add(objRequest);
    }
}
