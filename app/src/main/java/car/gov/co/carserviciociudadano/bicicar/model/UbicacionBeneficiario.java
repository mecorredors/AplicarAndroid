package car.gov.co.carserviciociudadano.bicicar.model;

import android.database.Cursor;

import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.common.ModelBase;

public class UbicacionBeneficiario extends ModelBase {
    public int IDBeneficiario;
    public double Latitude;
    public double Longitude;
    public double Estado;
    public double Norte;
    public double Este;

    public static final String TABLE_NAME = "UbicacionBeneficiario";

    public static final String ID_BENEFICIARIO = "IDBeneficiario";
    public static final String LATITUDE = "Latitude";
    public static final String LONGITUDE = "Longitude";
    public static final String ESTADO = "Estado";
    public static final String NORTE = "Norte";
    public static final String ESTE = "Este";

    public  UbicacionBeneficiario(){
    }

    public UbicacionBeneficiario(Cursor c) {
        if (c.getColumnIndex(ID_BENEFICIARIO) >= 0) this.IDBeneficiario = c.getInt(c.getColumnIndex(ID_BENEFICIARIO));
        if (c.getColumnIndex(LATITUDE) >= 0) this.Latitude = c.getDouble(c.getColumnIndex(LATITUDE));
        if (c.getColumnIndex(LONGITUDE) >= 0) this.Longitude = c.getDouble(c.getColumnIndex(LONGITUDE));
        if (c.getColumnIndex(ESTADO) >= 0) this.Estado = c.getInt(c.getColumnIndex(ESTADO));
        if (c.getColumnIndex(NORTE) >= 0) this.Norte = c.getDouble(c.getColumnIndex(NORTE));
        if (c.getColumnIndex(ESTE) >= 0) this.Este = c.getDouble(c.getColumnIndex(ESTE));
    }
}
