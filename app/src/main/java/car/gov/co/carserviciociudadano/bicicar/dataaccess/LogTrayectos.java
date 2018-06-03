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
import car.gov.co.carserviciociudadano.bicicar.interfaces.ILogTrayecto;
import car.gov.co.carserviciociudadano.bicicar.model.LogTrayecto;
import car.gov.co.carserviciociudadano.common.DbHelper;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class LogTrayectos {
    private DbHelper _dbHelper;
    public static  final String TAG = "LogTrayectos";
    private void InitDbHelper()
    {
        if(this._dbHelper == null)
            this._dbHelper = new DbHelper(AppCar.getContext());
    }

    private static String[] projectionDefault()
    {
        return new String[] {
                "[" + LogTrayecto.ID + "]",
                "[" + LogTrayecto.SERIAL + "]",
                "[" + LogTrayecto.RIN + "]",
                "[" + LogTrayecto.DISTANCIA_KM + "]",
                "[" + LogTrayecto.DURACION_MINUTOS + "]",
                "[" + LogTrayecto.FECHA + "]",
                "[" + LogTrayecto.ESTADO + "]",
                "[" + LogTrayecto.NOMBRE + "]",
                "[" + LogTrayecto.ID_BENEFICIARIO + "]",
                "[" + LogTrayecto.ID_BENEFICIARIO_REGISTRO + "]",
                "[" + LogTrayecto.ID_BICICLETA + "]"
        };
    }
    public boolean Insert(LogTrayecto element) {
        InitDbHelper();
        ContentValues cv = new ContentValues();

        cv.put(LogTrayecto.SERIAL, element.Serial);
        cv.put(LogTrayecto.RIN, element.TamanioRin);
        cv.put(LogTrayecto.DISTANCIA_KM, element.DistanciaKM);
        cv.put(LogTrayecto.DURACION_MINUTOS, element.DuracionMinutos);
        cv.put(LogTrayecto.FECHA, Utils.toStringSQLLite(element.Fecha));
        cv.put(LogTrayecto.ESTADO, element.Estado);
        cv.put(LogTrayecto.NOMBRE, element.Nombre);
        cv.put(LogTrayecto.ID_BENEFICIARIO, element.IDBeneficiario);
        cv.put(LogTrayecto.ID_BENEFICIARIO_REGISTRO, element.IDBeneficiarioRegistro);
        cv.put(LogTrayecto.ID_BICICLETA, element.IDBicicleta);

        long rowid = 0;

        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        try {
            rowid=db.insertOrThrow(LogTrayecto.TABLE_NAME, "", cv);
            element.IDLogTrayecto = rowid;
        }
        catch(Exception ex)
        {
            if(ex!=null){
                Log.d("ClientsSQL.Insert", ex.toString());
               // Crashlytics.logException(ex);
            }
            rowid = -1;
        }

        db.close();

        return (rowid > 0);
    }

    public boolean Update(LogTrayecto element) {
        InitDbHelper();
        ContentValues cv = new ContentValues();

        String selection = LogTrayecto.ID + " = ? ";
        String[] selectionArgs = {String.valueOf(element.IDLogTrayecto)};

        cv.put(LogTrayecto.SERIAL, element.Serial);
        cv.put(LogTrayecto.RIN, element.TamanioRin);
        cv.put(LogTrayecto.DISTANCIA_KM, element.DistanciaKM);
        cv.put(LogTrayecto.DURACION_MINUTOS, element.DuracionMinutos);
        cv.put(LogTrayecto.FECHA, Utils.toStringSQLLite(element.Fecha));
        cv.put(LogTrayecto.ESTADO, element.Estado);
        cv.put(LogTrayecto.NOMBRE, element.Nombre);
        cv.put(LogTrayecto.ID_BENEFICIARIO, element.IDBeneficiario);
        cv.put(LogTrayecto.ID_BENEFICIARIO_REGISTRO, element.IDBeneficiarioRegistro);
        cv.put(LogTrayecto.ID_BICICLETA, element.IDBicicleta);

        long rowid = 0;

        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        try {
            rowid=db.update(LogTrayecto.TABLE_NAME, cv,selection, selectionArgs);

        }
        catch(Exception ex)
        {
            if(ex!=null){
                Log.d("ClientsSQL.Insert", ex.toString());
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
        String selection = LogTrayecto.ID + " = ? ";
        String[] selectionArgs = {String.valueOf(id)};
        int result = db.delete(LogTrayecto.TABLE_NAME, selection, selectionArgs);
        return (result > 0);
    }

    public List<LogTrayecto> List(int estado)
    {   synchronized (this) {
            InitDbHelper();
            SQLiteDatabase db = _dbHelper.getReadableDatabase();
            List<LogTrayecto> lstLogTrayectoes = new ArrayList<>();


            try {
                String where = LogTrayecto.ESTADO + "=?";
                String[] selectionArgs = {String.valueOf(estado)};
                Cursor c = db.query(LogTrayecto.TABLE_NAME, projectionDefault(), where, selectionArgs, null, null, "[" + LogTrayecto.ID + "] DESC");

                if (c.moveToFirst()) {
                    do {
                        lstLogTrayectoes.add(new LogTrayecto(c));
                    } while (c.moveToNext());
                }
                c.close();
            } catch (Exception ex) {
                Log.d("Categories.List", ex.getMessage());
            }

            db.close();

            return lstLogTrayectoes;
        }
    }

    public LogTrayecto Read(int id)
    {
        synchronized (this) {
            InitDbHelper();
            SQLiteDatabase db = _dbHelper.getReadableDatabase();
            LogTrayecto LogTrayecto = null;
            try {
                String where = LogTrayecto.ID + "=?";
                String[] selectionArgs = {String.valueOf(id)};
                Cursor c = db.query(LogTrayecto.TABLE_NAME, projectionDefault(), where, selectionArgs, null, null, "[" + LogTrayecto.ID + "] DESC");

                if (c.moveToFirst()) {
                    LogTrayecto = new LogTrayecto(c);
                }
                c.close();
            } catch (Exception ex) {
                Log.d("Categories.List", ex.getMessage());
            }

            db.close();

            return LogTrayecto;
        }
    }

    public void publicar(final LogTrayecto logTrayecto, final ILogTrayecto iLogTrayecto )
    {
        String url = Config.API_BICICAR_LOG_TRAYECTO;

        JsonObjectRequest objRequest = new JsonObjectRequest (
                Request.Method.POST, url,   logTrayecto.toJSONObject() ,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            LogTrayecto logTrayectoResponse =  getItemFromJson(response.toString());
                            logTrayectoResponse.IDLogTrayecto = logTrayecto.IDLogTrayecto;
                            iLogTrayecto.onSuccessLogTrayecto(logTrayectoResponse);
                        }catch (JsonSyntaxException ex){
                            iLogTrayecto.onErrorLogTrayecto(new ErrorApi(ex));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        iLogTrayecto.onErrorLogTrayecto(new ErrorApi(error));
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

    public static LogTrayecto getItemFromJson(String json) throws JsonSyntaxException {
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Gson gson = builder.create();

        LogTrayecto element = gson.fromJson(json, LogTrayecto.class);
        return  element;
    }
}
