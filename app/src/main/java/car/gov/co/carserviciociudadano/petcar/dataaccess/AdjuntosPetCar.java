package car.gov.co.carserviciociudadano.petcar.dataaccess;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.Utils.Config;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.Utils;
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

        cv.put(AdjuntoPetCar.IDADJUNTO, element.IDAdjunto);
        cv.put(AdjuntoPetCar.PATH, element.Path);
        cv.put(AdjuntoPetCar.IDMATERIAL_RECOGIDO, element.IDMaterialRecogido);
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
        cv.put(AdjuntoPetCar.IDMATERIAL_RECOGIDO, element.IDMaterialRecogido);
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

    public List<AdjuntoPetCar> list(int idLocalMaterialRecogido, int estado){
        String where = AdjuntoPetCar.IDMATERIAL_RECOGIDO  + " = " + idLocalMaterialRecogido;
        if (estado != Enumerator.Estado.TODOS) {
            where = where + " and " + AdjuntoPetCar.ESTADO + " = " + estado;
        }
        return list(where);
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


    public int publicar(AdjuntoPetCar archivoAdjunto, int idMaterialRecogido, String usuarioCreacion) {

        int serverResponseCode = 0;
        String sourceFileUri = archivoAdjunto.Path;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);


        if (!sourceFile.isFile()) {
            Log.d("uploadFile", "Source File not exist :" + sourceFileUri);
            return 0;

        }
        else
        {
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(Config.API_PETCAR_AGREGAR_ADJUNTOS+ "?idMaterialRecogido="+ idMaterialRecogido + "&usuarioCreacion=" + usuarioCreacion);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setConnectTimeout(Enumerator.TIMEOUT_PUBLISH_IMAGES);
                conn.setReadTimeout(Enumerator.TIMEOUT_PUBLISH_IMAGES);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", sourceFileUri);
                conn.setRequestProperty("Authorization", "Basic " + Utils.getAuthorizationBICICAR());

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=uploaded_file;filename=" + sourceFileUri + "" + lineEnd);

                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.d("status code",serverResponseCode+ "");
                Log.d("mensaje",serverResponseMessage);
                if(serverResponseCode == 200){

                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();

                    GsonBuilder builder = new GsonBuilder();
                    builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    Gson gson = builder.create();
                    String pathServer = gson.fromJson(sb.toString(), String.class);
                    Log.d("imagen subida ",pathServer);
                    archivoAdjunto.PathTemporal = pathServer;

                    return serverResponseCode;

                }
                Log.d("publicarImagen", "HTTP: " + serverResponseMessage + ": " + serverResponseCode);

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {
                Log.e("Upload file to server", "MalformedURLException " + ex.getMessage(), ex);
                return 500;
            } catch (Exception e) {
                Log.e("Upload file", "Exception : " + e.getMessage(), e);
                return 500;
            }

            return  serverResponseCode;

        } // End else block

    }

}
