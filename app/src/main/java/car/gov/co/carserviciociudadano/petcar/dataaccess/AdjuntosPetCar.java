package car.gov.co.carserviciociudadano.petcar.dataaccess;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.common.DbHelper;
import car.gov.co.carserviciociudadano.petcar.model.AdjuntoPetCar;

public class AdjuntosPetCar {
    public static final String TAG ="AdjuntosPetCar";
    private DbHelper _dbHelper;

    private void InitDbHelper()
    {
        if(this._dbHelper == null)
            this._dbHelper = new DbHelper(AppCar.getContext());
    }

    private static String[] projectionDefault()
    {
        return new String[] {
                "[" + AdjuntoPetCar.ID + "]",
                "[" + AdjuntoPetCar.IDADJUNTO + "]",
                "[" + AdjuntoPetCar.PATH + "]",
                "[" + AdjuntoPetCar.IDMATERIAL_RECOGIDO + "]",
                "[" + AdjuntoPetCar.ESTADO + "]"

        };
    }

    public  boolean guardar(AdjuntoPetCar element){
        if (read(element.Id) == null){
            return insert(element);
        }else{
            return update(element);
        }
    }

    public boolean insert(AdjuntoPetCar element) {
        InitDbHelper();
        ContentValues cv = new ContentValues();

        cv.put(AdjuntoPetCar.ID, element.Id);
        cv.put(AdjuntoPetCar.IDADJUNTO, element.IDAdjunto);
        cv.put(AdjuntoPetCar.PATH, element.Path);
        cv.put(AdjuntoPetCar.IDMATERIAL_RECOGIDO, element.IDTipoMaterialRecogido);
        cv.put(AdjuntoPetCar.ESTADO, element.Estado);


        long rowid = 0;

        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        try {
            rowid=db.insertOrThrow(AdjuntoPetCar.TABLE_NAME, "", cv);
        }
        catch(Exception ex)
        {
            if(ex!=null){
                Log.d("AdjuntoPetCars.Insert", ex.toString());
                Crashlytics.logException(ex);
            }
            rowid = -1;
        }

        db.close();

        return (rowid > 0);
    }

    public boolean update(AdjuntoPetCar element) {
        InitDbHelper();
        ContentValues cv = new ContentValues();

        String selection = AdjuntoPetCar.ID + " = ? ";
        String[] selectionArgs = {String.valueOf(element.Id)};

        cv.put(AdjuntoPetCar.IDADJUNTO, element.IDAdjunto);
        cv.put(AdjuntoPetCar.PATH, element.Path);
        cv.put(AdjuntoPetCar.IDMATERIAL_RECOGIDO, element.IDTipoMaterialRecogido);
        cv.put(AdjuntoPetCar.ESTADO, element.Estado);

        long rowid;

        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        try {
            rowid=db.update(AdjuntoPetCar.TABLE_NAME, cv,selection, selectionArgs);

        }
        catch(Exception ex)
        {
            if(ex!=null){
                Log.d("AdjuntoPetCars.Insert", ex.toString());
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
        String selection = AdjuntoPetCar.ID + " = ? ";
        String[] selectionArgs = {String.valueOf(id)};
        int result = db.delete(AdjuntoPetCar.TABLE_NAME, selection, selectionArgs);
        return (result > 0);
    }

    public boolean deleteAll() {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        int result = db.delete(AdjuntoPetCar.TABLE_NAME, null, null);
        return (result > 0);
    }

    public List<AdjuntoPetCar> list(){
        return list(null);
    }

    public List<AdjuntoPetCar> list(String where)
    {   synchronized (this) {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        List<AdjuntoPetCar> lstAdjuntoPetCars = new ArrayList<>();

        try {

            Cursor c = db.query(AdjuntoPetCar.TABLE_NAME, projectionDefault(), where, null, null, null, "[" + AdjuntoPetCar.ID + "]");

            if (c.moveToFirst()) {
                do {
                    lstAdjuntoPetCars.add(new AdjuntoPetCar(c));
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception ex) {
            Log.d("Categories.List", ex.getMessage());
            Crashlytics.logException(ex);
        }

        db.close();

        return lstAdjuntoPetCars;
    }
    }

    public AdjuntoPetCar read(int id)
    {
        synchronized (this) {
            InitDbHelper();
            SQLiteDatabase db = _dbHelper.getReadableDatabase();
            AdjuntoPetCar AdjuntoPetCar = null;
            try {
                String where = AdjuntoPetCar.ID + "=?";
                String[] selectionArgs = {String.valueOf(id)};
                Cursor c = db.query(AdjuntoPetCar.TABLE_NAME, projectionDefault(), where, selectionArgs, null, null, "[" + AdjuntoPetCar.ID + "] DESC");

                if (c.moveToFirst()) {
                    AdjuntoPetCar = new AdjuntoPetCar(c);
                }
                c.close();
            } catch (Exception ex) {
                Log.d("Adjuntos.List", ex.getMessage());
                Crashlytics.logException(ex);
            }

            db.close();

            return AdjuntoPetCar;
        }
    }


}
