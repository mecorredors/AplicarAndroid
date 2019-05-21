package car.gov.co.carserviciociudadano.bicicar.dataaccess;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;

import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.bicicar.model.Evento;
import car.gov.co.carserviciociudadano.common.DbHelper;

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
        return new String[]{
                "[" + Evento.ID_EVENTO + "]",
                "[" + Evento.ID_COLEGIO + "]",
                "[" + Evento.ID_TIPO_EVENTO + "]",
                "[" + Evento.ID_RESPONSABLE + "]",
                "[" + Evento.NOMBRE + "]",
                "[" + Evento.F_INICIO + "]",
                "[" + Evento.F_FIN + "]",
                "[" + Evento.DESCRIPCION + "]",
                "[" + Evento.ESTADO + "]" };

    }

    public boolean Insert(Evento element) {
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

    public boolean Update(Evento element) {
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

    public boolean Delete(long id) {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        String selection = Evento.ID_EVENTO + " = ? ";
        String[] selectionArgs = {String.valueOf(id)};
        int result = db.delete(Evento.TABLE_NAME, selection, selectionArgs);
        return (result > 0);
    }

    public boolean DeleteAll() {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        int result = db.delete(Evento.TABLE_NAME, null, null);
        return (result > 0);
    }

    public List<Evento> List()
    {   synchronized (this) {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        List<Evento> lstEventos = new ArrayList<>();

        try {

            // String[] selectionArgs =  {String.valueOf(estado)};
            String where = null;

            Cursor c = db.query(Evento.TABLE_NAME, projectionDefault(), where, null, null, null, "[" + Evento.ID_EVENTO + "] DESC");

            if (c.moveToFirst()) {
                do {
                    lstEventos.add(new Evento(c));
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

    public Evento Read(int id)
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
                Log.d("Categories.List", ex.getMessage());
            }

            db.close();

            return Evento;
        }
    }

}
