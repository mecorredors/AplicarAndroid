package car.gov.co.carserviciociudadano.bicicar.model;

import android.database.Cursor;
import car.gov.co.carserviciociudadano.common.ModelBase;

public class Colegio extends ModelBase {
    public  int  IDColegio;
    public  String IDMunicipio;
    public  String  Nombre;
    public boolean  Activo;
    public String Municipio;
    public int IDTipoInstitucion;
    public  String TipoInstitucion;
    public double Latitude;
    public double Longitude;
    public double Norte;
    public double Este;
    public double Estado;

    public static final String TABLE_NAME = "Colegios";

    public static final String ID_COLEGIO = "IDColegio";
    public static final String ID_MUNICIPIO = "IDMunicipio";
    public static final String NOMBRE = "Nombre";
    public static final String ACTIVO = "Activo";
    public static final String MUNICIPIO = "Municipio";
    public static final String ID_TIPO_INSTITUCION = "IDTipoInstitucion";
    public static final String TIPO_INSTITUCION = "TipoInstitucion";
    public static final String LATITUDE = "Latitude";
    public static final String LONGITUDE = "Longitude";
    public static final String NORTE = "Norte";
    public static final String ESTE = "Este";
    public static final String ESTADO = "Estado";

    public  Colegio(){
    }
    public  Colegio(String nombre){
        Nombre = nombre;
    }
    public Colegio(Cursor c) {
        if (c.getColumnIndex(ID_COLEGIO) >= 0) this.IDColegio = c.getInt(c.getColumnIndex(ID_COLEGIO));
        if (c.getColumnIndex(ID_MUNICIPIO) >= 0) this.IDMunicipio = c.getString(c.getColumnIndex(ID_MUNICIPIO));
        if (c.getColumnIndex(NOMBRE) >= 0) this.Nombre = c.getString(c.getColumnIndex(NOMBRE));
        if (c.getColumnIndex(ACTIVO) >= 0) this.Activo =  c.getInt(c.getColumnIndex(ACTIVO)) == 0;
        if (c.getColumnIndex(MUNICIPIO) >= 0) this.Municipio = c.getString(c.getColumnIndex(MUNICIPIO));
        if (c.getColumnIndex(ID_TIPO_INSTITUCION) >= 0) this.IDTipoInstitucion = c.getInt(c.getColumnIndex(ID_TIPO_INSTITUCION));
        if (c.getColumnIndex(TIPO_INSTITUCION) >= 0) this.TipoInstitucion = c.getString(c.getColumnIndex(TIPO_INSTITUCION));
        if (c.getColumnIndex(LATITUDE) >= 0) this.Latitude = c.getDouble(c.getColumnIndex(LATITUDE));
        if (c.getColumnIndex(LONGITUDE) >= 0) this.Longitude = c.getDouble(c.getColumnIndex(LONGITUDE));
        if (c.getColumnIndex(NORTE) >= 0) this.Norte = c.getDouble(c.getColumnIndex(NORTE));
        if (c.getColumnIndex(ESTE) >= 0) this.Este = c.getDouble(c.getColumnIndex(ESTE));
        if (c.getColumnIndex(ESTADO) >= 0) this.Estado = c.getInt(c.getColumnIndex(ESTADO));
    }

    @Override
    public String  toString(){
        return this.Nombre + ( Municipio != null ?  " | " + Municipio : "") ;
    }
    @Override
    public boolean equals(Object o) {
        return (o instanceof Colegio && IDColegio == ((Colegio) o).IDColegio );
    }
}
