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
import car.gov.co.carserviciociudadano.petcar.interfaces.ApiTipoMaterial;
import car.gov.co.carserviciociudadano.petcar.interfaces.ITiposMaterial;
import car.gov.co.carserviciociudadano.petcar.model.TipoMaterial;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TiposMaterial {
    public static final String TAG ="TipoMaterial";
    private DbHelper _dbHelper;

    private void InitDbHelper()
    {
        if(this._dbHelper == null)
            this._dbHelper = new DbHelper(AppCar.getContext());
    }

    private static String[] projectionDefault()
    {
        return new String[] {
                "[" + TipoMaterial.IDTIPOMATERIAL + "]",
                "[" + TipoMaterial.NOMBRE + "]",
                "[" + TipoMaterial.DESCRIPCION + "]"
        };
    }

    public  boolean guardar(TipoMaterial element){
        if (read(element.IDTipoMaterial) == null){
            return insert(element);
        }else{
            return update(element);
        }
    }

    public boolean insert(TipoMaterial element) {
        InitDbHelper();
        ContentValues cv = new ContentValues();

        cv.put(TipoMaterial.IDTIPOMATERIAL, element.IDTipoMaterial);
        cv.put(TipoMaterial.NOMBRE, element.Nombre);
        cv.put(TipoMaterial.DESCRIPCION, element.Descripcion);

        long rowid = 0;

        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        try {
            rowid=db.insertOrThrow(TipoMaterial.TABLE_NAME, "", cv);
        }
        catch(Exception ex)
        {
            if(ex!=null){
                Log.d("TipoMaterials.Insert", ex.toString());
                Crashlytics.logException(ex);
            }
            rowid = -1;
        }

        db.close();

        return (rowid > 0);
    }

    public boolean update(TipoMaterial element) {
        InitDbHelper();
        ContentValues cv = new ContentValues();

        String selection = TipoMaterial.IDTIPOMATERIAL + " = ? ";
        String[] selectionArgs = {String.valueOf(element.IDTipoMaterial)};

        cv.put(TipoMaterial.NOMBRE, element.Nombre);
        cv.put(TipoMaterial.DESCRIPCION, element.Descripcion);

        long rowid;

        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        try {
            rowid=db.update(TipoMaterial.TABLE_NAME, cv,selection, selectionArgs);

        }
        catch(Exception ex)
        {
            if(ex!=null){
                Log.d("TipoMaterials.Insert", ex.toString());
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
        String selection = TipoMaterial.IDTIPOMATERIAL + " = ? ";
        String[] selectionArgs = {String.valueOf(id)};
        int result = db.delete(TipoMaterial.TABLE_NAME, selection, selectionArgs);
        return (result > 0);
    }

    public boolean deleteAll() {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        int result = db.delete(TipoMaterial.TABLE_NAME, null, null);
        return (result > 0);
    }

    public List<TipoMaterial> list(){
        return list(null);
    }

    public List<TipoMaterial> list(String where)
    {   synchronized (this) {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        List<TipoMaterial> lstTipoMaterials = new ArrayList<>();

        try {

            Cursor c = db.query(TipoMaterial.TABLE_NAME, projectionDefault(), where, null, null, null, "[" + TipoMaterial.IDTIPOMATERIAL + "]");

            if (c.moveToFirst()) {
                do {
                    lstTipoMaterials.add(new TipoMaterial(c));
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception ex) {
            Log.d("Categories.List", ex.getMessage());
            Crashlytics.logException(ex);
        }

        db.close();

        return lstTipoMaterials;
    }
    }

    public TipoMaterial read(int id)
    {
        synchronized (this) {
            InitDbHelper();
            SQLiteDatabase db = _dbHelper.getReadableDatabase();
            TipoMaterial TipoMaterial = null;
            try {
                String where = TipoMaterial.IDTIPOMATERIAL + "=?";
                String[] selectionArgs = {String.valueOf(id)};
                Cursor c = db.query(TipoMaterial.TABLE_NAME, projectionDefault(), where, selectionArgs, null, null, "[" + TipoMaterial.IDTIPOMATERIAL + "] DESC");

                if (c.moveToFirst()) {
                    TipoMaterial = new TipoMaterial(c);
                }
                c.close();
            } catch (Exception ex) {
                Log.d("Categories.List", ex.getMessage());
                Crashlytics.logException(ex);
            }

            db.close();

            return TipoMaterial;
        }
    }


    public  static void  getTiposMaterial(final ITiposMaterial iTiposMaterial){


        ApiTipoMaterial api = APIClient.getClient().create(ApiTipoMaterial.class);
        Call<List<TipoMaterial>> call = api.getTiposMaterial();

        call.enqueue(new Callback<List<TipoMaterial>>() {
            @Override
            public void onResponse(Call<List<TipoMaterial>> call, Response<List<TipoMaterial>> response) {

                if (response.code() == 200) {
                    iTiposMaterial.onSuccessTiposMaterial(response.body());
                }else{
                    iTiposMaterial.onErrorTiposMaterial(new ErrorApi(response));
                }

            }

            @Override
            public void onFailure(Call<List<TipoMaterial>> call, Throwable t) {
                call.cancel();
                iTiposMaterial.onErrorTiposMaterial(new ErrorApi(t));
                Log.d("item error", t.toString());
            }
        });

    }


}
