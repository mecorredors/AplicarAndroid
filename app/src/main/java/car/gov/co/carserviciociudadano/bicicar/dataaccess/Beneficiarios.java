package car.gov.co.carserviciociudadano.bicicar.dataaccess;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
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
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.PreferencesApp;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.bicicar.interfaces.IBeneficiario;
import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.bicicar.model.LogTrayecto;
import car.gov.co.carserviciociudadano.bicicar.model.RespuestaApi;
import car.gov.co.carserviciociudadano.common.DbHelper;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class Beneficiarios {
    public static final String TAG ="Beneficiarios";
    private DbHelper _dbHelper;

    private void InitDbHelper()
    {
        if(this._dbHelper == null)
            this._dbHelper = new DbHelper(AppCar.getContext());
    }

    private static String[] projectionDefault()
    {
        return new String[] {
                "[" + Beneficiario.ID_BENEFICIARIO + "]",
                "[" + Beneficiario.TIPO_NUMERO_ID + "]",
                "[" + Beneficiario.NUMERO_ID + "]",
                "[" + Beneficiario.ID_MUNICIPIO_VIVE + "]",
                "[" + Beneficiario.ID_VEREDA + "]",
                "[" + Beneficiario.ID_COLEGIO + "]",
                "[" + Beneficiario.ID_ESTADO + "]",
                "[" + Beneficiario.ID_PEDAGOGO + "]",
                "[" + Beneficiario.APELLIDOS + "]",
                "[" + Beneficiario.NOMBRES + "]",
                "[" + Beneficiario.FECHA_NACIMIENTO + "]",
                "[" + Beneficiario.GENERO + "]",
                "[" + Beneficiario.CURSO + "]",
                "[" + Beneficiario.NOMBRE_ACUDIENTE + "]",
                "[" + Beneficiario.TELEFONO_CONTACTO + "]",
                "[" + Beneficiario.ESTATURA + "]",
                "[" + Beneficiario.PESO + "]",
                "[" + Beneficiario.R_H + "]",
                "[" + Beneficiario.ID_FOTO + "]",
                "[" + Beneficiario.DISTANCIA_KM + "]",
                "[" + Beneficiario.ID_PERFIL + "]",
                "[" + Beneficiario.CLAVE_APP + "]",
                "[" + Beneficiario.EMAIL + "]",
                "[" + Beneficiario.FECHA_ESTADO + "]",
                "[" + Beneficiario.FECHA_CREACION + "]",
                "[" + Beneficiario.USUARIO_MODIFICACION + "]",
                "[" + Beneficiario.DIRECCION + "]",
                "[" + Beneficiario.PERSONA_EMERGENCIA + "]",
                "[" + Beneficiario.TELEFONO_EMERGENCIA + "]",
                "[" + Beneficiario.EPS + "]",
                "[" + Beneficiario.ID_BICICLETA + "]",
                "[" + Beneficiario.LINK_FOTO + "]",
                "[" + Beneficiario.DESC_PERFIL + "]",
                "[" + Beneficiario.LATITUDE + "]",
                "[" + Beneficiario.LONGITUDE + "]",
                "[" + Beneficiario.NORTE + "]",
                "[" + Beneficiario.ESTE + "]",
                "[" + Beneficiario.ESTADO + "]"
        };
    }

    public boolean Insert(Beneficiario element) {
        InitDbHelper();
        ContentValues cv = new ContentValues();

        cv.put(Beneficiario.ID_BENEFICIARIO, element.IDBeneficiario);
        cv.put(Beneficiario.TIPO_NUMERO_ID, element.TipoNumeroID);
        cv.put(Beneficiario.NUMERO_ID, element.NumeroID);
        cv.put(Beneficiario.ID_MUNICIPIO_VIVE, element.IDMunicipioVive);
        cv.put(Beneficiario.ID_VEREDA, element.IDVeredaVive);
        cv.put(Beneficiario.ID_COLEGIO, element.IDColegio);
        cv.put(Beneficiario.ID_ESTADO, element.IDEstado);
        cv.put(Beneficiario.ID_PEDAGOGO, element.IDPedagogo);
        cv.put(Beneficiario.APELLIDOS, element.Apellidos);
        cv.put(Beneficiario.NOMBRES, element.Nombres);
        cv.put(Beneficiario.FECHA_NACIMIENTO, Utils.toStringSQLLite(element.FechaNacimiento));
        cv.put(Beneficiario.GENERO, element.Genero);
        cv.put(Beneficiario.CURSO, element.Curso);
        cv.put(Beneficiario.NOMBRE_ACUDIENTE, element.NombreAcudiente);
        cv.put(Beneficiario.TELEFONO_CONTACTO, element.TelefonoContacto);
        cv.put(Beneficiario.ESTATURA, element.Estatura);
        cv.put(Beneficiario.PESO, element.Peso);
        cv.put(Beneficiario.R_H, element.RH);
        cv.put(Beneficiario.ID_FOTO, element.IDFoto);
        cv.put(Beneficiario.DISTANCIA_KM, element.DistanciaKm);
        cv.put(Beneficiario.ID_PERFIL, element.IDPerfil);
        cv.put(Beneficiario.EMAIL, element.Email);
        cv.put(Beneficiario.FECHA_ESTADO, Utils.toStringSQLLite(element.FechaEstado));
        cv.put(Beneficiario.FECHA_CREACION, Utils.toStringSQLLite(element.FechaCreacion));
        cv.put(Beneficiario.USUARIO_MODIFICACION, element.UsuarioModificacion);
        cv.put(Beneficiario.DIRECCION, element.DIRECCION);
        cv.put(Beneficiario.PERSONA_EMERGENCIA, element.PersonaEmergecia);
        cv.put(Beneficiario.TELEFONO_EMERGENCIA, element.TelefonoEmergencia);
        cv.put(Beneficiario.EPS, element.Eps);
        cv.put(Beneficiario.ID_BICICLETA, element.IDBicicleta);
        cv.put(Beneficiario.LINK_FOTO, element.LinkFoto);
        cv.put(Beneficiario.DESC_PERFIL, element.DescPerfil);
        cv.put(Beneficiario.DIRECCION, element.Direccion);
        cv.put(Beneficiario.LATITUDE, element.Latitude);
        cv.put(Beneficiario.LONGITUDE, element.Longitude);
        cv.put(Beneficiario.NORTE, element.Norte);
        cv.put(Beneficiario.ESTE, element.Este);
        cv.put(Beneficiario.ESTADO, element.Estado);

        long rowid = 0;

        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        try {
            rowid=db.insertOrThrow(Beneficiario.TABLE_NAME, "", cv);
        }
        catch(Exception ex)
        {
            if(ex!=null){
                Log.d("Beneficiarios.Insert", ex.toString());
                // Crashlytics.logException(ex);
            }
            rowid = -1;
        }

        db.close();

        return (rowid > 0);
    }

    public boolean Update(Beneficiario element) {
        InitDbHelper();
        ContentValues cv = new ContentValues();

        String selection = Beneficiario.ID_BENEFICIARIO + " = ? ";
        String[] selectionArgs = {String.valueOf(element.IDBeneficiario)};

        cv.put(Beneficiario.TIPO_NUMERO_ID, element.TipoNumeroID);
        cv.put(Beneficiario.NUMERO_ID, element.NumeroID);
        cv.put(Beneficiario.ID_MUNICIPIO_VIVE, element.IDMunicipioVive);
        cv.put(Beneficiario.ID_VEREDA, element.IDVeredaVive);
        cv.put(Beneficiario.ID_COLEGIO, element.IDColegio);
        cv.put(Beneficiario.ID_ESTADO, element.IDEstado);
        cv.put(Beneficiario.ID_PEDAGOGO, element.IDPedagogo);
        cv.put(Beneficiario.APELLIDOS, element.Apellidos);
        cv.put(Beneficiario.NOMBRES, element.Nombres);
        cv.put(Beneficiario.FECHA_NACIMIENTO, Utils.toStringSQLLite(element.FechaNacimiento));
        cv.put(Beneficiario.GENERO, element.Genero);
        cv.put(Beneficiario.CURSO, element.Curso);
        cv.put(Beneficiario.NOMBRE_ACUDIENTE, element.NombreAcudiente);
        cv.put(Beneficiario.TELEFONO_CONTACTO, element.TelefonoContacto);
        cv.put(Beneficiario.ESTATURA, element.Estatura);
        cv.put(Beneficiario.PESO, element.Peso);
        cv.put(Beneficiario.R_H, element.RH);
        cv.put(Beneficiario.ID_FOTO, element.IDFoto);
        cv.put(Beneficiario.DISTANCIA_KM, element.DistanciaKm);
        cv.put(Beneficiario.ID_PERFIL, element.IDPerfil);
        cv.put(Beneficiario.EMAIL, element.Email);
        cv.put(Beneficiario.FECHA_ESTADO, Utils.toStringSQLLite(element.FechaEstado));
        cv.put(Beneficiario.FECHA_CREACION, Utils.toStringSQLLite(element.FechaCreacion));
        cv.put(Beneficiario.USUARIO_MODIFICACION, element.UsuarioModificacion);
        cv.put(Beneficiario.DIRECCION, element.DIRECCION);
        cv.put(Beneficiario.PERSONA_EMERGENCIA, element.PersonaEmergecia);
        cv.put(Beneficiario.TELEFONO_EMERGENCIA, element.TelefonoEmergencia);
        cv.put(Beneficiario.EPS, element.Eps);
        cv.put(Beneficiario.ID_BICICLETA, element.IDBicicleta);
        cv.put(Beneficiario.LINK_FOTO, element.LinkFoto);
        cv.put(Beneficiario.DESC_PERFIL, element.DescPerfil);
        cv.put(Beneficiario.DIRECCION, element.Direccion);
        cv.put(Beneficiario.LATITUDE, element.Latitude);
        cv.put(Beneficiario.LONGITUDE, element.Longitude);
        cv.put(Beneficiario.NORTE, element.Norte);
        cv.put(Beneficiario.ESTE, element.Este);
        cv.put(Beneficiario.ESTADO, element.Estado);

        long rowid = 0;

        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        try {
            rowid=db.update(Beneficiario.TABLE_NAME, cv,selection, selectionArgs);

        }
        catch(Exception ex)
        {
            if(ex!=null){
                Log.d("Beneficiarios.Insert", ex.toString());
                // Crashlytics.logException(ex);
            }
            rowid = -1;
        }

        db.close();

        return (rowid > 0);
    }

    public boolean Delete(long id) {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        String selection = Beneficiario.ID_BENEFICIARIO + " = ? ";
        String[] selectionArgs = {String.valueOf(id)};
        int result = db.delete(Beneficiario.TABLE_NAME, selection, selectionArgs);
        return (result > 0);
    }

    public boolean DeleteAll() {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        int result = db.delete(Beneficiario.TABLE_NAME, null, null);
        return (result > 0);
    }

    public List<Beneficiario> List(){
        return List(null);
    }

    public List<Beneficiario> List(String curso, int idColegio){
        String where = Beneficiario.CURSO + "="+ curso + " and " + Beneficiario.ID_COLEGIO + "= " + idColegio ;
        return List(where);
    }
    public List<Beneficiario> List( int idColegio){
        String where =  Beneficiario.ID_COLEGIO + "= " + idColegio ;
        return List(where);
    }

    public List<Beneficiario> List(String where)
    {   synchronized (this) {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        List<Beneficiario> lstBeneficiarios = new ArrayList<>();

        try {

            Cursor c = db.query(Beneficiario.TABLE_NAME, projectionDefault(), where, null, null, null, "[" + Beneficiario.ID_BENEFICIARIO + "] DESC");

            if (c.moveToFirst()) {
                do {
                    lstBeneficiarios.add(new Beneficiario(c));
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception ex) {
            Log.d("Categories.List", ex.getMessage());
        }

        db.close();

        return lstBeneficiarios;
    }
    }

    public Beneficiario Read(int id)
    {
        synchronized (this) {
            InitDbHelper();
            SQLiteDatabase db = _dbHelper.getReadableDatabase();
            Beneficiario beneficiario = null;
            try {
                String where = Beneficiario.ID_BENEFICIARIO + "=?";
                String[] selectionArgs = {String.valueOf(id)};
                Cursor c = db.query(Beneficiario.TABLE_NAME, projectionDefault(), where, selectionArgs, null, null, "[" + Beneficiario.ID_BENEFICIARIO + "] DESC");

                if (c.moveToFirst()) {
                    beneficiario = new Beneficiario(c);
                }
                c.close();
            } catch (Exception ex) {
                Log.d("Categories.List", ex.getMessage());
            }

            db.close();

            return beneficiario;
        }
    }

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
        Log.d(TAG, url);
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

    public void listarItems(String curso, int idColegio, final IBeneficiario iBeneficiario)
    {
        String url = "";
        if (curso != null)
            url = Config.API_BICICAR_LISTAR_BENEFICIARIOS+"?curso=" + curso + "&idColegio=" + idColegio;
        else
            url = Config.API_BICICAR_LISTAR_BENEFICIARIOS+"?idColegio=" + idColegio;

        url = url.replace(" ", "%20");

        JsonArrayRequest objRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            iBeneficiario.onSuccess(JSONArrayToList(response), true);
                        } catch (JSONException ex) {
                            iBeneficiario.onErrorListarItems(new ErrorApi(ex));
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                iBeneficiario.onErrorListarItems(new ErrorApi(error));
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

    private List<Beneficiario> JSONArrayToList(JSONArray response) throws JSONException{
        List<Beneficiario> lista = new ArrayList<>();
        for(int i = 0; i < response.length(); i++){
            JSONObject jresponse = response.getJSONObject(i);
            lista.add( Beneficiarios.getItemFromJson(jresponse.toString()));
        }
        return lista;
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

    public void actualizar(final Beneficiario beneficiario, final IBeneficiario iBeneficiario )
    {
        String url = Config.API_BICICAR_BENEFICIARIO_ACTUALIZAR;

        JsonObjectRequest objRequest = new JsonObjectRequest (
                Request.Method.POST, url,   beneficiario.toJSONObject() ,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            iBeneficiario.onSuccess(Beneficiarios.getItemFromJson(response.toString()));
                        }catch (JsonSyntaxException ex){
                            iBeneficiario.onError(new ErrorApi(ex));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        iBeneficiario.onError(new ErrorApi(error));
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
