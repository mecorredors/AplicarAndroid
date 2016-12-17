package car.gov.co.carserviciociudadano.parques.dataaccess;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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
import car.gov.co.carserviciociudadano.parques.interfaces.IDetalleReserva;
import car.gov.co.carserviciociudadano.parques.model.DetalleReserva;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;


/**
 * Created by Olger on 27/11/2016.
 */

public class DetalleReservas {

    public static final String TAG ="DetalleReserva";

    public void list( String login, long idReserva,final IDetalleReserva iDetalleReserva )
    {
        String url = Config.API_PARQUES_RESERVA_DETALLE + "?login=" + login +"&idReserva="+idReserva;
        url = url.replace(" ", "%20");

        JsonArrayRequest objRequest = new JsonArrayRequest( url,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response)
                    {
                        try {
                            iDetalleReserva.onSuccess(JSONArrayToList(response));
                        }catch (JSONException ex){
                            iDetalleReserva.onError(new ErrorApi(ex));
                        }
                    }
                },	new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                iDetalleReserva.onError(new ErrorApi(error));
            }
        }
        ){
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

    private List<DetalleReserva> JSONArrayToList(JSONArray response) throws JSONException{
        List<DetalleReserva> lista = new ArrayList<>();
        for(int i = 0; i < response.length(); i++){
            JSONObject jresponse = response.getJSONObject(i);
            lista.add(new DetalleReserva(jresponse.toString()));
        }
        return lista;
    }
}
