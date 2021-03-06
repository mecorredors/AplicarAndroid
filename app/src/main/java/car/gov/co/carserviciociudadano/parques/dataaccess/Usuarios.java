package car.gov.co.carserviciociudadano.parques.dataaccess;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

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

    public void login(String login, final String clave, final IUsuario iUsuario )
    {
        String url = Config.API_PARQUES_USUARIO_LOGIN + "?login="+login+"&clave="+clave;
        url = url.replace(" ", "%20");

        JsonObjectRequest objRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        Usuario usuarioRes = new Usuario(response.toString());
                        usuarioRes.setClaveUsuario(clave);
                        iUsuario.onSuccess(usuarioRes);
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
        String url = requestMethod == Request.Method.POST ? Config.API_PARQUES_USUARIO_INSERTAR : Config.API_PARQUES_USUARIO_ACTUALIZAR;

        JsonObjectRequest objRequest = new JsonObjectRequest (
                requestMethod, url,   usuario.toJSONObject() ,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Usuario usuarioRes = new Usuario(response.toString());
                        usuarioRes.setClaveUsuario(usuario.getClaveUsuario());
                        usuarioRes.setLoginSIDCAR(usuario.isLoginSIDCAR());
                        iUsuario.onSuccess(usuarioRes);
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
                        40000,
                        0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppCar.VolleyQueue().add(objRequest);
    }


    public void recuperarContrasena(final IUsuario iUsuario, final Usuario usuario )
    {
        String url =  Config.API_PARQUES_USUARIO_RECUPERAR_CONTRASENA;

        JsonObjectRequest objRequest = new JsonObjectRequest (
                Request.Method.POST, url,   usuario.toJSONObject() ,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        iUsuario.onSuccess(usuario);
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
                        40000,
                        0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppCar.VolleyQueue().add(objRequest);
    }

    public void cambiarContrasena( final Usuario usuario, final IUsuario iUsuario )
    {
        String url =  Config.API_PARQUES_USUARIO_CAMBIAR_CONTRASENA;

        JsonObjectRequest objRequest = new JsonObjectRequest (
                Request.Method.POST, url,   usuario.toJSONObject() ,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        iUsuario.onSuccess(usuario);
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
                        40000,
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
        preferencesApp.putInt(Usuario.ID_MUNICIPIO,usuario.getIDMunicipio());
        preferencesApp.putBoolean(Usuario.FUNCIONARIO_CAR,usuario.isFuncionarioCar());
        preferencesApp.putBoolean(Usuario.LOGIN_SIDCAR,usuario.isLoginSIDCAR());
        preferencesApp.commit();
    }

    public Usuario leer(){
        Usuario usuario = new Usuario();

        PreferencesApp preferencesApp = new PreferencesApp(PreferencesApp.READ,TAG);
        usuario.setIdUsuario(preferencesApp.getInt(Usuario.ID_USUARIO,0 ));
        usuario.setEmailUsuario(preferencesApp.getString(Usuario.EMAIL_USUARIO));
        usuario.setLogin(preferencesApp.getString(Usuario.EMAIL_USUARIO));
        usuario.setClaveUsuario(preferencesApp.getString(Usuario.CLAVE_USUARIO));
        usuario.setNombreCompleto(preferencesApp.getString(Usuario.NOMBRE_COMPLETO));
        usuario.setTelefonoUsuario(preferencesApp.getString(Usuario.TELEFONO_USUARIO));
        usuario.setCelularUsuario(preferencesApp.getString(Usuario.CELULAR_USUARIO));
        usuario.setDireccionUsuario(preferencesApp.getString(Usuario.DIRECCION_USUARIO));
        usuario.setDocumento(preferencesApp.getString(Usuario.DOCUMENTO));
        usuario.setIDMunicipio(preferencesApp.getInt(Usuario.ID_MUNICIPIO,0));
        usuario.setFuncionarioCar(preferencesApp.getBoolean(Usuario.FUNCIONARIO_CAR,false));
        usuario.setLoginSIDCAR(preferencesApp.getBoolean(Usuario.LOGIN_SIDCAR,false));

        return usuario;

    }


    public void loginSIDCAR( String login, String password, final IUsuario iUsuario)
    {
        String url = Config.API_VISITA_TENICA_LOGIN + "?login="+login+"&password="+password;
        url = url.replace(" ", "%20");
        Log.d("Url login", url );
        StringRequest objRequest = new  StringRequest(Request.Method.GET, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response)
                    {
                        iUsuario.onSuccessSIDCAR(response.equals("true"));
                    }
                },	new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                iUsuario.onError(new ErrorApi(error));
            }
        }){
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
