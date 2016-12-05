package car.gov.co.carserviciociudadano.parques.dataaccess;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.Utils.Config;
import car.gov.co.carserviciociudadano.Utils.PreferencesApp;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.parques.interfaces.IUsuario;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.Usuario;

/**
 * Created by Olger on 27/11/2016.
 */

public class Usuarios {

    public static final String TAG ="Usuarios";

    public void login( String login, String clave,final IUsuario iUsuario )
    {
        String url = Config.API_PARQUES_USUARIO_LOGIN + "?login="+login+"&clave="+clave;
        url = url.replace(" ", "%20");

        JsonObjectRequest objRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        iUsuario.onSuccess(new Usuario(response.toString()));
                    }
                },	new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                iUsuario.onError(new ErrorApi(error));
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

    public void insert(final IUsuario iUsuario, final Usuario usuario){
        request(iUsuario,usuario, Request.Method.POST);
    }
    public void update(final IUsuario iUsuario, final Usuario usuario){
        request(iUsuario,usuario, Request.Method.PUT);
    }
    private void request(final IUsuario iUsuario, final Usuario usuario,int requestMethod )
    {
        String url = Config.API_PARQUES_USUARIO;

        JsonObjectRequest objRequest = new JsonObjectRequest (
                requestMethod, url,   usuario.toJSONObject() ,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        iUsuario.onSuccess(new Usuario(response.toString()));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        iUsuario.onError(new ErrorApi(error));
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


    public void guardar(Usuario usuario){
        PreferencesApp preferencesApp = new PreferencesApp(PreferencesApp.WRITE,TAG);
        preferencesApp.putInt(Usuario.ID_USUARIO,usuario.getIdUsuario());
        preferencesApp.putString(Usuario.EMAIL_USUARIO,usuario.getEmailUsuario());
        preferencesApp.putString(Usuario.CLAVE_USUARIO,usuario.getClaveUsuario());
        preferencesApp.putString(Usuario.NOMBRE_COMPLETO,usuario.getNombreCompleto());
        preferencesApp.putString(Usuario.TELEFONO_USUARIO,usuario.getTelefonoUsuario());
        preferencesApp.putString(Usuario.CELULAR_USUARIO,usuario.getCelularUsuario());
        preferencesApp.putString(Usuario.DOCUMENTO,usuario.getDocumento());
        preferencesApp.putString(Usuario.DIRECCION_USUARIO,usuario.getDireccionUsuario());
        preferencesApp.putInt(Usuario.ID_MUNICIPIO,usuario.getIdUsuario());
        preferencesApp.commit();
    }

    public Usuario leer(){
        Usuario usuario = new Usuario();

        PreferencesApp preferencesApp = new PreferencesApp(PreferencesApp.READ,TAG);
        usuario.setIdUsuario(preferencesApp.getInt(Usuario.ID_USUARIO,0 ));
        usuario.setEmailUsuario(preferencesApp.getString(Usuario.EMAIL_USUARIO));
        usuario.setClaveUsuario(preferencesApp.getString(Usuario.CLAVE_USUARIO));
        usuario.setNombreCompleto(preferencesApp.getString(Usuario.NOMBRE_COMPLETO));
        usuario.setTelefonoUsuario(preferencesApp.getString(Usuario.TELEFONO_USUARIO));
        usuario.setCelularUsuario(preferencesApp.getString(Usuario.CELULAR_USUARIO));
        usuario.setDireccionUsuario(preferencesApp.getString(Usuario.DIRECCION_USUARIO));
        usuario.setDocumento(preferencesApp.getString(Usuario.DOCUMENTO));
        usuario.setIDMunicipio(preferencesApp.getInt(Usuario.ID_MUNICIPIO,0));

        return usuario;

    }

}
