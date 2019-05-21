package car.gov.co.carserviciociudadano.bicicar.model;

import android.database.Cursor;

import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.common.ModelBase;

public class UbicacionBeneficiario extends ModelBase {
    public int IDBeneficiario;
    public double Latitude;
    public double Longitude;
    public double Estado;

    public static final String TABLE_NAME = "UbicacionBeneficiario";

    public static final String ID_BENEFICIARIO = "IDBeneficiario";
    public static final String LATITUDE = "Latitude";
    public static final String LONGITUDE = "Longitude";
    public static final String ESTADO = "Estado";

    public  UbicacionBeneficiario(){
    }

    public UbicacionBeneficiario(Cursor c) {
        if (c.getColumnIndex(ID_BENEFICIARIO) >= 0) this.IDBeneficiario = c.getInt(c.getColumnIndex(ID_BENEFICIARIO));
        if (c.getColumnIndex(LATITUDE) >= 0) this.Latitude = c.getInt(c.getColumnIndex(LATITUDE));
        if (c.getColumnIndex(LONGITUDE) >= 0) this.Longitude = c.getInt(c.getColumnIndex(LONGITUDE));
        if (c.getColumnIndex(ESTADO) >= 0) this.Estado = c.getInt(c.getColumnIndex(ESTADO));
    }
}
