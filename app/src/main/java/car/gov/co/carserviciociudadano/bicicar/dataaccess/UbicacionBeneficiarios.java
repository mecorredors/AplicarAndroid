package car.gov.co.carserviciociudadano.bicicar.dataaccess;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.bicicar.model.UbicacionBeneficiario;
import car.gov.co.carserviciociudadano.common.DbHelper;

public class UbicacionBeneficiarios {
    public static final String TAG ="UbicacionBeneficiarios";
    private DbHelper _dbHelper;

    private void InitDbHelper()
    {
        if(this._dbHelper == null)
            this._dbHelper = new DbHelper(AppCar.getContext());
    }

    private static String[] projectionDefault()
    {
        return new String[]{
                "[" + UbicacionBeneficiario.ID_BENEFICIARIO + "]",
                "[" + UbicacionBeneficiario.LATITUDE + "]",
                "[" + UbicacionBeneficiario.LONGITUDE + "]",
                "[" + UbicacionBeneficiario.ESTADO + "]" };

    }

    public boolean Insert(UbicacionBeneficiario element) {
        InitDbHelper();
        ContentValues cv = new ContentValues();

        cv.put(UbicacionBeneficiario.ID_BENEFICIARIO, element.IDBeneficiario);
        cv.put(UbicacionBeneficiario.LATITUDE, element.Latitude);
        cv.put(UbicacionBeneficiario.LONGITUDE, element.Longitude);
        cv.put(UbicacionBeneficiario.ESTADO, element.Estado);

        long rowid = 0;
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        try {
            rowid=db.insertOrThrow(UbicacionBeneficiario.TABLE_NAME, "", cv);
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

    public boolean Update(UbicacionBeneficiario element) {
        InitDbHelper();
        ContentValues cv = new ContentValues();

        String selection = UbicacionBeneficiario.ID_BENEFICIARIO + " = ? ";
        String[] selectionArgs = {String.valueOf(element.IDBeneficiario)};

        cv.put(UbicacionBeneficiario.ID_BENEFICIARIO, element.IDBeneficiario);
        cv.put(UbicacionBeneficiario.LATITUDE, element.Latitude);
        cv.put(UbicacionBeneficiario.LONGITUDE, element.Longitude);
        cv.put(UbicacionBeneficiario.ESTADO, element.Estado);


        long rowid = 0;
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        try {
            rowid=db.update(UbicacionBeneficiario.TABLE_NAME, cv,selection, selectionArgs);

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

    public boolean Delete(long id) {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        String selection = UbicacionBeneficiario.ID_BENEFICIARIO + " = ? ";
        String[] selectionArgs = {String.valueOf(id)};
        int result = db.delete(UbicacionBeneficiario.TABLE_NAME, selection, selectionArgs);
        return (result > 0);
    }

    public boolean DeleteAll() {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        int result = db.delete(UbicacionBeneficiario.TABLE_NAME, null, null);
        return (result > 0);
    }

    public List<UbicacionBeneficiario> List()
    {   synchronized (this) {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        List<UbicacionBeneficiario> lstEventos = new ArrayList<>();

        try {

            // String[] selectionArgs =  {String.valueOf(estado)};
            String where = null;

            Cursor c = db.query(UbicacionBeneficiario.TABLE_NAME, projectionDefault(), where, null, null, null, "[" + UbicacionBeneficiario.ID_BENEFICIARIO + "] DESC");

            if (c.moveToFirst()) {
                do {
                    lstEventos.add(new UbicacionBeneficiario(c));
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception ex) {
            Log.d("Categories.List", ex.getMessage());
        }

        db.close();

        return lstEventos;
    }
    }

    public UbicacionBeneficiario Read(int id)
    {
        synchronized (this) {
            InitDbHelper();
            SQLiteDatabase db = _dbHelper.getReadableDatabase();
            UbicacionBeneficiario UbicacionBeneficiario = null;
            try {
                String where = UbicacionBeneficiario.ID_BENEFICIARIO + "=?";
                String[] selectionArgs = {String.valueOf(id)};
                Cursor c = db.query(UbicacionBeneficiario.TABLE_NAME, projectionDefault(), where, selectionArgs, null, null, "[" + UbicacionBeneficiario.ID_BENEFICIARIO + "] DESC");

                if (c.moveToFirst()) {
                    UbicacionBeneficiario = new UbicacionBeneficiario(c);
                }
                c.close();
            } catch (Exception ex) {
                Log.d("Categories.List", ex.getMessage());
            }

            db.close();

            return UbicacionBeneficiario;
        }
    }
}
