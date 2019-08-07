package car.gov.co.carserviciociudadano.bicicar.dataaccess;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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
import car.gov.co.carserviciociudadano.bicicar.interfaces.ICuenca;
import car.gov.co.carserviciociudadano.bicicar.interfaces.INivel;
import car.gov.co.carserviciociudadano.bicicar.model.Cuenca;
import car.gov.co.carserviciociudadano.bicicar.model.Nivel;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class Cuencas {
    public static final String TAG ="Cuencas";

    public void getCuencas(final ICuenca iCuenca)
    {
        String url = Config.API_BICICAR_CATALOGO_OBTENER_CUENCAS ;
        url = url.replace(" ", "%20");

        JsonArrayRequest objRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            iCuenca.onSuccess(JSONArrayToList(response));
                        } catch (JSONException ex) {
                            iCuenca.onError(new ErrorApi(ex));
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                iCuenca.onError(new ErrorApi(error));
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
                        10000,
                        0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppCar.VolleyQueue().add(objRequest);
    }

    private List<Cuenca> JSONArrayToList(JSONArray response) throws JSONException{
        List<Cuenca> lista = new ArrayList<>();
        for(int i = 0; i < response.length(); i++){
            JSONObject jresponse = response.getJSONObject(i);
            lista.add( Cuencas.getItemFromJson(jresponse.toString()));
        }
        return lista;
    }

    public static Cuenca getItemFromJson(String json) throws JsonSyntaxException {
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Gson gson = builder.create();

        Cuenca element = gson.fromJson(json, Cuenca.class);
        return  element;
    }
}
