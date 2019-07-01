package car.gov.co.carserviciociudadano.bicicar.model;

import android.database.Cursor;

import car.gov.co.carserviciociudadano.bicicar.dataaccess.TiposEvento;
import car.gov.co.carserviciociudadano.common.ModelBase;
import car.gov.co.carserviciociudadano.denunciaambiental.model.Lugar;

public class TipoEvento extends ModelBase {
    public int IDTipoEvento;
    public String Nombre;
    public boolean Publico;
    public boolean Recorrido;

    public static final String TABLE_NAME = "TiposEvento";

    public static final String ID_TIPO_EVENTO = "IDTipoEvento";
    public static final String NOMBRE ="Nombre";
    public static final String PUBLICO ="Publico";
    public static final String RECORRIDO ="Recorrido";


    public  TipoEvento(){
    }
    public  TipoEvento(String nombre){
        this.Nombre = nombre;
    }

    public TipoEvento(Cursor c) {
        if (c.getColumnIndex(ID_TIPO_EVENTO) >= 0) this.IDTipoEvento = c.getInt(c.getColumnIndex(ID_TIPO_EVENTO));
        if (c.getColumnIndex(NOMBRE) >= 0) this.Nombre = c.getString(c.getColumnIndex(NOMBRE));
        if (c.getColumnIndex(PUBLICO) >= 0) this.Publico = c.getInt(c.getColumnIndex(PUBLICO)) == 1;
        if (c.getColumnIndex(RECORRIDO) >= 0) this.Recorrido = c.getInt(c.getColumnIndex(RECORRIDO)) == 1;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof TiposEvento && IDTipoEvento == ((TipoEvento) o).IDTipoEvento );
    }
    @Override
    public String  toString(){
        return this.Nombre + (Recorrido ?  " | Con recorrido"  : "") ;
    }
}
