package car.gov.co.carserviciociudadano.bicicar.model;

import android.database.Cursor;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import car.gov.co.carserviciociudadano.Utils.Utils;

public class LogTrayecto {
    public long IDLogTrayecto;
    public String Serial;
    public String TamanioRin;
    public float DistanciaKm;
    public float DuracionMinutos;
    public Date Fecha;
    public int Estado;
    public String Nombre;
    public String Label;
    public int IDBeneficiarioRegistro;
    public int IDBicicleta;
    public int IDBeneficiario;
    public double LatitudePuntoA;
    public double LongitudePuntoA;
    public double NortePuntoA;
    public double EstePuntoA;
    public double LatitudePuntoB;
    public double LongitudePuntoB;
    public double NortePuntoB;
    public double EstePuntoB;
    public String Ruta;
    public int TotalItems;

    public static final String ID = "id";
    public static final String SERIAL = "Serial";
    public static final String RIN = "Rin";
    public static final String DISTANCIA_KM = "DistanciaKM";
    public static final String DURACION_MINUTOS = "DuracionMinutos";
    public static final String FECHA = "Fecha";
    public static final String ESTADO = "Estado";
    public static final String NOMBRE = "Nombre";
    public static final String ID_BENEFICIARIO = "IDBeneficiario";
    public static final String ID_BENEFICIARIO_REGISTRO = "IDBeneficiarioRegistro";
    public static final String ID_BICICLETA = "IDBicicleta";
    public static final String RUTA = "Ruta";
    public static final String LATITUDE_PUNTO_A = "LatitudePuntoA";
    public static final String LONGITUDE_PUNTO_A= "LongitudePuntoA";
    public static final String LATITUDE_PUNTO_B = "LatitudePuntoB";
    public static final String LONGITUDE_PUNTO_B = "LongitudePuntoA";
    public static final String ESTE_PUNTO_A = "EstePuntoA";
    public static final String NORTE_PUNTO_A = "NortePuntoA";
    public static final String ESTE_PUNTO_B = "EstePuntoB";
    public static final String NORTE_PUNTO_B = "NortePuntoB";

    public static final String TABLE_NAME = "LogTrayectos";

    public LogTrayecto(){}

    public LogTrayecto(Cursor c)
    {
        if(c.getColumnIndex(ID)>=0) this.IDLogTrayecto = c.getInt(c.getColumnIndex(ID));
        if(c.getColumnIndex(SERIAL)>=0) this.Serial = c.getString(c.getColumnIndex(SERIAL));
        if(c.getColumnIndex(RIN)>=0) this.TamanioRin = c.getString((c.getColumnIndex(RIN)));
        if(c.getColumnIndex(DISTANCIA_KM)>=0) this.DistanciaKm = c.getFloat(c.getColumnIndex(DISTANCIA_KM));
        if(c.getColumnIndex(DURACION_MINUTOS)>=0) this.DuracionMinutos = c.getFloat(c.getColumnIndex(DURACION_MINUTOS));
        if(c.getColumnIndex(FECHA)>=0) this.Fecha = Utils.convertToDateSQLLite(c.getString(c.getColumnIndex(FECHA)));
        if(c.getColumnIndex(ESTADO)>=0) this.Estado = c.getInt(c.getColumnIndex(ESTADO));
        if(c.getColumnIndex(NOMBRE)>=0) this.Nombre = c.getString(c.getColumnIndex(NOMBRE));
        if(c.getColumnIndex(ID_BENEFICIARIO)>=0) this.IDBeneficiario = c.getInt(c.getColumnIndex(ID_BENEFICIARIO));
        if(c.getColumnIndex(ID_BENEFICIARIO_REGISTRO)>=0) this.IDBeneficiarioRegistro = c.getInt(c.getColumnIndex(ID_BENEFICIARIO_REGISTRO));
        if(c.getColumnIndex(ID_BICICLETA)>=0) this.IDBicicleta = c.getInt(c.getColumnIndex(ID_BICICLETA));
        if(c.getColumnIndex(RUTA)>=0) this.Ruta = c.getString(c.getColumnIndex(RUTA));
        if(c.getColumnIndex(LATITUDE_PUNTO_A)>=0) this.LatitudePuntoA = c.getDouble(c.getColumnIndex(LATITUDE_PUNTO_A));
        if(c.getColumnIndex(LONGITUDE_PUNTO_A)>=0) this.LongitudePuntoA = c.getDouble(c.getColumnIndex(LONGITUDE_PUNTO_A));
        if(c.getColumnIndex(LATITUDE_PUNTO_B)>=0) this.LatitudePuntoB = c.getDouble(c.getColumnIndex(LATITUDE_PUNTO_B));
        if(c.getColumnIndex(LONGITUDE_PUNTO_B)>=0) this.LongitudePuntoB = c.getDouble(c.getColumnIndex(LONGITUDE_PUNTO_B));
        if(c.getColumnIndex(NORTE_PUNTO_A)>=0) this.NortePuntoA = c.getDouble(c.getColumnIndex(NORTE_PUNTO_A));
        if(c.getColumnIndex(ESTE_PUNTO_A)>=0) this.EstePuntoA = c.getDouble(c.getColumnIndex(ESTE_PUNTO_A));
        if(c.getColumnIndex(NORTE_PUNTO_B)>=0) this.NortePuntoB = c.getDouble(c.getColumnIndex(NORTE_PUNTO_B));
        if(c.getColumnIndex(ESTE_PUNTO_B)>=0) this.EstePuntoB = c.getDouble(c.getColumnIndex(ESTE_PUNTO_B));
    }

    @Override public String toString(){
        Gson gson = new Gson();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(gson.toJson(this));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return jsonObject.toString();
    }

    public JSONObject toJSONObject(){

        Gson gson = new Gson();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(gson.toJson(this));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return jsonObject;
    }
}
