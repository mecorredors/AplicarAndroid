package car.gov.co.carserviciociudadano.common;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.crashlytics.android.Crashlytics;


public class DbHelper extends SQLiteOpenHelper {
    //Ruta por defecto de las bases de datos en el sistema Android
    private static String DATABASE_PATH;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 5;
    public static final String DATABASE_NAME = "db_bicicar.db";

    private final Context context;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

        if(android.os.Build.VERSION.SDK_INT >= 17){
            DATABASE_PATH = context.getApplicationInfo().dataDir + "/databases/";
        }
        else
        {
            DATABASE_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }

        //Llamado al metodo que verifica si existe la BD y si no crea una vacia y la reemplaza por una con nuestros datos
        try{
            createDataBase();
        } catch (IOException e) {
            throw new Error("Ha sido imposible crear la Base de Datos");
        }


    }

    /**
     * Crea una base de datos vac�a en el sistema y la reescribe con nuestro fichero de base de datos.
     * */
    private void createDataBase() throws IOException{
        boolean dbExist = checkDataBase();
        if(dbExist){
            //la base de datos existe y no hacemos nada.
        }
        else{
            //Llamando a este m�todo se crea la base de datos vac�a en la ruta por defecto del sistema
            //de nuestra aplicaci�n por lo que podremos sobreescribirla con nuestra base de datos.
            try {
                this.getReadableDatabase();
                this.close();
            }catch (Exception e){
                Crashlytics.logException(e);
            }

            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copiando Base de Datos " + e.toString());
            }
        }
    }

    /**
     * Comprueba si la base de datos existe para evitar copiar siempre el fichero cada vez que se abra la aplicaci�n.
     * @return true si existe, false si no existe
     */
    private boolean checkDataBase(){
        SQLiteDatabase checkDB = null;

        try{
            String myPath = DATABASE_PATH + DATABASE_NAME;
            File file = new File(myPath);
            if (file.exists()) {
                checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
            } else {
                Log.d("checkdatabase", "Aun no existe la base de datos");
            }
        } catch (SQLiteException e) {
            Log.d("erro checkdatabase", e.toString());
            //si llegamos aqui es porque la base de datos no existe todavia.
        }

        if(checkDB != null){
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    /**
     * Copia nuestra base de datos desde la carpeta assets a la reci�n creada
     * base de datos en la carpeta de sistema, desde d�nde podremos acceder a ella.
     * Esto se hace con bytestream.
     * */
    private void copyDataBase() throws IOException{
        //Abrimos el fichero de base de datos como entrada
        InputStream myInput = context.getAssets().open(DATABASE_NAME);

        //Ruta a la base de datos vac�a reci�n creada
        String outFileName = DATABASE_PATH + DATABASE_NAME;

        //Abrimos la base de datos vac�a como salida
        OutputStream myOutput = new FileOutputStream(outFileName);

        //Transferimos los bytes desde el fichero de entrada al de salida
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Liberamos los streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.disableWriteAheadLogging();
    }

    @Override
    public void onCreate(SQLiteDatabase arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

       if(oldVersion < 4)
        {
            //Agregamos a la tabla Beneficiarios
            try {
                db.execSQL(TABLA_BENEFICIARIOS);
            } catch (SQLiteException ex) {
                Log.d("DbHelper.onUpgrade", ex.toString());
            }
        }
        if(oldVersion < 5)
        {
            //Agregamos a la tabla Rutas
            try {
                db.execSQL(TABLA_RUTAS);
            } catch (SQLiteException ex) {
                Log.d("DbHelper.onUpgrade", ex.toString());
            }
        }

    }

    /**
     * Verificar si una columna ya existe en un tabla
     *
     * @param tableName nombre de la tabla
     * @param fieldName nombre de la columna
     * @return
     */
    private boolean FieldExist(SQLiteDatabase db, String tableName, String fieldName) {
        List<String> columns = GetColumns(db, tableName);
        if (columns != null) {
            for (String col : columns) {
                if (col.equals(fieldName))
                    return true;
            }
        }
        return false;
    }

    /**
     * Obtener todas las colunmnas de una tabla
     *
     * @param db
     * @param tableName
     * @return
     */
    private static List<String> GetColumns(SQLiteDatabase db, String tableName) {
        List<String> columns = null;
        Cursor c = null;
        try {
            c = db.rawQuery("select * from " + tableName + " limit 1", null);
            if (c != null) {

                columns = new ArrayList<String>(Arrays.asList(c.getColumnNames()));
            }
        } catch (Exception e) {
            Log.d(tableName, e.toString());
        } finally {
            if (c != null)
                c.close();
        }

        return columns;
    }


    ////////NUEVAS TABLAS

    static String TABLA_BENEFICIARIOS = "CREATE TABLE Beneficiarios (" +
            "IDBeneficiario INTEGER PRIMARY KEY NOT NULL, " +
            "TipoNumeroID  TEXT NOT NULL, " +
            "NumeroID  TEXT NOT NULL, " +
            "IDMunicipioVive   TEXT NOT NULL, " +
            "IDVereda  TEXT, " +
            "IDColegio INTEGER NOT NULL, " +
            "IDEstado  INTEGER NOT NULL, " +
            "IDPedagogo    INTEGER, " +
            "Apellidos TEXT NOT NULL, " +
            "Nombres   TEXT NOT NULL, " +
            "FechaNacimiento   TEXT, " +
            "Genero    TEXT NOT NULL, " +
            "Curso TEXT NOT NULL, " +
            "NombreAcudiente   TEXT, " +
            "TelefonoContacto  TEXT, " +
            "Estatura  REAL NOT NULL, " +
            "Peso  REAL NOT NULL, " +
            "RH    TEXT, " +
            "IDFoto    TEXT, " +
            "DistanciaKM   TEXT NOT NULL, " +
            "IDPerfil  TEXT NOT NULL, " +
            "ClaveApp  TEXT, " +
            "Email TEXT, " +
            "FechaEstado   TEXT, " +
            "FechaCreacion TEXT, " +
            "FechaModificacion TEXT, " +
            "UsuarioModificacion   TEXT, " +
            "Direccion TEXT, " +
            "PersonaEmergencia TEXT, " +
            "TelefonoEmergencia    TEXT, " +
            "EPS    TEXT, " +
            "IDBicicleta    INTEGER, " +
            "LinkFoto    TEXT, " +
            "DescPerfil   TEXT );";



    static String TABLA_RUTAS = "CREATE TABLE Ruta (ID	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            "IDBeneficiario	INTEGER NOT NULL, " +
            "IDNivel	INTEGER NOT NULL, " +
            "Nombre	TEXT NOT NULL, " +
            "Descripcion	TEXT, " +
            "DistanciaKM	REAL, " +
            "DuracionMinutos	TEXT, " +
            "RutaTrayecto	TEXT, " +
            "Estado	INTEGER, " +
            "IDRuta	INTEGER );";

}

