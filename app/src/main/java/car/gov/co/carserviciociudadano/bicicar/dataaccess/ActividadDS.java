package car.gov.co.carserviciociudadano.bicicar.dataaccess;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.bicicar.model.Actividad;
import car.gov.co.carserviciociudadano.common.DbHelper;

public class ActividadDS {
    private DbHelper _dbHelper;

    private void InitDbHelper()
    {
        if(this._dbHelper == null)
            this._dbHelper = new DbHelper(AppCar.getContext());
    }

    private static String[] projectionDefault()
    {
        return new String[] {
                "[" + Actividad.ID + "]",
                "[" + Actividad.SERIAL + "]",
                "[" + Actividad.RIN + "]",
                "[" + Actividad.DISTANCIA_KM + "]",
                "[" + Actividad.DURACION_MINUTOS + "]",
                "[" + Actividad.FECHA + "]",
                "[" + Actividad.ESTADO + "]"
        };
    }
    public boolean Insert(Actividad element) {
        InitDbHelper();

        ContentValues cv = new ContentValues();

        cv.put(Actividad.SERIAL, element.Serial);
        cv.put(Actividad.RIN, element.Rin);
        cv.put(Actividad.DISTANCIA_KM, element.DistanciaKM);
        cv.put(Actividad.DURACION_MINUTOS, element.DuracionMinutos);
        cv.put(Actividad.FECHA, Utils.toStringSQLLite(element.Fecha));
        cv.put(Actividad.ESTADO, element.Estado);

        long rowid = 0;

        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        try {
            rowid=db.insertOrThrow(Actividad.TABLE_NAME, "", cv);
            element.Id = rowid;
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
        String selection = Actividad.ID + " = ? ";
        String[] selectionArgs = {String.valueOf(id)};
        int result = db.delete(Actividad.TABLE_NAME, selection, selectionArgs);
        return (result > 0);
    }

    public List<Actividad> List(int estado)
    {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        List<Actividad> lstActividades = new ArrayList<>();


        try {
            String where = Actividad.ESTADO + "=?";
            String [] selectionArgs = {String.valueOf(estado)};
            Cursor c = db.query(Actividad.TABLE_NAME, projectionDefault(), where, selectionArgs, null, null,"[" + Actividad.ID + "]");

            if(c.moveToFirst())
            {
                do{
                    lstActividades.add(new Actividad(c));
                } while (c.moveToNext());
            }
            c.close();
        }
        catch(Exception ex)
        {
            Log.d("Categories.List", ex.getMessage());
        }

        db.close();

        return lstActividades;
    }

    public Actividad Read(int id)
    {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        Actividad actividad = null;
        try {
            String where = Actividad.ID + "=?";
            String [] selectionArgs = {String.valueOf(id)};
            Cursor c = db.query(Actividad.TABLE_NAME, projectionDefault(), where, selectionArgs, null, null,"[" + Actividad.ID + "]");

            if(c.moveToFirst())
            {
                actividad =  new Actividad(c);
            }
            c.close();
        }
        catch(Exception ex)
        {
            Log.d("Categories.List", ex.getMessage());
        }

        db.close();

        return actividad;
    }
}
