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
    public float DistanciaKM;
    public float DuracionMinutos;
    public Date Fecha;
    public int Estado;
    public String Nombre;
    public String Label;
    public int IDBeneficiarioRegistro;
    public int IDBicicleta;
    public int IDBeneficiario;
    public String Polyline;


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
    public static final String POLYINE = "Polyline";

    public static final String TABLE_NAME = "LogTrayectos";

    public LogTrayecto(){}

    public LogTrayecto(Cursor c)
    {
        if(c.getColumnIndex(ID)>=0) this.IDLogTrayecto = c.getInt(c.getColumnIndex(ID));
        if(c.getColumnIndex(SERIAL)>=0) this.Serial = c.getString(c.getColumnIndex(SERIAL));
        if(c.getColumnIndex(RIN)>=0) this.TamanioRin = c.getString((c.getColumnIndex(RIN)));
        if(c.getColumnIndex(DISTANCIA_KM)>=0) this.DistanciaKM = c.getFloat(c.getColumnIndex(DISTANCIA_KM));
        if(c.getColumnIndex(DURACION_MINUTOS)>=0) this.DuracionMinutos = c.getFloat(c.getColumnIndex(DURACION_MINUTOS));
        if(c.getColumnIndex(FECHA)>=0) this.Fecha = Utils.convertToDateSQLLite(c.getString(c.getColumnIndex(FECHA)));
        if(c.getColumnIndex(ESTADO)>=0) this.Estado = c.getInt(c.getColumnIndex(ESTADO));
        if(c.getColumnIndex(NOMBRE)>=0) this.Nombre = c.getString(c.getColumnIndex(NOMBRE));
        if(c.getColumnIndex(ID_BENEFICIARIO)>=0) this.IDBeneficiario = c.getInt(c.getColumnIndex(ID_BENEFICIARIO));
        if(c.getColumnIndex(ID_BENEFICIARIO_REGISTRO)>=0) this.IDBeneficiarioRegistro = c.getInt(c.getColumnIndex(ID_BENEFICIARIO_REGISTRO));
        if(c.getColumnIndex(ID_BICICLETA)>=0) this.IDBicicleta = c.getInt(c.getColumnIndex(ID_BICICLETA));
        if(c.getColumnIndex(POLYINE)>=0) this.Polyline = c.getString(c.getColumnIndex(POLYINE));

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
