package car.gov.co.carserviciociudadano.bicicar.dataaccess;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import car.gov.co.carserviciociudadano.bicicar.model.ArchivoAdjunto;
import car.gov.co.carserviciociudadano.common.DbHelper;


public class ArchivosAdjuntos {
    public static final String TAG ="ArchivosAdjuntos";
    private DbHelper _dbHelper;

    private void InitDbHelper()
    {
        if(this._dbHelper == null)
            this._dbHelper = new DbHelper(AppCar.getContext());
    }

    private static String[] projectionDefault()
    {
        return new String[]{
                "[" + ArchivoAdjunto.ID + "]",
                "[" + ArchivoAdjunto.ID_ARCHIVO_ADJUNTO + "]",
                "[" + ArchivoAdjunto.ID_EVENTO + "]",
                "[" + ArchivoAdjunto.NOMBRE + "]",
                "[" + ArchivoAdjunto.DESCRIPCION + "]",
                "[" + ArchivoAdjunto.ES_FOTO_EVENTO + "]",
                "[" + ArchivoAdjunto.PUBLICAR_WEB + "]",
                "[" + ArchivoAdjunto.PATH + "]",
                "[" + ArchivoAdjunto.ESTADO + "]",
                "[" + ArchivoAdjunto.PATH_TEMPORAL + "]" };

    }

    public boolean insert(ArchivoAdjunto element) {
        InitDbHelper();
        ContentValues cv = new ContentValues();

        cv.put(ArchivoAdjunto.ID_ARCHIVO_ADJUNTO, element.IDArchivoAdjunto);
        cv.put(ArchivoAdjunto.ID_EVENTO, element.IDEvento);
        cv.put(ArchivoAdjunto.NOMBRE, element.Nombre);
        cv.put(ArchivoAdjunto.DESCRIPCION, element.Descripcion);
        cv.put(ArchivoAdjunto.ES_FOTO_EVENTO, element.EsFotoEvento);
        cv.put(ArchivoAdjunto.PUBLICAR_WEB, element.PublicarWeb);
        cv.put(ArchivoAdjunto.PATH, element.Path);
        cv.put(ArchivoAdjunto.PATH_TEMPORAL, element.PathTemporal);
        cv.put(ArchivoAdjunto.ESTADO, element.Estado);

        long rowid = 0;
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        try {
            rowid = db.insertOrThrow(ArchivoAdjunto.TABLE_NAME, "", cv);
            element.Id = (int) rowid;
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

    public boolean update(ArchivoAdjunto element) {
        InitDbHelper();
        ContentValues cv = new ContentValues();

        String selection = ArchivoAdjunto.ID + " = ? ";
        String[] selectionArgs = {String.valueOf(element.Id)};

        cv.put(ArchivoAdjunto.ID_ARCHIVO_ADJUNTO, element.IDArchivoAdjunto);
        cv.put(ArchivoAdjunto.ID_EVENTO, element.IDEvento);
        cv.put(ArchivoAdjunto.NOMBRE, element.Nombre);
        cv.put(ArchivoAdjunto.DESCRIPCION, element.Descripcion);
        cv.put(ArchivoAdjunto.ES_FOTO_EVENTO, element.EsFotoEvento);
        cv.put(ArchivoAdjunto.PUBLICAR_WEB, element.PublicarWeb);
        cv.put(ArchivoAdjunto.PATH, element.Path);
        cv.put(ArchivoAdjunto.PATH_TEMPORAL, element.PathTemporal);
        cv.put(ArchivoAdjunto.ESTADO, element.Estado);


        long rowid = 0;
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        try {
            rowid=db.update(ArchivoAdjunto.TABLE_NAME, cv,selection, selectionArgs);

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

    public boolean delete(long id) {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getWritableDatabase();
        String selection = ArchivoAdjunto.ID + " = ? ";
        String[] selectionArgs = {String.valueOf(id)};
        int result = db.delete(ArchivoAdjunto.TABLE_NAME, selection, selectionArgs);
        return (result > 0);
    }

    public boolean deleteAll() {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getWritableDatabase();

        int result = db.delete(ArchivoAdjunto.TABLE_NAME, null, null);
        return (result > 0);
    }

    public List<ArchivoAdjunto> list(int idEvento)
    {
        return list(idEvento,  -1);
    }

    public List<ArchivoAdjunto> list(int idEvento, int estado)
    {   synchronized (this) {
        InitDbHelper();
        SQLiteDatabase db = _dbHelper.getReadableDatabase();
        List<ArchivoAdjunto> lstArchivoAdjuntos = new ArrayList<>();

        try {
            String where = "";
            if (estado != -1){
                where = ArchivoAdjunto.ID_EVENTO + " = " + idEvento + " and " + ArchivoAdjunto.ESTADO + " = " + estado;
            }else {
                where = ArchivoAdjunto.ID_EVENTO + " = " + idEvento;
            }
            String[] selectionArgs =  {String.valueOf(idEvento)};
            Cursor c = db.query(ArchivoAdjunto.TABLE_NAME, projectionDefault(), where, null, null, null, "[" + ArchivoAdjunto.ID + "] DESC");

            if (c.moveToFirst()) {
                do {
                    lstArchivoAdjuntos.add(new ArchivoAdjunto(c));
                } while (c.moveToNext());
            }
            c.close();
        } catch (Exception ex) {
            Log.d(TAG + "List", ex.getMessage());
        }

        db.close();

        return lstArchivoAdjuntos;
    }
    }

    public ArchivoAdjunto read(int id)
    {
        synchronized (this) {
            InitDbHelper();
            SQLiteDatabase db = _dbHelper.getReadableDatabase();
            ArchivoAdjunto ArchivoAdjunto = null;
            try {
                String where = ArchivoAdjunto.Id + "=?";
                String[] selectionArgs = {String.valueOf(id)};
                Cursor c = db.query(ArchivoAdjunto.TABLE_NAME, projectionDefault(), where, selectionArgs, null, null, "[" + ArchivoAdjunto.ID + "] ASC");

                if (c.moveToFirst()) {
                    ArchivoAdjunto = new ArchivoAdjunto(c);
                }
                c.close();
            } catch (Exception ex) {
                Log.d(TAG + "Read", ex.getMessage());
            }

            db.close();

            return ArchivoAdjunto;
        }
    }


    private List<ArchivoAdjunto> JSONArrayToList(JSONArray response) throws JSONException{
        List<ArchivoAdjunto> lista = new ArrayList<>();
        for(int i = 0; i < response.length(); i++){
            JSONObject jresponse = response.getJSONObject(i);
            lista.add( ArchivosAdjuntos.getItemFromJson(jresponse.toString()));
        }
        return lista;
    }


    public static ArchivoAdjunto getItemFromJson(String json) throws JsonSyntaxException {
        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Gson gson = builder.create();

        ArchivoAdjunto element = gson.fromJson(json, ArchivoAdjunto.class);
        return  element;
    }

    public int publicar(ArchivoAdjunto archivoAdjunto) {

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
                URL url = new URL(Config.API_BICICAR_EVENTO_AGREGAR_FOTOS+ "?idEvento="+archivoAdjunto.IDEvento + "&esFotoEvento=" + (archivoAdjunto.EsFotoEvento ? 1 : 0)+ "&publicarWEB=" + (archivoAdjunto.PublicarWeb ? 1 : 0));

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
