package car.gov.co.carserviciociudadano.parques.dataaccess;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.Utils.Config;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.parques.interfaces.IReserva;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.ServicioReserva;

/**
 * Created by Olger on 27/11/2016.
 */

public class Reservas {
    public static final String TAG ="Reservas";

    public void insert(final IReserva iReserva, final ServicioReserva servicioReserva )
    {
        String url = Config.API_PARQUES_RESERVA;

        JsonObjectRequest objRequest = new JsonObjectRequest (
                Request.Method.POST, url,   servicioReserva.toJSONObject() ,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            servicioReserva.setIDReserva(response.getInt("IDReserva"));
                            iReserva.onSuccess(servicioReserva);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            iReserva.onError(new ErrorApi(e));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        iReserva.onError(new ErrorApi(error));
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Basic " + Utils.getAuthorizationParques());
                return headers;
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

    public void cancelarReserva(final IReserva iReserva, final ServicioReserva servicioReserva )
    {
        String url = Config.API_PARQUES_CANCELAR_RESERVA;

        JsonObjectRequest objRequest = new JsonObjectRequest (
                Request.Method.PUT, url,   servicioReserva.toJSONObject() ,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                       iReserva.onSuccess(servicioReserva);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        iReserva.onError(new ErrorApi(error));
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Basic " + Utils.getAuthorizationParques());
                return headers;
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