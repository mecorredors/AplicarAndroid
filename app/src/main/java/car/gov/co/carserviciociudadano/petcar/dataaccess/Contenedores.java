package car.gov.co.carserviciociudadano.petcar.dataaccess;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.anupcowkur.reservoir.Reservoir;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.BuildConfig;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Reportes;
import car.gov.co.carserviciociudadano.bicicar.model.Estadistica;
import car.gov.co.carserviciociudadano.common.APIClient;
import car.gov.co.carserviciociudadano.common.DbHelper;
import car.gov.co.carserviciociudadano.parques.dataaccess.Parques;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.petcar.interfaces.ApiContenedor;
import car.gov.co.carserviciociudadano.petcar.interfaces.IContenedor;
import car.gov.co.carserviciociudadano.petcar.model.Contenedor;
import car.gov.co.carserviciociudadano.petcar.model.Contenedor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by apple on 9/09/18.
 */

public class Contenedores {
    public static final String TAG ="Contenedores";
    private DbHelper _dbHelper;

    private void InitDbHelper()
    {
        if(this._dbHelper == null)
            this._dbHelper = new DbHelper(AppCar.getContext());
    }

    private static String[] projectionDefault()
    {
        return new String[] {
                "[" + Contenedor.IDCONTENEDOR + "]",
                "[" + Contenedor.IDMUNICIPIO + "]",
                "[" + Contenedor.LATITUDE + "]",
                "[" + Contenedor.LONGITUDE + "]",
                "[" + Contenedor.ALTITUD + "]",
                "[" + Contenedor.FOTO_PRINCIPAL + "]",
                "[" + Contenedor.DIRECCION + "]",
                "[" + Contenedor.TOPE_MAXIMO_KG + "]",
                "[" + Contenedor.CODIGO + "]",
                "[" + Contenedor.MUNICIPIO + "]",

        };
    }

    public  boolean guardar(Contenedor element){
        if (read(element.IDContenedor) == null){
            return insert(element);
        }else{
            return update(element);
        }
    }

    public boolean insert(Contenedor element) {
        InitDbHelper();
        ContentValues cv = new ContentValues();

        cv.put(Contenedor.IDCONTENEDOR, element.IDContenedor);
        cv.put(Contenedor.IDMUNICIPIO, element.IDMunicipio);
        cv.put(Contenedor.LATITUDE, element.Latitude);
        cv.put(Contenedor.LONGITUDE, element.Longitude);
        cv.put(Contenedor.ALTITUD, element.Altitud);
        cv.put(Contenedor.DIRECCION, element.Direccion);
        cv.put(Contenedor.FOTO_PRINCIPAL, element.FotoPrincipal);
        cv.put(Contenedor.CODIGO, element.Codigo);
        cv.put(Contenedor.TOPE_MAXIMO_KG, element.TopeMaximoKG);
        cv.put(Contenedor.MUNICIPIO, element.Municipio);

        long rowid = 0;

        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        try {
            rowid=db.insertOrThrow(Contenedor.TABLE_NAME, "", cv);
        }
        catch(Exception ex)
        {
            if(ex!=null){
                Log.d("Contenedors.Insert", ex.toString());
                 Crashlytics.logException(ex);
            }
            rowid = -1;
        }

        db.close();

        return (rowid > 0);
    }

    public boolean update(Contenedor element) {
        InitDbHelper();
        ContentValues cv = new ContentValues();

        String selection = Contenedor.IDCONTENEDOR + " = ? ";
        String[] selectionArgs = {String.valueOf(element.IDContenedor)};

        cv.put(Contenedor.IDMUNICIPIO, element.IDMunicipio);
        cv.put(Contenedor.LATITUDE, element.Latitude);
        cv.put(Contenedor.LONGITUDE, element.Longitude);
        cv.put(Contenedor.ALTITUD, element.Altitud);
        cv.put(Contenedor.DIRECCION, element.Direccion);
        cv.put(Contenedor.FOTO_PRINCIPAL, element.FotoPrincipal);
        cv.put(Contenedor.CODIGO, element.Codigo);
        cv.put(Contenedor.TOPE_MAXIMO_KG, element.TopeMaximoKG);
        cv.put(Contenedor.MUNICIPIO, element.Municipio);

        long rowid;

        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        try {
            rowid=db.update(Contenedor.TABLE_NAME, cv,selection, selectionArgs);

        }
        catch(Exception ex)
        {
            if(ex!=null){
                Log.d("Contenedors.Insert", ex.toString());
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
        String selection = Contenedor.IDCONTENEDOR + " = ? ";
        String[] selectionArgs = {String.valueOf(id)};
        int result = db.delete(Contenedor.TABLE_NAME, selection, selectionArgs);
        return (result > 0);
    }

    public boolean deleteAll() {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        int result = db.delete(Contenedor.TABLE_NAME, null, null);
        return (result > 0);
    }

    public List<Contenedor> list(){
        return list(null);
    }

    public List<Contenedor> listByMunicipio(String idMunicipio){
        String where = Contenedor.IDMUNICIPIO + " = " + idMunicipio.trim();
        return list(where);
    }

    public List<Contenedor> list(String where)
    {   synchronized (this) {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        List<Contenedor> lstContenedors = new ArrayList<>();

        try {

            Cursor c = db.query(Contenedor.TABLE_NAME, projectionDefault(), where, null, null, null, "[" + Contenedor.IDCONTENEDOR + "]");

            if (c.moveToFirst()) {
                do {
                    lstContenedors.add(new Contenedor(c));
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception ex) {
            Log.d("Categories.List", ex.getMessage());
            Crashlytics.logException(ex);
        }

        db.close();

        return lstContenedors;
    }
    }

    public Contenedor read(int id)
    {
        synchronized (this) {
            InitDbHelper();
            SQLiteDatabase db = _dbHelper.getReadableDatabase();
            Contenedor Contenedor = null;
            try {
                String where = Contenedor.IDCONTENEDOR + "=?";
                String[] selectionArgs = {String.valueOf(id)};
                Cursor c = db.query(Contenedor.TABLE_NAME, projectionDefault(), where, selectionArgs, null, null, "[" + Contenedor.IDCONTENEDOR + "] DESC");

                if (c.moveToFirst()) {
                    Contenedor = new Contenedor(c);
                }
                c.close();
            } catch (Exception ex) {
                Log.e("Contenedores.List", ex.getMessage());
                Crashlytics.logException(ex);
            }

            db.close();

            return Contenedor;
        }
    }

    public Contenedor read(String codigo)
    {
        synchronized (this) {
           String where = Contenedor.CODIGO + " = " + codigo;
           List<Contenedor> listContenedores = list(where);
           if (listContenedores.size() > 0) {
               return  listContenedores.get(0);
            }
            return null;

        }
    }

    public  static void  getContenedores(String idMunicipio, final IContenedor iContenedor){

        ApiContenedor apiContenedor = APIClient.getClient().create(ApiContenedor.class);
        Call<List<Contenedor>> call = apiContenedor.getContenedores(idMunicipio);

        call.enqueue(new Callback<List<Contenedor>>() {
            @Override
            public void onResponse(Call<List<Contenedor>> call, Response<List<Contenedor>> response) {

                if (response.code() == 200) {

                    iContenedor.onSuccessContenedores(response.body());
                } else {
                    iContenedor.onErrorContenedores(new ErrorApi(response));
                }

            }

            @Override
            public void onFailure(Call<List<Contenedor>> call, Throwable t) {
                call.cancel();
                iContenedor.onErrorContenedores(new ErrorApi(t));
                Log.d("item error", t.toString());
            }
        });
    }

    public  static void  getContenedores(final IContenedor iContenedor){

       // iContenedor.onSuccess(getContenedoresFromJson());

        ApiContenedor apiContenedor = APIClient.getClient().create(ApiContenedor.class);
        Call<List<Contenedor>> call = apiContenedor.getContenedores();

        call.enqueue(new Callback<List<Contenedor>>() {
            @Override
            public void onResponse(Call<List<Contenedor>> call, Response<List<Contenedor>> response) {

                if (response.code() == 200) {
                    iContenedor.onSuccessContenedores(response.body());
                }else{
                    iContenedor.onErrorContenedores(new ErrorApi(response));
                }

            }

            @Override
            public void onFailure(Call<List<Contenedor>> call, Throwable t) {
                call.cancel();
                iContenedor.onErrorContenedores(new ErrorApi(t));
                Log.d("item error", t.toString());
            }
        });

    }

    private static List<Contenedor> getContenedoresFromJson() throws JsonSyntaxException {
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Gson gson = builder.create();
        //builder.setLenient();

        TypeToken<List<Contenedor>> token = new TypeToken<List<Contenedor>>() {};
        List<Contenedor> elements = gson.fromJson(getContenedoresJson(), token.getType());

       // Contenedor element = gson.fromJson(json, Contenedor.class);
        return  elements;
    }

    private static String getContenedoresJson() {

        String res = "[{'IDContenedor':10,'IDMunicipio':'25099','Latitude':4.7325639,'Longitude':-74.3340583,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'BOJACÁ'},{'IDContenedor':100,'IDMunicipio':'25260','Latitude':4.8601783,'Longitude':-74.2637467,'Altitud':0.0,'Direccion':'Coliseo Municipal pendiente de instalación ','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'EL ROSAL'},{'IDContenedor':101,'IDMunicipio':'25769','Latitude':4.9300548,'Longitude':-74.1715297,'Altitud':0.0,'Direccion':'Plaza de la Cultura','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'SUBACHOQUE'},{'IDContenedor':102,'IDMunicipio':'25758','Latitude':4.9040495,'Longitude':-73.9446131,'Altitud':0.0,'Direccion':'parque principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'SOPÓ'},{'IDContenedor':103,'IDMunicipio':'25175','Latitude':4.8569756,'Longitude':-74.0812493,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'CHÍA'},{'IDContenedor':104,'IDMunicipio':'25126','Latitude':4.9219658,'Longitude':-74.0239338,'Altitud':0.0,'Direccion':'Parque Principal\n','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'CAJICÁ'},{'IDContenedor':105,'IDMunicipio':'25126','Latitude':4.9414443,'Longitude':-74.022165,'Altitud':0.0,'Direccion':'parque cultivarte sector Capellania','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'CAJICÁ'},{'IDContenedor':106,'IDMunicipio':'25214','Latitude':0.0,'Longitude':0.0,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'COTA'},{'IDContenedor':107,'IDMunicipio':'25785','Latitude':4.9201228,'Longitude':-74.0983893,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'TABIO'},{'IDContenedor':108,'IDMunicipio':'25799','Latitude':4.8712241,'Longitude':-74.1451151,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'TENJO'},{'IDContenedor':109,'IDMunicipio':'25486','Latitude':5.0688052,'Longitude':-73.8796944,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'NEMOCÓN'},{'IDContenedor':11,'IDMunicipio':'25099','Latitude':4.732375,'Longitude':-74.3411111,'Altitud':0.0,'Direccion':'Institución Educativa Santa Helena','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'BOJACÁ'},{'IDContenedor':110,'IDMunicipio':'25817','Latitude':4.9691882,'Longitude':-73.9129512,'Altitud':0.0,'Direccion':'Parque principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'Tocancipá'},{'IDContenedor':111,'IDMunicipio':'25200','Latitude':5.1196457,'Longitude':-73.9164036,'Altitud':0.0,'Direccion':'Parque principal ','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'COGUA'},{'IDContenedor':12,'IDMunicipio':'25099','Latitude':4.7371667,'Longitude':-74.3449356,'Altitud':0.0,'Direccion':'Senderos del Zipa','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'BOJACÁ'},{'IDContenedor':13,'IDMunicipio':'25099','Latitude':4.7378088,'Longitude':-74.3448457,'Altitud':0.0,'Direccion':'Hogar Día Adulto Mayor - La Sagrada Familia','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'BOJACÁ'},{'IDContenedor':14,'IDMunicipio':'25875','Latitude':4.9695944,'Longitude':-74.4762389,'Altitud':0.0,'Direccion':'Barrio Alfonso López  Kra 8 No. 4 - 59\nFrente al Hospital','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'Villeta'},{'IDContenedor':15,'IDMunicipio':'25875','Latitude':5.011975,'Longitude':-74.466875,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'Villeta'},{'IDContenedor':16,'IDMunicipio':'25372','Latitude':0.0,'Longitude':0.0,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'JUNÍN'},{'IDContenedor':17,'IDMunicipio':'25372','Latitude':4.8870603,'Longitude':-74.3023502,'Altitud':0.0,'Direccion':'Urbanización Villa Amalia','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'JUNÍN'},{'IDContenedor':18,'IDMunicipio':'25653','Latitude':0.0,'Longitude':0.0,'Altitud':0.0,'Direccion':'Centro Poblado La Montaña','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'SAN CAYETANO'},{'IDContenedor':19,'IDMunicipio':'25653','Latitude':5.3333212,'Longitude':-74.0237642,'Altitud':0.0,'Direccion':'Casco Urbano','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'SAN CAYETANO'},{'IDContenedor':20,'IDMunicipio':'25653','Latitude':0.0,'Longitude':0.0,'Altitud':0.0,'Direccion':'Vereda Camancha','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'SAN CAYETANO'},{'IDContenedor':21,'IDMunicipio':'25317','Latitude':5.3849682,'Longitude':-73.6856998,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'GUACHETÁ'},{'IDContenedor':22,'IDMunicipio':'25317','Latitude':0.0,'Longitude':0.0,'Altitud':0.0,'Direccion':'Plaza de Mercado','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'GUACHETÁ'},{'IDContenedor':23,'IDMunicipio':'25745','Latitude':5.5026814,'Longitude':-73.852321,'Altitud':0.0,'Direccion':'IED Agustin Parra Sede Primaria','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'SIMIJACA'},{'IDContenedor':24,'IDMunicipio':'25745','Latitude':5.5024824,'Longitude':-73.8520323,'Altitud':0.0,'Direccion':'IED Agustin Parra Sede Secundaria','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'SIMIJACA'},{'IDContenedor':25,'IDMunicipio':'25183','Latitude':5.1457143,'Longitude':-73.6831168,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'Chocontá'},{'IDContenedor':26,'IDMunicipio':'25183','Latitude':5.1476821,'Longitude':-73.6774877,'Altitud':0.0,'Direccion':'Polideportivo - IED','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'Chocontá'},{'IDContenedor':27,'IDMunicipio':'25086','Latitude':4.7996812,'Longitude':-74.7417666,'Altitud':0.0,'Direccion':'Casco Urbano','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'BELTRÁN'},{'IDContenedor':28,'IDMunicipio':'25086','Latitude':4.6624628,'Longitude':-74.7868677,'Altitud':0.0,'Direccion':'Vereda Paquiló ','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'BELTRÁN'},{'IDContenedor':29,'IDMunicipio':'25086','Latitude':4.7769291,'Longitude':-74.7615025,'Altitud':0.0,'Direccion':'Vereda Gramalotal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'BELTRÁN'},{'IDContenedor':30,'IDMunicipio':'25324','Latitude':4.5167496,'Longitude':-74.7905262,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'GUATAQUÍ'},{'IDContenedor':31,'IDMunicipio':'25488','Latitude':4.3466641,'Longitude':-74.5441454,'Altitud':0.0,'Direccion':'Parque Centro Poblado Pueblo Nuevo','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'NILO'},{'IDContenedor':32,'IDMunicipio':'25488','Latitude':4.3061837,'Longitude':-74.6189956,'Altitud':0.0,'Direccion':'Coliseo Municipal parte Externa','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'NILO'},{'IDContenedor':33,'IDMunicipio':'25488','Latitude':4.2235737,'Longitude':-74.6968753,'Altitud':0.0,'Direccion':'Colegio La Esmeralda Parte Externa','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'NILO'},{'IDContenedor':34,'IDMunicipio':'25488','Latitude':4.306843,'Longitude':-74.6202212,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'NILO'},{'IDContenedor':35,'IDMunicipio':'25815','Latitude':4.4577472,'Longitude':-74.6341241,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'TOCAIMA'},{'IDContenedor':36,'IDMunicipio':'25815','Latitude':4.4583741,'Longitude':-74.6302143,'Altitud':0.0,'Direccion':'Parque Kenedy','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'TOCAIMA'},{'IDContenedor':37,'IDMunicipio':'25053','Latitude':4.2719925,'Longitude':-74.4148561,'Altitud':0.0,'Direccion':'Colegio Jhon F Kenedy','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'ARBELÁEZ'},{'IDContenedor':38,'IDMunicipio':'25120','Latitude':3.985522,'Longitude':-74.4839265,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'CABRERA'},{'IDContenedor':39,'IDMunicipio':'25269','Latitude':4.5324678,'Longitude':-74.295514,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'Facatativá'},{'IDContenedor':40,'IDMunicipio':'25524','Latitude':0.0,'Longitude':0.0,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'PANDI'},{'IDContenedor':41,'IDMunicipio':'25535','Latitude':4.3087494,'Longitude':-74.3003492,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'PASCA'},{'IDContenedor':42,'IDMunicipio':'25649','Latitude':4.1795602,'Longitude':-74.4223368,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'SAN BERNARDO'},{'IDContenedor':43,'IDMunicipio':'25743','Latitude':4.468537,'Longitude':-74.3859491,'Altitud':0.0,'Direccion':'Concha Acustica','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'SILVANIA'},{'IDContenedor':44,'IDMunicipio':'25805','Latitude':0.0,'Longitude':0.0,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'TIBACUY'},{'IDContenedor':45,'IDMunicipio':'25506','Latitude':4.0891579,'Longitude':-74.4781063,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'VENECIA'},{'IDContenedor':46,'IDMunicipio':'25019','Latitude':4.8773527,'Longitude':-74.4374617,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'ALBÁN'},{'IDContenedor':47,'IDMunicipio':'25328','Latitude':4.8788827,'Longitude':-74.4666994,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'GUAYABAL DE SIQUIMA'},{'IDContenedor':48,'IDMunicipio':'25095','Latitude':4.8721993,'Longitude':-74.5393226,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'BITUIMA'},{'IDContenedor':49,'IDMunicipio':'25867','Latitude':4.8744168,'Longitude':-74.5621867,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'Vianí'},{'IDContenedor':50,'IDMunicipio':'25168','Latitude':0.0,'Longitude':0.0,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'CHAGUANÍ'},{'IDContenedor':51,'IDMunicipio':'25168','Latitude':4.9864569,'Longitude':-74.5848368,'Altitud':0.0,'Direccion':'Parque Saman','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'CHAGUANÍ'},{'IDContenedor':52,'IDMunicipio':'25662','Latitude':4.8457416,'Longitude':-74.6225113,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'SAN JUAN DE RIOSECO'},{'IDContenedor':53,'IDMunicipio':'25580','Latitude':0.0,'Longitude':0.0,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'PULÍ'},{'IDContenedor':54,'IDMunicipio':'13268','Latitude':5.2483193,'Longitude':-74.2922998,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'EL PEÑÓN'},{'IDContenedor':55,'IDMunicipio':'25181','Latitude':5.3603452,'Longitude':-74.3903833,'Altitud':0.0,'Direccion':'Pendiente Instalación en el sitio final','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'CHOACHÍ'},{'IDContenedor':56,'IDMunicipio':'25518','Latitude':5.3802627,'Longitude':-74.1964352,'Altitud':0.0,'Direccion':'Frente Alcaldía Municipal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'PAIME'},{'IDContenedor':57,'IDMunicipio':'25823','Latitude':0.0,'Longitude':0.0,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'TOPAIPÍ'},{'IDContenedor':58,'IDMunicipio':'25871','Latitude':5.2741718,'Longitude':-74.1951339,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'VILLA GÓMEZ'},{'IDContenedor':59,'IDMunicipio':'25513','Latitude':5.1386517,'Longitude':-74.1537018,'Altitud':0.0,'Direccion':'Pendiente Instalación en el sitio final','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'Pacho'},{'IDContenedor':60,'IDMunicipio':'25513','Latitude':5.1386517,'Longitude':-74.1537018,'Altitud':0.0,'Direccion':'Pendiente Instalación en el sitio final','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'Pacho'},{'IDContenedor':61,'IDMunicipio':'25885','Latitude':5.4573891,'Longitude':-74.3397833,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'YACOPÍ'},{'IDContenedor':62,'IDMunicipio':'25772','Latitude':5.1028941,'Longitude':-73.7987963,'Altitud':0.0,'Direccion':'Frente Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'SUESCA'},{'IDContenedor':63,'IDMunicipio':'25436','Latitude':5.0086243,'Longitude':-73.5410461,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'MANTA'},{'IDContenedor':64,'IDMunicipio':'25807','Latitude':5.0528845,'Longitude':-73.5053996,'Altitud':0.0,'Direccion':'Pendiente Instalación en el sitio final','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'TIBIRITA'},{'IDContenedor':65,'IDMunicipio':'25793','Latitude':0.0,'Longitude':0.0,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'TAUSA'},{'IDContenedor':66,'IDMunicipio':'25781','Latitude':5.2411544,'Longitude':-73.8542121,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'SUTATAUSA'},{'IDContenedor':67,'IDMunicipio':'25407','Latitude':5.319556,'Longitude':-73.6236645,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'LENGUAZAQUE'},{'IDContenedor':68,'IDMunicipio':'25224','Latitude':5.2501418,'Longitude':-73.7668349,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'CUCUNUBÁ'},{'IDContenedor':69,'IDMunicipio':'25154','Latitude':5.3485989,'Longitude':-73.9014224,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'CARMEN DE CARUPA'},{'IDContenedor':70,'IDMunicipio':'25279','Latitude':5.4519187,'Longitude':-73.8162997,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'FOMEQUE'},{'IDContenedor':71,'IDMunicipio':'15632','Latitude':5.6958809,'Longitude':-73.7584351,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'SABOYÁ'},{'IDContenedor':72,'IDMunicipio':'25754','Latitude':4.5820791,'Longitude':-74.2190646,'Altitud':0.0,'Direccion':'Pendiente Instalación en el sitio final','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'Soacha'},{'IDContenedor':73,'IDMunicipio':'25740','Latitude':4.489288,'Longitude':-74.2593688,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'SIBATÉ'},{'IDContenedor':74,'IDMunicipio':'25645','Latitude':4.6158342,'Longitude':-74.3520181,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'SAN ANTONIO DEL TEQUENDAMA'},{'IDContenedor':75,'IDMunicipio':'25645','Latitude':0.0,'Longitude':0.0,'Altitud':0.0,'Direccion':'Parque Principal Santandercito','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'SAN ANTONIO DEL TEQUENDAMA'},{'IDContenedor':76,'IDMunicipio':'25245','Latitude':4.5843495,'Longitude':-74.4456912,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'EL COLEGIO'},{'IDContenedor':77,'IDMunicipio':'25878','Latitude':4.4390333,'Longitude':-74.5207505,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'VIOTÁ'},{'IDContenedor':78,'IDMunicipio':'25599','Latitude':4.5191043,'Longitude':-74.5944248,'Altitud':0.0,'Direccion':'Frente Alcaldía','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'APULO'},{'IDContenedor':79,'IDMunicipio':'25040','Latitude':4.7695617,'Longitude':-74.4701977,'Altitud':0.0,'Direccion':'Frente Alcaldía','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'ANOLAIMA'},{'IDContenedor':80,'IDMunicipio':'25596','Latitude':4.745966,'Longitude':-74.5337122,'Altitud':0.0,'Direccion':'Frente al Coliseo','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'QUIPILE'},{'IDContenedor':81,'IDMunicipio':'25596','Latitude':4.6520066,'Longitude':-74.5676367,'Altitud':0.0,'Direccion':'Inspección La Virgen\nParque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'QUIPILE'},{'IDContenedor':82,'IDMunicipio':'25293','Latitude':4.6640828,'Longitude':-74.4053017,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'GACHALA'},{'IDContenedor':83,'IDMunicipio':'25001','Latitude':4.3774207,'Longitude':-74.6693697,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'AGUA DE DIOS'},{'IDContenedor':84,'IDMunicipio':'25612','Latitude':4.2686012,'Longitude':-74.7764193,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'RICAURTE'},{'IDContenedor':85,'IDMunicipio':'25377','Latitude':0.0,'Longitude':0.0,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'LA CALERA'},{'IDContenedor':86,'IDMunicipio':'25491','Latitude':5.0753684,'Longitude':-74.3746678,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'NOCAIMA'},{'IDContenedor':87,'IDMunicipio':'25489','Latitude':5.1259763,'Longitude':-74.385162,'Altitud':0.0,'Direccion':'Pendiente Instalación en el sitio final','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'NIMAIMA'},{'IDContenedor':88,'IDMunicipio':'25862','Latitude':5.1156564,'Longitude':-74.3497405,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'VERGARA'},{'IDContenedor':89,'IDMunicipio':'25718','Latitude':0.0,'Longitude':0.0,'Altitud':0.0,'Direccion':'Parque principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'SASAIMA'},{'IDContenedor':90,'IDMunicipio':'25592','Latitude':5.1160408,'Longitude':-74.4786448,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'QUEBRADANEGRA'},{'IDContenedor':91,'IDMunicipio':'25592','Latitude':5.0800811,'Longitude':-74.4877939,'Altitud':0.0,'Direccion':'Inspección La Magdalena','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'QUEBRADANEGRA'},{'IDContenedor':92,'IDMunicipio':'25398','Latitude':0.0,'Longitude':0.0,'Altitud':0.0,'Direccion':'Pendiente Instalación en el sitio final','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'LA PEÑA'},{'IDContenedor':93,'IDMunicipio':'25320','Latitude':5.0751331,'Longitude':-74.6024273,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'Guaduas'},{'IDContenedor':94,'IDMunicipio':'25572','Latitude':5.4641119,'Longitude':-74.6566962,'Altitud':0.0,'Direccion':'Coliseo Municipal parte Externa','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'PUERTO SALGAR'},{'IDContenedor':95,'IDMunicipio':'25473','Latitude':4.7201559,'Longitude':-74.2280966,'Altitud':0.0,'Direccion':'IED Roberto Velandia','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'Mosquera'},{'IDContenedor':96,'IDMunicipio':'25286','Latitude':4.7322751,'Longitude':-74.2210505,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'FUNZA'},{'IDContenedor':97,'IDMunicipio':'25430','Latitude':4.7332047,'Longitude':-74.2660469,'Altitud':0.0,'Direccion':'Pendiente Instalación en el sitio final','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'MADRID'},{'IDContenedor':98,'IDMunicipio':'25898','Latitude':4.7594973,'Longitude':-74.3802066,'Altitud':0.0,'Direccion':'Parque Principal','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'ZIPACÓN'},{'IDContenedor':99,'IDMunicipio':'25269','Latitude':4.8087309,'Longitude':-74.3495787,'Altitud':0.0,'Direccion':'Frente a secretaria de educacion','FechaInstalacion':'2018-09-08T17:38:35.617','FotoPrincipal':null,'UsuarioCreacion':'system','FechaCreacion':'2018-09-08T17:38:35.617','UsuarioModificacion':'system','FechaModificacion':'2018-09-08T17:38:35.617','Icono':'NONE','Municipio':'Facatativá'}]";
        return  res;

    }

}
