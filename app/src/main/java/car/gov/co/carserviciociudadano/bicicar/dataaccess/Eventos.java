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
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.bicicar.interfaces.IEvento;
import car.gov.co.carserviciociudadano.bicicar.interfaces.ILogTrayecto;
import car.gov.co.carserviciociudadano.bicicar.model.Evento;
import car.gov.co.carserviciociudadano.bicicar.model.LogTrayecto;
import car.gov.co.carserviciociudadano.common.DbHelper;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class Eventos {
    public static final String TAG ="Eventos";
    private DbHelper _dbHelper;

    private void InitDbHelper()
    {
        if(this._dbHelper == null)
            this._dbHelper = new DbHelper(AppCar.getContext());
    }

    private static String[] projectionDefault()
    {
       /* return new String[]{
                "[" + Evento.ID_EVENTO + "]",
                "[" + Evento.ID_COLEGIO + "]",
                "[" + Evento.ID_TIPO_EVENTO + "]",
                "[" + Evento.ID_RESPONSABLE + "]",
                "[" + Evento.NOMBRE + "]",
                "[" + Evento.F_INICIO + "]",
                "[" + Evento.F_FIN + "]",
                "[" + Evento.DESCRIPCION + "]",
                "[" + Evento.PARTICIPANTES + "]",
                "[" + Evento.DISTANCIA_KM + "]",
                "[" + Evento.DURACION_MINUTOS + "]",
                "[" + Evento.ESTADO + "]" };*/


        return new String[]{

                "[" + Evento.ID_EVENTO + "]",
                "[" + Evento.ID_COLEGIO + "]",
                "[" + Evento.ID_TIPO_EVENTO + "]",
                "[" + Evento.ID_RESPONSABLE + "]",
                "[" + Evento.NOMBRE + "]",
                "[" + Evento.F_INICIO + "]",
                "[" + Evento.F_FIN + "]",
                "[" + Evento.DESCRIPCION + "]",
                "[" + Evento.PARTICIPANTES + "]",
                "[" + Evento.DISTANCIA_KM + "]",
                "[" + Evento.DURACION_MINUTOS + "]",
                "[" + Evento.HORA_INICIO + "]",
                "[" + Evento.HORA_FIN + "]",
                "[" + Evento.ID_MUNICIPIO + "]",
                "[" + Evento.ID_VEREDA + "]",
                "[" + Evento.PREDIO + "]",
                "[" + Evento.ID_CUENCA + "]",
                "[" + Evento.LATITUD + "]",
                "[" + Evento.LONGITUD + "]",
                "[" + Evento.NORTE + "]",
                "[" + Evento.ESTE + "]",
                "[" + Evento.ALTITUD + "]",
                "[" + Evento.ESTADO + "]" };

    }

    public boolean insert(Evento element) {
        InitDbHelper();
        ContentValues cv = new ContentValues();

        cv.put(Evento.ID_EVENTO, element.IDEvento);
        cv.put(Evento.ID_COLEGIO, element.IDColegio);
        cv.put(Evento.ID_TIPO_EVENTO, element.IDTipoEvento);
        cv.put(Evento.ID_RESPONSABLE, element.IDResponsable);
        cv.put(Evento.NOMBRE, element.Nombre);
        cv.put(Evento.F_INICIO, Utils.toStringSQLLite(element.FInicio));
        cv.put(Evento.F_FIN, Utils.toStringSQLLite(element.FFin));
        cv.put(Evento.DESCRIPCION, element.Descripcion);
        cv.put(Evento.ESTADO, element.Estado);
        cv.put(Evento.PARTICIPANTES, element.Participantes);
        cv.put(Evento.DISTANCIA_KM, element.DistanciaKm);
        cv.put(Evento.DURACION_MINUTOS, element.DuracionMinutos);
        cv.put(Evento.HORA_INICIO, element.HoraInicio);
        cv.put(Evento.HORA_FIN, element.HoraFin);
        cv.put(Evento.ID_MUNICIPIO, element.IDMunicipio);
        cv.put(Evento.ID_VEREDA, element.IDVereda);
        cv.put(Evento.PREDIO, element.Predio);
        cv.put(Evento.ID_CUENCA, element.IDCuenca);
        cv.put(Evento.LATITUD, element.Latitud);
        cv.put(Evento.LONGITUD, element.Longitud);
        cv.put(Evento.NORTE, element.Norte);
        cv.put(Evento.ESTE, element.Este);
        cv.put(Evento.ALTITUD, element.Altitud);

        long rowid = 0;
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        try {
            rowid=db.insertOrThrow(Evento.TABLE_NAME, "", cv);
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

    public boolean update(Evento element) {
        InitDbHelper();
        ContentValues cv = new ContentValues();

        String selection = Evento.ID_EVENTO + " = ? ";
        String[] selectionArgs = {String.valueOf(element.IDEvento)};

        cv.put(Evento.ID_EVENTO, element.IDEvento);
        cv.put(Evento.ID_COLEGIO, element.IDColegio);
        cv.put(Evento.ID_TIPO_EVENTO, element.IDTipoEvento);
        cv.put(Evento.ID_RESPONSABLE, element.IDResponsable);
        cv.put(Evento.NOMBRE, element.Nombre);
        cv.put(Evento.F_INICIO, Utils.toStringSQLLite(element.FInicio));
        cv.put(Evento.F_FIN, Utils.toStringSQLLite(element.FFin));
        cv.put(Evento.DESCRIPCION, element.Descripcion);
        cv.put(Evento.ESTADO, element.Estado);
        cv.put(Evento.PARTICIPANTES, element.Participantes);
        cv.put(Evento.DISTANCIA_KM, element.DistanciaKm);
        cv.put(Evento.DURACION_MINUTOS, element.DuracionMinutos);
        cv.put(Evento.HORA_INICIO, element.HoraInicio);
        cv.put(Evento.HORA_FIN, element.HoraFin);
        cv.put(Evento.ID_MUNICIPIO, element.IDMunicipio);
        cv.put(Evento.ID_VEREDA, element.IDVereda);
        cv.put(Evento.PREDIO, element.Predio);
        cv.put(Evento.ID_CUENCA, element.IDCuenca);
        cv.put(Evento.LATITUD, element.Latitud);
        cv.put(Evento.LONGITUD, element.Longitud);
        cv.put(Evento.NORTE, element.Norte);
        cv.put(Evento.ESTE, element.Este);
        cv.put(Evento.ALTITUD, element.Altitud);

        long rowid = 0;
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        try {
            rowid=db.update(Evento.TABLE_NAME, cv,selection, selectionArgs);

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
        String selection = Evento.ID_EVENTO + " = ? ";
        String[] selectionArgs = {String.valueOf(id)};
        int result = db.delete(Evento.TABLE_NAME, selection, selectionArgs);
        return (result > 0);
    }

    public boolean deleteAll() {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        int result = db.delete(Evento.TABLE_NAME, null, null);
        return (result > 0);
    }

    public List<Evento> list(int estado)
    {   synchronized (this) {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        List<Evento> lstEventos = new ArrayList<>();

        try {

            // String[] selectionArgs =  {String.valueOf(estado)};
            String where = null;
            if (estado != Enumerator.Estado.TODOS){
                where = Evento.ESTADO + " = " + estado;
            }


            Cursor c = db.query(Evento.TABLE_NAME, projectionDefault(), where, null, null, null, "[" + Evento.ID_EVENTO + "] DESC");

            if (c.moveToFirst()) {
                do {
                    lstEventos.add(new Evento(c));
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception ex) {
            Log.d(  TAG, ex.getMessage());
        }

        db.close();

        return lstEventos;
    }
    }

    public Evento read(int id)
    {
        synchronized (this) {
            InitDbHelper();
            SQLiteDatabase db = _dbHelper.getReadableDatabase();
            Evento Evento = null;
            try {
                String where = Evento.ID_EVENTO + "=?";
                String[] selectionArgs = {String.valueOf(id)};
                Cursor c = db.query(Evento.TABLE_NAME, projectionDefault(), where, selectionArgs, null, null, "[" + Evento.ID_EVENTO + "] DESC");

                if (c.moveToFirst()) {
                    Evento = new Evento(c);
                }
                c.close();
            } catch (Exception ex) {
                Log.d(TAG, ex.getMessage());
            }

            db.close();

            return Evento;
        }
    }

    public void publicar(final Evento evento, final IEvento iEvento )
    {
        String url = Config.API_BICICAR_EVENTO_PUBLICAR;

        JsonObjectRequest objRequest = new JsonObjectRequest (
                Request.Method.POST, url,   evento.toJSONObject() ,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Evento eventoResponse =  getItemFromJson(response.toString());
                            evento.IDEvento = eventoResponse.IDEvento;
                            iEvento.onSuccess(evento);
                        }catch (JsonSyntaxException ex){
                            iEvento.onError(new ErrorApi(ex));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        iEvento.onError(new ErrorApi(error));
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

    public void modificar(final Evento evento, final IEvento iEvento )
    {
        String url = Config.API_BICICAR_EVENTO_MODIFICAR;

        JsonObjectRequest objRequest = new JsonObjectRequest (
                Request.Method.POST, url,   evento.toJSONObject() ,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            iEvento.onSuccessModificar(evento);
                        }catch (JsonSyntaxException ex){
                            iEvento.onError(new ErrorApi(ex));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        iEvento.onError(new ErrorApi(error));
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

    public void eliminar(final Evento evento, final IEvento iEvento )
    {
        String url = Config.API_BICICAR_EVENTO_ELIMINAR;

        JsonObjectRequest objRequest = new JsonObjectRequest (
                Request.Method.POST, url,   evento.toJSONObject() ,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Evento eventoResponse =  getItemFromJson(response.toString());
                            iEvento.onSuccessEliminar(eventoResponse);
                        }catch (JsonSyntaxException ex){
                            iEvento.onError(new ErrorApi(ex));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        iEvento.onError(new ErrorApi(error));
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

    public static Evento getItemFromJson(String json) throws JsonSyntaxException {
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Gson gson = builder.create();

        Evento element = gson.fromJson(json, Evento.class);
        return  element;
    }
}
