package car.gov.co.carserviciociudadano.bicicar.dataaccess;

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
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.bicicar.interfaces.IBicicleta;
import car.gov.co.carserviciociudadano.bicicar.model.Bicicleta;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

/**
 * Created by apple on 17/06/18.
 */

public class Bicicletas {

    public static final String TAG ="Beneficiarios";

    public void obtenerItem(int idBeneficiario, final IBicicleta iBicicleta)
    {
        String url = Config.API_BICICAR__OBTENER_BICICLETA + "?idBeneficiario=" + idBeneficiario;
        url = url.replace(" ", "%20");

        JsonObjectRequest objRequest = new JsonObjectRequest(Request.Method.GET, url,null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try {
                            iBicicleta.onSuccessBicicleta(Bicicletas.getItemFromJson(response.toString()));
                        }catch (JsonSyntaxException ex){
                            iBicicleta.onErrorBicicleta(new ErrorApi(ex));
                        }
                    }
                },	new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                iBicicleta.onErrorBicicleta(new ErrorApi(error));
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

    public static Bicicleta getItemFromJson(String json) throws JsonSyntaxException{
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Gson gson = builder.create();

        Bicicleta element = gson.fromJson(json, Bicicleta.class);
        return  element;
    }
}
