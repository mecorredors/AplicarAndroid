package car.gov.co.carserviciociudadano.petcar.dataaccess;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.common.APIClient;
import car.gov.co.carserviciociudadano.common.DbHelper;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.petcar.interfaces.ApiVisita;
import car.gov.co.carserviciociudadano.petcar.interfaces.IVisita;
import car.gov.co.carserviciociudadano.petcar.model.Visita;
import car.gov.co.carserviciociudadano.petcar.model.Visita;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Visitas {
    public static final String TAG ="Visitas";
    private DbHelper _dbHelper;

    private void InitDbHelper()
    {
        if(this._dbHelper == null)
            this._dbHelper = new DbHelper(AppCar.getContext());
    }

    private static String[] projectionDefault()
    {
        return new String[] {
                "[" + Visita.ID + "]",
                "[" + Visita.ID_VISITA + "]",
                "[" + Visita.IDCONTENEDOR + "]",             
                "[" + Visita.FECHALECTURA_QR + "]",                
                "[" + Visita.COMENTARIOS + "]",
                "[" + Visita.ESTADO + "]",
                "[" + Visita.ID_GESTOR + "]",
        };
    }

    public  boolean guardar(Visita element){
        if (read(element.Id) == null){
            return insert(element);
        }else{
            return update(element);
        }
    }

    public boolean insert(Visita element) {
        InitDbHelper();
        ContentValues cv = new ContentValues();

        cv.put(Visita.ID_VISITA, element.IDVisita);
        cv.put(Visita.IDCONTENEDOR, element.IDContenedor);    
        cv.put(Visita.FECHALECTURA_QR, Utils.toStringFromDate(element.FechaLecturaQR));
        cv.put(Visita.COMENTARIOS, element.Comentarios);    
        cv.put(Visita.ESTADO, element.Estado);
        cv.put(Visita.ID_GESTOR, element.IDGestor);
        
        long rowid = 0;

        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        try {
            rowid = db.insertOrThrow(Visita.TABLE_NAME, "", cv);
            element.Id = (int) rowid;
        }
        catch(Exception ex)
        {
            if(ex!=null){
                Log.e("MaterialReco.Insert", ex.toString());
                Crashlytics.logException(ex);
            }
            rowid = -1;
        }

        db.close();

        return (rowid > 0);
    }

    public boolean update(Visita element) {
        InitDbHelper();
        ContentValues cv = new ContentValues();

        String selection = Visita.ID + " = ? ";
        String[] selectionArgs = {String.valueOf(element.Id)};

        cv.put(Visita.ID_VISITA, element.IDVisita);
        cv.put(Visita.IDCONTENEDOR, element.IDContenedor);
        cv.put(Visita.FECHALECTURA_QR, Utils.toStringFromDate(element.FechaLecturaQR));
        cv.put(Visita.COMENTARIOS, element.Comentarios);
        cv.put(Visita.ESTADO, element.Estado);
        cv.put(Visita.ID_GESTOR, element.IDGestor);

        long rowid;

        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        try {
            rowid=db.update(Visita.TABLE_NAME, cv,selection, selectionArgs);

        }
        catch(Exception ex)
        {
            if(ex!=null){
                Log.e("MaterialReco.Insert", ex.toString());
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
        String selection = Visita.ID + " = ? ";
        String[] selectionArgs = {String.valueOf(id)};
        int result = db.delete(Visita.TABLE_NAME, selection, selectionArgs);
        return (result > 0);
    }

    public boolean deleteAll() {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        int result = db.delete(Visita.TABLE_NAME, null, null);
        return (result > 0);
    }

    public List<Visita> list(int estado){

        String where = Visita.ESTADO + " = " + estado;
        return list(where);
    }

    public List<Visita> list(){
        return list(null);
    }

    public List<Visita> list(String where)
    {   synchronized (this) {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        List<Visita> lstVisitas = new ArrayList<>();

        try {

            Cursor c = db.query(Visita.TABLE_NAME, projectionDefault(), where, null, null, null, "[" + Visita.ID + "] DESC");

            if (c.moveToFirst()) {
                do {
                    lstVisitas.add(new Visita(c));
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception ex) {
            Log.e("Visitas.List", ex.getMessage());
            Crashlytics.logException(ex);
        }

        db.close();

        return lstVisitas;
    }
    }

    public Visita read(int id)
    {
        synchronized (this) {
            InitDbHelper();
            SQLiteDatabase db = _dbHelper.getReadableDatabase();
            Visita Visita = null;
            try {
                String where = Visita.ID + "=?";
                String[] selectionArgs = {String.valueOf(id)};
                Cursor c = db.query(Visita.TABLE_NAME, projectionDefault(), where, selectionArgs, null, null, "[" + Visita.ID + "] DESC");

                if (c.moveToFirst()) {
                    Visita = new Visita(c);
                }
                c.close();
            } catch (Exception ex) {
                Log.e("Categories.List", ex.getMessage());
                Crashlytics.logException(ex);
            }

            db.close();

            return Visita;
        }
    }


    public void publicar(final Visita visita , final IVisita iVisita)
    {
        ApiVisita apiVisita = APIClient.getClient().create(ApiVisita.class);
        Call<Visita> call = apiVisita.agregar(visita);

        call.enqueue(new Callback<Visita>() {
            @Override
            public void onResponse(Call<Visita> call, Response<Visita> response) {
                if (response.code() == 200) {
                    Visita element = response.body();
                    visita.IDVisita = element.IDVisita;
                    iVisita.onSuccessPublicarVisita(visita);
                } else {
                    iVisita.onErrorPublicarVisita(new ErrorApi(response));
                }
            }

            @Override
            public void onFailure(Call<Visita> call, Throwable t) {
                call.cancel();
                iVisita.onErrorPublicarVisita(new ErrorApi(t));
                Log.d("item error", t.toString());
            }
        });
    }
}
