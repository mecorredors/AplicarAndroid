package car.gov.co.carserviciociudadano.bicicar.model;

import android.database.Cursor;

import car.gov.co.carserviciociudadano.common.ModelBase;

public class Asistente extends ModelBase {
    public int IDEvento;
    public int IDBeneficiario;
    public String UsuarioCreacion;
    public int Estado;

    public static final String TABLE_NAME = "Asistentes";

    public static final String ID_EVENTO = "IDEvento";
    public static final String ID_BENEFICIARIO ="IDBeneficiario";
    public static final String ESTADO ="Estado";


    public  Asistente(){
    }
    public  Asistente(int idEvento, int idBeneficiario, int estado){
        IDEvento = idEvento;
        IDBeneficiario = idBeneficiario;
        Estado = estado;
    }
    public Asistente(Cursor c) {
        if (c.getColumnIndex(ID_EVENTO) >= 0) this.IDEvento = c.getInt(c.getColumnIndex(ID_EVENTO));
        if (c.getColumnIndex(ID_BENEFICIARIO) >= 0) this.IDBeneficiario = c.getInt(c.getColumnIndex(ID_BENEFICIARIO));
        if (c.getColumnIndex(ESTADO) >= 0) this.Estado = c.getInt(c.getColumnIndex(ESTADO));
    }
}
