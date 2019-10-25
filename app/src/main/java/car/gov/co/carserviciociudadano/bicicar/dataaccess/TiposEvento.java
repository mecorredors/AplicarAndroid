package car.gov.co.carserviciociudadano.bicicar.dataaccess;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
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
import car.gov.co.carserviciociudadano.bicicar.interfaces.IColegio;
import car.gov.co.carserviciociudadano.bicicar.interfaces.ITipoEvento;
import car.gov.co.carserviciociudadano.bicicar.model.Colegio;
import car.gov.co.carserviciociudadano.bicicar.model.TipoEvento;

import car.gov.co.carserviciociudadano.common.DbHelper;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class TiposEvento {
    public static final String TAG ="TiposTipoEvento";
    private DbHelper _dbHelper;

    private void InitDbHelper()
    {
        if(this._dbHelper == null)
            this._dbHelper = new DbHelper(AppCar.getContext());
    }

    private static String[] projectionDefault()
    {
        return new String[]{
                "[" + TipoEvento.ID_TIPO_EVENTO + "]",
                "[" + TipoEvento.NOMBRE + "]",
                "[" + TipoEvento.PUBLICO + "]",
                "[" + TipoEvento.RECORRIDO + "]" };

    }

    public boolean insert(TipoEvento element) {
        InitDbHelper();
        ContentValues cv = new ContentValues();

        cv.put(TipoEvento.ID_TIPO_EVENTO, element.IDTipoEvento);
        cv.put(TipoEvento.NOMBRE, element.Nombre);
        cv.put(TipoEvento.PUBLICO, element.Publico);
        cv.put(TipoEvento.RECORRIDO, element.Recorrido);

        long rowid = 0;
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        try {
            rowid=db.insertOrThrow(TipoEvento.TABLE_NAME, "", cv);
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

    public boolean update(TipoEvento element) {
        InitDbHelper();
        ContentValues cv = new ContentValues();

        String selection = TipoEvento.ID_TIPO_EVENTO + " = ? ";
        String[] selectionArgs = {String.valueOf(element.IDTipoEvento)};

        cv.put(TipoEvento.NOMBRE, element.Nombre);
        cv.put(TipoEvento.PUBLICO, element.Publico);
        cv.put(TipoEvento.RECORRIDO, element.Recorrido);


        long rowid = 0;
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        try {
            rowid=db.update(TipoEvento.TABLE_NAME, cv,selection, selectionArgs);

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
        String selection = TipoEvento.ID_TIPO_EVENTO + " = ? ";
        String[] selectionArgs = {String.valueOf(id)};
        int result = db.delete(TipoEvento.TABLE_NAME, selection, selectionArgs);
        return (result > 0);
    }

    public boolean deleteAll()
    {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        int result = db.delete(TipoEvento.TABLE_NAME, null, null);
        return (result > 0);

    }

    public List<TipoEvento> list()
    {   synchronized (this) {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        List<TipoEvento> lstTipoEventos = new ArrayList<>();

        try {

            // String[] selectionArgs =  {String.valueOf(estado)};
            String where = null;

            Cursor c = db.query(TipoEvento.TABLE_NAME, projectionDefault(), where, null, null, null, "[" + TipoEvento.ID_TIPO_EVENTO + "] ASC");

            if (c.moveToFirst()) {
                do {
                    lstTipoEventos.add(new TipoEvento(c));
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception ex) {
            Log.d(TAG + "List", ex.getMessage());
        }

        db.close();

        return lstTipoEventos;
    }
    }

    public TipoEvento read(int id)
    {
        synchronized (this) {
            InitDbHelper();
            SQLiteDatabase db = _dbHelper.getReadableDatabase();
            TipoEvento TipoEvento = null;
            try {
                String where = TipoEvento.ID_TIPO_EVENTO + "=?";
                String[] selectionArgs = {String.valueOf(id)};
                Cursor c = db.query(TipoEvento.TABLE_NAME, projectionDefault(), where, selectionArgs, null, null, "[" + TipoEvento.ID_TIPO_EVENTO + "] ASC");

                if (c.moveToFirst()) {
                    TipoEvento = new TipoEvento(c);
                }
                c.close();
            } catch (Exception ex) {
                Log.d(TAG + "Read", ex.getMessage());
            }

            db.close();

            return TipoEvento;
        }
    }

    public void list(  final ITipoEvento iTipoEvento)
    {
        String url = Config.API_BICICAR_CATALOGOS_OBTENER_TIPOS_EVENTOS;
        url = url.replace(" ", "%20");

        JsonArrayRequest objRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            iTipoEvento.onSuccess(JSONArrayToList(response));
                        } catch (JSONException ex) {
                            iTipoEvento.onError(new ErrorApi(ex));
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                iTipoEvento.onError(new ErrorApi(error));
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

    private List<TipoEvento> JSONArrayToList(JSONArray response) throws JSONException{
        List<TipoEvento> lista = new ArrayList<>();
        for(int i = 0; i < response.length(); i++){
            JSONObject jresponse = response.getJSONObject(i);
            lista.add( TiposEvento.getItemFromJson(jresponse.toString()));
        }
        return lista;
    }


    public static TipoEvento getItemFromJson(String json) throws JsonSyntaxException {
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Gson gson = builder.create();

        TipoEvento element = gson.fromJson(json, TipoEvento.class);
        return  element;
    }
}
