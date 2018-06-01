package car.gov.co.carserviciociudadano.bicicar.model;

import android.database.Cursor;

import java.util.Date;

import car.gov.co.carserviciociudadano.Utils.Utils;

public class Actividad {
    public long Id;
    public String Serial;
    public String Rin;
    public float DistanciaKM;
    public float DuracionMinutos;
    public Date Fecha;
    public int Estado;


    public static final String ID = "id";
    public static final String SERIAL = "Serial";
    public static final String RIN = "Rin";
    public static final String DISTANCIA_KM = "DistanciaKM";
    public static final String DURACION_MINUTOS = "DuracionMinutos";
    public static final String FECHA = "Fecha";
    public static final String ESTADO = "Estado";

    public static final String TABLE_NAME = "Actividad";

    public Actividad(Cursor c)
    {
        if(c.getColumnIndex(ID)>=0) this.Id = c.getInt(c.getColumnIndex(ID));
        if(c.getColumnIndex(SERIAL)>=0) this.Serial = c.getString(c.getColumnIndex(SERIAL));
        if(c.getColumnIndex(RIN)>=0) this.Rin = c.getString((c.getColumnIndex(RIN)));
        if(c.getColumnIndex(DISTANCIA_KM)>=0) this.DistanciaKM = c.getFloat(c.getColumnIndex(DISTANCIA_KM));
        if(c.getColumnIndex(DURACION_MINUTOS)>=0) this.DuracionMinutos = c.getFloat(c.getColumnIndex(DURACION_MINUTOS));
        if(c.getColumnIndex(FECHA)>=0) this.Fecha = Utils.convertToDateSQLLite(c.getString(c.getColumnIndex(FECHA)));
    }
}
