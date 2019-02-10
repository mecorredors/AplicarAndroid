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
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.bicicar.interfaces.IRuta;
import car.gov.co.carserviciociudadano.bicicar.model.Ruta;
import car.gov.co.carserviciociudadano.common.DbHelper;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class Rutas {
    public static final String TAG ="Rutas";

    private DbHelper _dbHelper;

    private void InitDbHelper()
    {
        if(this._dbHelper == null)
            this._dbHelper = new DbHelper(AppCar.getContext());
    }

    private static String[] projectionDefault()
    {
        return new String[] {
                "[" + Ruta.ID + "]",
                "[" + Ruta.ID_RUTA + "]",
                "[" + Ruta.ID_BENEFICIARIO + "]",
                "[" + Ruta.ID_NIVEL + "]",
                "[" + Ruta.NOMBRE + "]",
                "[" + Ruta.DESCRIPCION + "]",
                "[" + Ruta.DISTANCIA_KM + "]",
                "[" + Ruta.DURACION_MINUTOS + "]",
                "[" + Ruta.RUTA_TRAYECTO + "]",
                "[" + Ruta.ESTADO + "]"
        };
    }

    public boolean Insert(Ruta element) {
        InitDbHelper();
        ContentValues cv = new ContentValues();

        cv.put(Ruta.ID_BENEFICIARIO, element.IDBeneficiario);
        cv.put(Ruta.ID_NIVEL, element.IDNivel);
        cv.put(Ruta.NOMBRE, element.Nombre);
        cv.put(Ruta.DESCRIPCION, element.Descripcion);
        cv.put(Ruta.DISTANCIA_KM, element.DistanciaKM);
        cv.put(Ruta.DURACION_MINUTOS, element.DuracionMinutos);
        cv.put(Ruta.RUTA_TRAYECTO, element.RutaTrayecto);
        cv.put(Ruta.ESTADO, element.Estado);

        long rowid = 0;

        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        try {
            rowid=db.insertOrThrow(Ruta.TABLE_NAME, "", cv);
            element.Id = (int) rowid;
        }
        catch(Exception ex)
        {
            if(ex!=null){
                Log.d("Ruta.Insert", ex.toString());
                // Crashlytics.logException(ex);
            }
            rowid = -1;
        }

        db.close();

        return (rowid > 0);
    }

    public boolean Update(Ruta element) {
        InitDbHelper();
        ContentValues cv = new ContentValues();

        String selection = Ruta.ID + " = ? ";
        String[] selectionArgs = {String.valueOf(element.Id)};

        cv.put(Ruta.ID_BENEFICIARIO, element.IDBeneficiario);
        cv.put(Ruta.ID_NIVEL, element.IDNivel);
        cv.put(Ruta.NOMBRE, element.Nombre);
        cv.put(Ruta.DESCRIPCION, element.Descripcion);
        cv.put(Ruta.DISTANCIA_KM, element.DistanciaKM);
        cv.put(Ruta.DURACION_MINUTOS, element.DuracionMinutos);
        cv.put(Ruta.RUTA_TRAYECTO, element.RutaTrayecto);
        cv.put(Ruta.ESTADO, element.Estado);

        long rowid;

        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        try {
            rowid=db.update(Ruta.TABLE_NAME, cv,selection, selectionArgs);

        }
        catch(Exception ex)
        {
            if(ex!=null){
                Log.d("Ruta.Insert", ex.toString());
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
        String selection = Ruta.ID + " = ? ";
        String[] selectionArgs = {String.valueOf(id)};
        int result = db.delete(Ruta.TABLE_NAME, selection, selectionArgs);
        return (result > 0);
    }

    public boolean DeleteAll() {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        int result = db.delete(Ruta.TABLE_NAME, null, null);
        return (result > 0);
    }

    public List<Ruta> List(int estado, int idBeneficiario)
    {   synchronized (this) {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        List<Ruta> lstRutas = new ArrayList<>();


        try {

            // String[] selectionArgs =  {String.valueOf(estado)};
            String where;

            if (estado == Enumerator.Estado.TODOS) {
                where = Ruta.ID_BENEFICIARIO + "= " + idBeneficiario ;
            }else{
                where = Ruta.ESTADO + "="+ estado + " and " + Ruta.ID_BENEFICIARIO + "=" + idBeneficiario ;
            }

            Cursor c = db.query(Ruta.TABLE_NAME, projectionDefault(), where, null, null, null, "[" + Ruta.ID + "] DESC");

            if (c.moveToFirst()) {
                do {
                    lstRutas.add(new Ruta(c));
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception ex) {
            Log.d("Rutas.List", ex.getMessage());
        }

        db.close();

        return lstRutas;
    }
    }

    public Ruta Read(int id)
    {
        synchronized (this) {
            InitDbHelper();
            SQLiteDatabase db = _dbHelper.getReadableDatabase();
            Ruta ruta = null;
            try {
                String where = Ruta.ID + "=?";
                String[] selectionArgs = {String.valueOf(id)};
                Cursor c = db.query(Ruta.TABLE_NAME, projectionDefault(), where, selectionArgs, null, null, "[" + Ruta.ID + "] DESC");

                if (c.moveToFirst()) {
                    ruta = new Ruta(c);
                }
                c.close();
            } catch (Exception ex) {
                Log.d("Categories.List", ex.getMessage());
            }

            db.close();

            return ruta;
        }
    }

    public void publicar(final Ruta ruta, final IRuta iRuta )
    {
        String url = Config.API_BICICAR_RUTA;

        JsonObjectRequest objRequest = new JsonObjectRequest (
                Request.Method.POST, url,   ruta.toJSONObject() ,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Ruta rutaResponse =  getItemFromJson(response.toString());
                            rutaResponse.Id = ruta.Id;
                            iRuta.onSuccess(rutaResponse);
                        }catch (JsonSyntaxException ex){
                            iRuta.onError(new ErrorApi(ex));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        iRuta.onError(new ErrorApi(error));
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

    public void getRutas(int idBeneficiario, final IRuta iRuta)
    {
        String url = Config.API_BICICAR_OBTENER_RUTAS +"?idBeneficiario="+idBeneficiario;
        url = url.replace(" ", "%20");

        JsonArrayRequest objRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            iRuta.onSuccess(JSONArrayToList(response));
                        } catch (JSONException ex) {
                            iRuta.onError(new ErrorApi(ex));
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                iRuta.onError(new ErrorApi(error));
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

    private List<Ruta> JSONArrayToList(JSONArray response) throws JSONException{
        List<Ruta> lista = new ArrayList<>();
        for(int i = 0; i < response.length(); i++){
            JSONObject jresponse = response.getJSONObject(i);
            lista.add( Rutas.getItemFromJson(jresponse.toString()));
        }
        return lista;
    }

    public static Ruta getItemFromJson(String json) throws JsonSyntaxException {
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Gson gson = builder.create();

        Ruta element = gson.fromJson(json, Ruta.class);
        return  element;
    }
}
