package car.gov.co.carserviciociudadano.petcar.dataaccess;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.common.APIClient;
import car.gov.co.carserviciociudadano.common.DbHelper;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.petcar.interfaces.ApiGestor;
import car.gov.co.carserviciociudadano.petcar.interfaces.IGestor;
import car.gov.co.carserviciociudadano.petcar.model.Gestor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Gestores {
    public static final String TAG ="Gestores";
    private DbHelper _dbHelper;

    private void InitDbHelper()
    {
        if(this._dbHelper == null)
            this._dbHelper = new DbHelper(AppCar.getContext());
    }

    private static String[] projectionDefault()
    {
        return new String[] {
                "[" + Gestor.IDGESTOR + "]",
                "[" + Gestor.IDMUNICPIO + "]",
                "[" + Gestor.TIPO_PERSONA + "]",
                "[" + Gestor.TIPO_IDENTIFICACION + "]",
                "[" + Gestor.IDENTIFICACION + "]",
                "[" + Gestor.NOMBRE_COMPLETO + "]",
                "[" + Gestor.DIRECCION_CONTACTO + "]",
                "[" + Gestor.EMAIL + "]",
                "[" + Gestor.TELEFONO + "]"                
        };
    }

    public  boolean guardar(Gestor element){
        if (read(element.IDGestor) == null){
            return insert(element);
        }else{
            return update(element);
        }
    }

    public boolean insert(Gestor element) {
        InitDbHelper();
        ContentValues cv = new ContentValues();

        cv.put(Gestor.IDGESTOR, element.IDGestor);
        cv.put(Gestor.IDMUNICPIO, element.IDMunicipio);
        cv.put(Gestor.TIPO_PERSONA, element.TipoPersona);
        cv.put(Gestor.TIPO_IDENTIFICACION, element.TipoIdentificacion);
        cv.put(Gestor.IDENTIFICACION, element.Identificacion);
        cv.put(Gestor.NOMBRE_COMPLETO, element.NombreCompleto);
        cv.put(Gestor.DIRECCION_CONTACTO, element.DireccionContacto);
        cv.put(Gestor.EMAIL, element.Email);
        cv.put(Gestor.TELEFONO, element.Telefono);

        long rowid = 0;

        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        try {
            rowid=db.insertOrThrow(Gestor.TABLE_NAME, "", cv);
        }
        catch(Exception ex)
        {
            if(ex!=null){
                Log.d("Gestors.Insert", ex.toString());
                Crashlytics.logException(ex);
            }
            rowid = -1;
        }

        db.close();

        return (rowid > 0);
    }

    public boolean update(Gestor element) {
        InitDbHelper();
        ContentValues cv = new ContentValues();

        String selection = Gestor.IDGESTOR + " = ? ";
        String[] selectionArgs = {String.valueOf(element.IDGestor)};

        cv.put(Gestor.IDMUNICPIO, element.IDMunicipio);
        cv.put(Gestor.TIPO_PERSONA, element.TipoPersona);
        cv.put(Gestor.TIPO_IDENTIFICACION, element.TipoIdentificacion);
        cv.put(Gestor.IDENTIFICACION, element.Identificacion);
        cv.put(Gestor.NOMBRE_COMPLETO, element.NombreCompleto);
        cv.put(Gestor.DIRECCION_CONTACTO, element.DireccionContacto);
        cv.put(Gestor.EMAIL, element.Email);
        cv.put(Gestor.TELEFONO, element.Telefono);

        long rowid;

        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        try {
            rowid=db.update(Gestor.TABLE_NAME, cv,selection, selectionArgs);

        }
        catch(Exception ex)
        {
            if(ex!=null){
                Log.d("Gestors.Insert", ex.toString());
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
        String selection = Gestor.IDGESTOR + " = ? ";
        String[] selectionArgs = {String.valueOf(id)};
        int result = db.delete(Gestor.TABLE_NAME, selection, selectionArgs);
        return (result > 0);
    }

    public boolean deleteAll() {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        int result = db.delete(Gestor.TABLE_NAME, null, null);
        return (result > 0);
    }

    public List<Gestor> list(){
        return list(null);
    }

    public List<Gestor> list(String where)
    {   synchronized (this) {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        List<Gestor> lstGestors = new ArrayList<>();

        try {

            Cursor c = db.query(Gestor.TABLE_NAME, projectionDefault(), where, null, null, null, "[" + Gestor.IDGESTOR + "]");

            if (c.moveToFirst()) {
                do {
                    lstGestors.add(new Gestor(c));
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception ex) {
            Log.d("Categories.List", ex.getMessage());
            Crashlytics.logException(ex);
        }

        db.close();

        return lstGestors;
    }
    }

    public Gestor read(int id)
    {
        synchronized (this) {
            InitDbHelper();
            SQLiteDatabase db = _dbHelper.getReadableDatabase();
            Gestor Gestor = null;
            try {
                String where = Gestor.IDGESTOR + "=?";
                String[] selectionArgs = {String.valueOf(id)};
                Cursor c = db.query(Gestor.TABLE_NAME, projectionDefault(), where, selectionArgs, null, null, "[" + Gestor.IDGESTOR + "] DESC");

                if (c.moveToFirst()) {
                    Gestor = new Gestor(c);
                }
                c.close();
            } catch (Exception ex) {
                Log.d("Categories.List", ex.getMessage());
                Crashlytics.logException(ex);
            }

            db.close();

            return Gestor;
        }
    }

    public void login(String identificacion, String claveApp , final IGestor iGestor)
    {
        ApiGestor apiGestor = APIClient.getClient().create(ApiGestor.class);
        Call<Gestor> call = apiGestor.login(identificacion, claveApp);

        call.enqueue(new Callback<Gestor>() {
            @Override
            public void onResponse(Call<Gestor> call, Response<Gestor> response) {

                if (response.code() == 200) {

                    iGestor.onSuccessLoging(response.body());
                } else {
                    iGestor.onErrorLoging(new ErrorApi(response));
                }

            }

            @Override
            public void onFailure(Call<Gestor> call, Throwable t) {
                call.cancel();
                iGestor.onErrorLoging(new ErrorApi(t));
                Log.d("item error", t.toString());
            }
        });
    }


}
