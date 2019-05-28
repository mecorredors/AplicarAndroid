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
import com.crashlytics.android.Crashlytics;
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
import car.gov.co.carserviciociudadano.bicicar.interfaces.IBeneficiario;
import car.gov.co.carserviciociudadano.bicicar.interfaces.IColegio;
import car.gov.co.carserviciociudadano.bicicar.interfaces.IColegio;
import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.bicicar.model.Colegio;
import car.gov.co.carserviciociudadano.bicicar.model.Colegio;
import car.gov.co.carserviciociudadano.common.DbHelper;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class Colegios {
    public static final String TAG ="model.Colegios";
    private DbHelper _dbHelper;

    private void InitDbHelper()
    {
        if(this._dbHelper == null)
            this._dbHelper = new DbHelper(AppCar.getContext());
    }

    private static String[] projectionDefault()
    {
        return new String[]{
                "[" + Colegio.ID_COLEGIO + "]",
                "[" + Colegio.ID_MUNICIPIO + "]",
                "[" + Colegio.NOMBRE + "]",
                "[" + Colegio.ACTIVO + "]",
                "[" + Colegio.MUNICIPIO + "]",
                "[" + Colegio.ID_TIPO_INSTITUCION + "]",
                "[" + Colegio.TIPO_INSTITUCION + "]",
                "[" + Colegio.LATITUDE + "]",
                "[" + Colegio.LONGITUDE + "]",
                "[" + Colegio.NORTE + "]",
                "[" + Colegio.ESTE + "]",
                "[" + Colegio.ESTADO + "]"
                };

    }

    public boolean insert(Colegio element) {
        InitDbHelper();
        ContentValues cv = new ContentValues();

        cv.put(Colegio.ID_COLEGIO, element.IDColegio);
        cv.put(Colegio.ID_MUNICIPIO, element.IDMunicipio);
        cv.put(Colegio.NOMBRE, element.Nombre);
        cv.put(Colegio.ACTIVO, element.Activo);
        cv.put(Colegio.MUNICIPIO, element.Municipio);
        cv.put(Colegio.ID_TIPO_INSTITUCION, element.IDTipoInstitucion);
        cv.put(Colegio.TIPO_INSTITUCION, element.TipoInstitucion);
        cv.put(Colegio.LATITUDE, element.Latitude);
        cv.put(Colegio.LONGITUDE, element.Longitude);
        cv.put(Colegio.NORTE, element.Norte);
        cv.put(Colegio.ESTE, element.Este);
        cv.put(Colegio.ESTADO, element.Estado);

        long rowid = 0;
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        try {
            rowid=db.insertOrThrow(Colegio.TABLE_NAME, "", cv);
        }
        catch(Exception ex)
        {
            if(ex!=null){
                Log.d(TAG, ex.toString());

                //Crashlytics.logException(ex);
            }
            rowid = -1;
        }

        db.close();

        return (rowid > 0);
    }

    public boolean update(Colegio element) {
        InitDbHelper();
        ContentValues cv = new ContentValues();

        String selection = Colegio.ID_COLEGIO + " = ? ";
        String[] selectionArgs = {String.valueOf(element.IDColegio)};

        cv.put(Colegio.ID_MUNICIPIO, element.IDMunicipio);
        cv.put(Colegio.NOMBRE, element.Nombre);
        cv.put(Colegio.ACTIVO, element.Activo);
        cv.put(Colegio.MUNICIPIO, element.Municipio);
        cv.put(Colegio.ID_TIPO_INSTITUCION, element.IDTipoInstitucion);
        cv.put(Colegio.TIPO_INSTITUCION, element.TipoInstitucion);
        cv.put(Colegio.LATITUDE, element.Latitude);
        cv.put(Colegio.LONGITUDE, element.Longitude);
        cv.put(Colegio.NORTE, element.Norte);
        cv.put(Colegio.ESTE, element.Este);
        cv.put(Colegio.ESTADO, element.Estado);

        long rowid = 0;
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        try {
            rowid=db.update(Colegio.TABLE_NAME, cv,selection, selectionArgs);

        }
        catch(Exception ex)
        {
            if(ex!=null){
                Log.d(TAG, ex.toString());
                Crashlytics.logException(ex);
            }
            rowid = -1;
        }

        db.close();

        return (rowid > 0);

    }

    public boolean delete(long id) {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        String selection = Colegio.ID_COLEGIO + " = ? ";
        String[] selectionArgs = {String.valueOf(id)};
        int result = db.delete(Colegio.TABLE_NAME, selection, selectionArgs);
        return (result > 0);
    }

    public boolean deleteAll() {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        int result = db.delete(Colegio.TABLE_NAME, null, null);
        return (result > 0);
    }

    public List<Colegio> list(){
        return list("");
    }
    public List<Colegio> list(String where)
    {   synchronized (this) {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        List<Colegio> lstEventos = new ArrayList<>();

        try {

            Cursor c = db.query(Colegio.TABLE_NAME, projectionDefault(), where, null, null, null, "[" + Colegio.ID_COLEGIO + "] DESC");

            if (c.moveToFirst()) {
                do {
                    lstEventos.add(new Colegio(c));
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        }

        db.close();

        return lstEventos;
    }
    }

    public Colegio read(int id)
    {
        synchronized (this) {
            InitDbHelper();
            SQLiteDatabase db = _dbHelper.getReadableDatabase();
            Colegio Colegio = null;
            try {
                String where = Colegio.ID_COLEGIO + "=?";
                String[] selectionArgs = {String.valueOf(id)};
                Cursor c = db.query(Colegio.TABLE_NAME, projectionDefault(), where, selectionArgs, null, null, "[" + Colegio.ID_COLEGIO + "] DESC");

                if (c.moveToFirst()) {
                    Colegio = new Colegio(c);
                }
                c.close();
            } catch (Exception ex) {
                Log.d(  TAG, ex.getMessage());
            }

            db.close();

            return Colegio;
        }
    }


    public void list(  final IColegio iColegio)
    {
        String url = Config.API_BICICAR_CATALOGO_OBTENER_COLEGIOS;
        url = url.replace(" ", "%20");

        JsonArrayRequest objRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            iColegio.onSuccess(JSONArrayToList(response));
                        } catch (JSONException ex) {
                            iColegio.onError(new ErrorApi(ex));
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                iColegio.onError(new ErrorApi(error));
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


    public void actualizar(final Colegio colegio, final IColegio iColegio )
    {
        String url = Config.API_BICICAR_CATALOGO_ACTUALIZAR_COLEGIO;

        JsonObjectRequest objRequest = new JsonObjectRequest (
                Request.Method.POST, url,   colegio.toJSONObject() ,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            iColegio.onSuccess(Colegios.getItemFromJson(response.toString()));
                        }catch (JsonSyntaxException ex){
                            iColegio.onError(new ErrorApi(ex));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        iColegio.onError(new ErrorApi(error));
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

    private List<Colegio> JSONArrayToList(JSONArray response) throws JSONException{
        List<Colegio> lista = new ArrayList<>();
        for(int i = 0; i < response.length(); i++){
            JSONObject jresponse = response.getJSONObject(i);
            lista.add( Colegios.getItemFromJson(jresponse.toString()));
        }
        return lista;
    }


    public static Colegio getItemFromJson(String json) throws JsonSyntaxException {
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Gson gson = builder.create();

        Colegio element = gson.fromJson(json, Colegio.class);
        return  element;
    }
}
