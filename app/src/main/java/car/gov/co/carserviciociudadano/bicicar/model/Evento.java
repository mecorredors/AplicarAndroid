package car.gov.co.carserviciociudadano.bicicar.model;

import android.database.Cursor;

import java.util.Date;

import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.TiposEvento;
import car.gov.co.carserviciociudadano.common.ModelBase;

public class Evento  extends ModelBase {

    public int IDEvento;
    public int IDColegio;
    public int IDTipoEvento;
    public int IDResponsable;
    public String Nombre;
    public Date FInicio;
    public Date FFin;
    public String Descripcion;
    public String UsuarioCreacion;
    public int Participantes;
    public float DistanciaKm;
    public float DuracionMinutos;
    public int Estado;
    public String HoraInicio;
    public String HoraFin;
    public String IDMunicipio;
    public String IDVereda;
    public String Predio;
    public int IDCuenca;
    public double Latitud;
    public double Longitud;
    public String Norte;
    public String Este;
    public double Altitud;
    public TipoEvento _TipoEvento;

    public static final String TABLE_NAME = "Eventos";

    public static final String ID_EVENTO = "IDEvento";
    public static final String ID_COLEGIO ="IDColegio";
    public static final String ID_TIPO_EVENTO ="IDTipoEvento";
    public static final String ID_RESPONSABLE ="IDResponsable";
    public static final String NOMBRE ="Nombre";
    public static final String F_INICIO ="FInicio";
    public static final String F_FIN ="FFin";
    public static final String DESCRIPCION ="Descripcion";
    public static final String ESTADO ="Estado";
    public static final String PARTICIPANTES ="Participantes";
    public static final String DISTANCIA_KM = "DistanciaKM";
    public static final String DURACION_MINUTOS = "DuracionMinutos";
    public static final String HORA_INICIO = "HoraInicio";
    public static final String HORA_FIN = "HoraFin";
    public static final String ID_MUNICIPIO = "IDMunicipio";
    public static final String ID_VEREDA = "IDVereda";
    public static final String PREDIO = "Predio";
    public static final String ID_CUENCA = "IDCuenca";
    public static final String LATITUD = "Latitud";
    public static final String LONGITUD = "Longitud";
    public static final String NORTE = "Norte";
    public static final String ESTE = "Este";
    public static final String ALTITUD = "Altitud";

    public  Evento(){
    }

    public Evento(Cursor c) {
        if (c.getColumnIndex(ID_EVENTO) >= 0) this.IDEvento = c.getInt(c.getColumnIndex(ID_EVENTO));
        if (c.getColumnIndex(ID_COLEGIO) >= 0) this.IDColegio = c.getInt(c.getColumnIndex(ID_COLEGIO));
        if (c.getColumnIndex(ID_TIPO_EVENTO) >= 0) this.IDTipoEvento = c.getInt(c.getColumnIndex(ID_TIPO_EVENTO));
        if (c.getColumnIndex(ID_RESPONSABLE) >= 0) this.IDResponsable = c.getInt(c.getColumnIndex(ID_RESPONSABLE));
        if (c.getColumnIndex(NOMBRE) >= 0) this.Nombre = c.getString(c.getColumnIndex(NOMBRE));
        if (c.getColumnIndex(F_INICIO) >= 0) this.FInicio = Utils.convertToDateSQLLite(c.getString(c.getColumnIndex(F_INICIO)));
        if (c.getColumnIndex(F_FIN) >= 0) this.FFin = Utils.convertToDateSQLLite(c.getString(c.getColumnIndex(F_FIN)));
        if (c.getColumnIndex(DESCRIPCION) >= 0) this.Descripcion = c.getString(c.getColumnIndex(DESCRIPCION));
        if (c.getColumnIndex(ESTADO) >= 0) this.Estado = c.getInt(c.getColumnIndex(ESTADO));
        if (c.getColumnIndex(PARTICIPANTES) >= 0) this.Participantes = c.getInt(c.getColumnIndex(PARTICIPANTES));
        if(c.getColumnIndex(DISTANCIA_KM)>=0) this.DistanciaKm = c.getFloat(c.getColumnIndex(DISTANCIA_KM));
        if(c.getColumnIndex(DURACION_MINUTOS)>=0) this.DuracionMinutos = c.getFloat(c.getColumnIndex(DURACION_MINUTOS));
        if(c.getColumnIndex(HORA_INICIO)>=0) this.HoraInicio = c.getString(c.getColumnIndex(HORA_INICIO));
        if(c.getColumnIndex(HORA_FIN)>=0) this.HoraFin = c.getString(c.getColumnIndex(HORA_FIN));
        if(c.getColumnIndex(ID_MUNICIPIO)>=0) this.IDMunicipio = c.getString(c.getColumnIndex(ID_MUNICIPIO));
        if(c.getColumnIndex(ID_VEREDA)>=0) this.IDVereda = c.getString(c.getColumnIndex(ID_VEREDA));
        if(c.getColumnIndex(PREDIO)>=0) this.Predio = c.getString(c.getColumnIndex(PREDIO));
        if(c.getColumnIndex(ID_CUENCA)>=0) this.IDCuenca = c.getInt(c.getColumnIndex(ID_CUENCA));
        if(c.getColumnIndex(LATITUD)>=0) this.Latitud = c.getDouble(c.getColumnIndex(LATITUD));
        if(c.getColumnIndex(LONGITUD)>=0) this.Longitud = c.getDouble(c.getColumnIndex(LONGITUD));
        if(c.getColumnIndex(NORTE)>=0) this.Norte = c.getString(c.getColumnIndex(NORTE));
        if(c.getColumnIndex(ESTE)>=0) this.Este = c.getString(c.getColumnIndex(ESTE));
        if(c.getColumnIndex(ALTITUD)>=0) this.Altitud = c.getDouble(c.getColumnIndex(ALTITUD));
    }

    public TipoEvento getTipoEvento(){
        if (_TipoEvento == null)
            _TipoEvento = new TiposEvento().read(IDTipoEvento);

        return _TipoEvento;
    }
}
