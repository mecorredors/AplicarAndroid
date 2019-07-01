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
import com.android.volley.toolbox.JsonObjectRequest;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.Utils.Config;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.bicicar.interfaces.IAsistente;
import car.gov.co.carserviciociudadano.bicicar.model.Asistente;
import car.gov.co.carserviciociudadano.bicicar.model.Evento;
import car.gov.co.carserviciociudadano.common.DbHelper;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class Asistentes {
    public static final String TAG ="Asistentes";

    private DbHelper _dbHelper;

    private void InitDbHelper()
    {
        if(this._dbHelper == null)
            this._dbHelper = new DbHelper(AppCar.getContext());
    }

    private static String[] projectionDefault()
    {
        return new String[]{
                "[" + Asistente.ID_EVENTO + "]",
                "[" + Asistente.ID_BENEFICIARIO + "]",
                "[" + Asistente.ESTADO + "]" };

    }

    public boolean insert(Asistente element) {
        InitDbHelper();
        ContentValues cv = new ContentValues();

        cv.put(Asistente.ID_EVENTO, element.IDEvento);
        cv.put(Asistente.ID_BENEFICIARIO, element.IDBeneficiario);
        cv.put(Asistente.ESTADO, element.Estado);

        long rowid = 0;
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        try {
            rowid=db.insertOrThrow(Asistente.TABLE_NAME, "", cv);
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

    public boolean update(Asistente element) {
        InitDbHelper();
        ContentValues cv = new ContentValues();

        String selection = Asistente.ID_EVENTO + " = ? AND " + Asistente.ID_BENEFICIARIO + " = ? ";
        String[] selectionArgs = {String.valueOf(element.IDEvento), String.valueOf(element.IDBeneficiario)};

        cv.put(Asistente.ESTADO, element.Estado);

        long rowid = 0;
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        try {
            rowid=db.update(Asistente.TABLE_NAME, cv,selection, selectionArgs);

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

    public boolean delete(int idEvento, int idBeneficiario) {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        String where = Asistente.ID_EVENTO + " = " + idEvento + " and " + Asistente.ID_BENEFICIARIO + " = " + idBeneficiario;
        int result = db.delete(Asistente.TABLE_NAME, where, null);
        return (result > 0);
    }

    public boolean deleteAll() {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        int result = db.delete(Asistente.TABLE_NAME, null, null);
        return (result > 0);
    }

    public List<Asistente> list(int idEvento){
        return list(idEvento, -1);
    }

    public List<Asistente> list(int idEvento, int estado)
    {   synchronized (this) {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        List<Asistente> lstAsistentes = new ArrayList<>();

        try {

            String where = "";
            if (estado != -1) {
                where = Asistente.ID_EVENTO + " = " + idEvento + " and " + Asistente.ESTADO + " = " + estado;
            }else{
                where = Asistente.ID_EVENTO + " = " + idEvento;
            }
            Cursor c = db.query(Asistente.TABLE_NAME, projectionDefault(), where, null, null, null, "[" + Asistente.ID_BENEFICIARIO + "] DESC");

            if (c.moveToFirst()) {
                do {
                    lstAsistentes.add(new Asistente(c));
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception ex) {
            Log.d(TAG + "List", ex.getMessage());
        }

        db.close();

        return lstAsistentes;
    }
    }

    public Asistente read(int idEvento, int idBeneficiario)
    {
        synchronized (this) {
            InitDbHelper();
            SQLiteDatabase db = _dbHelper.getReadableDatabase();
            Asistente Asistente = null;
            try {
                String where = Asistente.ID_EVENTO + " = " + idEvento + " and " + Asistente.ID_BENEFICIARIO + " = " + idBeneficiario;
                Cursor c = db.query(Asistente.TABLE_NAME, projectionDefault(), where, null, null, null, "[" + Asistente.IDBeneficiario + "] DESC");

                if (c.moveToFirst()) {
                    Asistente = new Asistente(c);
                }
                c.close();
            } catch (Exception ex) {
                Log.d(TAG + "Read", ex.getMessage());
            }

            db.close();

            return Asistente;
        }
    }

    public void publicar(final Asistente Asistente, final IAsistente iAsistente )
    {
        String url = Config.API_BICICAR_EVENTO_PUBLICAR_ASISTENTE;

        JsonObjectRequest objRequest = new JsonObjectRequest (
                Request.Method.POST, url,   Asistente.toJSONObject() ,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Asistente asistenteResponse =  getItemFromJson(response.toString());

                            iAsistente.onSuccess(asistenteResponse);
                        }catch (JsonSyntaxException ex){
                            iAsistente.onError(new ErrorApi(ex));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        iAsistente.onError(new ErrorApi(error));
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

    public static Asistente getItemFromJson(String json) throws JsonSyntaxException {
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Gson gson = builder.create();

        Asistente element = gson.fromJson(json, Asistente.class);
        return  element;
    }
}
