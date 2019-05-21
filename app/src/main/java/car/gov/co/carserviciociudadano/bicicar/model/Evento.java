package car.gov.co.carserviciociudadano.bicicar.model;

import android.database.Cursor;

import java.util.Date;

import car.gov.co.carserviciociudadano.Utils.Utils;
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
    public int Estado;


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
    }
}
