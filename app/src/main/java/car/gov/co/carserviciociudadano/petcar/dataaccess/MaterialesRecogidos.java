package car.gov.co.carserviciociudadano.petcar.dataaccess;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.List;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.common.APIClient;
import car.gov.co.carserviciociudadano.common.DbHelper;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.petcar.interfaces.ApiGestor;
import car.gov.co.carserviciociudadano.petcar.interfaces.ApiMaterialRecogido;
import car.gov.co.carserviciociudadano.petcar.interfaces.IGestor;
import car.gov.co.carserviciociudadano.petcar.interfaces.IMaterialRecogido;
import car.gov.co.carserviciociudadano.petcar.model.Gestor;
import car.gov.co.carserviciociudadano.petcar.model.MaterialRecogido;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MaterialesRecogidos {
    public static final String TAG ="MaterialesRecogidos";
    private DbHelper _dbHelper;

    private void InitDbHelper()
    {
        if(this._dbHelper == null)
            this._dbHelper = new DbHelper(AppCar.getContext());
    }

    private static String[] projectionDefault()
    {
        return new String[] {
                "[" + MaterialRecogido.ID + "]",
                "[" + MaterialRecogido.ID_MATERIAL_RECOGIDO + "]",
                "[" + MaterialRecogido.IDCONTENEDOR + "]",
                "[" + MaterialRecogido.IDTIPO_MATERIAL + "]",
                "[" + MaterialRecogido.FECHALECTURA_QR + "]",
                "[" + MaterialRecogido.KILOS + "]",
                "[" + MaterialRecogido.COMENTARIOS + "]",
                "[" + MaterialRecogido.ESTADO + "]"

        };
    }

    public  boolean guardar(MaterialRecogido element){
        if (read(element.Id) == null){
            return insert(element);
        }else{
            return update(element);
        }
    }

    public boolean insert(MaterialRecogido element) {
        InitDbHelper();
        ContentValues cv = new ContentValues();

        cv.put(MaterialRecogido.ID_MATERIAL_RECOGIDO, element.IDMaterialRecogido);
        cv.put(MaterialRecogido.IDCONTENEDOR, element.IDContenedor);
        cv.put(MaterialRecogido.IDTIPO_MATERIAL, element.IDTipoMaterial);
        cv.put(MaterialRecogido.FECHALECTURA_QR, Utils.toStringFromDate(element.FechaLecturaQR));
        cv.put(MaterialRecogido.COMENTARIOS, element.Comentarios);
        cv.put(MaterialRecogido.KILOS, element.Kilos);
        cv.put(MaterialRecogido.ESTADO, element.Estado);

        long rowid = 0;

        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        try {
            rowid=db.insertOrThrow(MaterialRecogido.TABLE_NAME, "", cv);
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

    public boolean update(MaterialRecogido element) {
        InitDbHelper();
        ContentValues cv = new ContentValues();

        String selection = MaterialRecogido.ID + " = ? ";
        String[] selectionArgs = {String.valueOf(element.Id)};

        cv.put(MaterialRecogido.ID_MATERIAL_RECOGIDO, element.IDMaterialRecogido);
        cv.put(MaterialRecogido.IDCONTENEDOR, element.IDContenedor);
        cv.put(MaterialRecogido.IDTIPO_MATERIAL, element.IDTipoMaterial);
        cv.put(MaterialRecogido.FECHALECTURA_QR, Utils.toStringFromDate(element.FechaLecturaQR));
        cv.put(MaterialRecogido.COMENTARIOS, element.Comentarios);
        cv.put(MaterialRecogido.KILOS, element.Kilos);
        cv.put(MaterialRecogido.ESTADO, element.Estado);

        long rowid;

        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        try {
            rowid=db.update(MaterialRecogido.TABLE_NAME, cv,selection, selectionArgs);

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
        String selection = MaterialRecogido.ID + " = ? ";
        String[] selectionArgs = {String.valueOf(id)};
        int result = db.delete(MaterialRecogido.TABLE_NAME, selection, selectionArgs);
        return (result > 0);
    }

    public boolean deleteAll() {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        int result = db.delete(MaterialRecogido.TABLE_NAME, null, null);
        return (result > 0);
    }

    public List<MaterialRecogido> list(int estado){

        String where = MaterialRecogido.ESTADO + " = " + estado;
        return list(where);
    }

    public List<MaterialRecogido> list(){
        return list(null);
    }

    public List<MaterialRecogido> list(String where)
    {   synchronized (this) {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        List<MaterialRecogido> lstMaterialRecogidos = new ArrayList<>();

        try {

            Cursor c = db.query(MaterialRecogido.TABLE_NAME, projectionDefault(), where, null, null, null, "[" + MaterialRecogido.ID + "] DESC");

            if (c.moveToFirst()) {
                do {
                    lstMaterialRecogidos.add(new MaterialRecogido(c));
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception ex) {
            Log.e("Categories.List", ex.getMessage());
            Crashlytics.logException(ex);
        }

        db.close();

        return lstMaterialRecogidos;
    }
    }

    public MaterialRecogido read(int id)
    {
        synchronized (this) {
            InitDbHelper();
            SQLiteDatabase db = _dbHelper.getReadableDatabase();
            MaterialRecogido MaterialRecogido = null;
            try {
                String where = MaterialRecogido.ID + "=?";
                String[] selectionArgs = {String.valueOf(id)};
                Cursor c = db.query(MaterialRecogido.TABLE_NAME, projectionDefault(), where, selectionArgs, null, null, "[" + MaterialRecogido.ID + "] DESC");

                if (c.moveToFirst()) {
                    MaterialRecogido = new MaterialRecogido(c);
                }
                c.close();
            } catch (Exception ex) {
                Log.e("Categories.List", ex.getMessage());
                Crashlytics.logException(ex);
            }

            db.close();

            return MaterialRecogido;
        }
    }


    public void publicar(final MaterialRecogido materialRecogido , final IMaterialRecogido iMaterialRecogido)
    {
        ApiMaterialRecogido apiMaterialRecogido = APIClient.getClient().create(ApiMaterialRecogido.class);
        Call<MaterialRecogido> call = apiMaterialRecogido.agregar(materialRecogido);

        call.enqueue(new Callback<MaterialRecogido>() {
            @Override
            public void onResponse(Call<MaterialRecogido> call, Response<MaterialRecogido> response) {
                if (response.code() == 200) {
                    MaterialRecogido element = response.body();
                    materialRecogido.IDMaterialRecogido = element.IDMaterialRecogido;
                    iMaterialRecogido.onSuccessPublicarMaterial(materialRecogido);
                } else {
                    iMaterialRecogido.onErrorPublicarMaterial(new ErrorApi(response));
                }
            }

            @Override
            public void onFailure(Call<MaterialRecogido> call, Throwable t) {
                call.cancel();
                iMaterialRecogido.onErrorPublicarMaterial(new ErrorApi(t));
                Log.d("item error", t.toString());
            }
        });
    }
}
