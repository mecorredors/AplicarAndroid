package car.gov.co.carserviciociudadano.bicicar.model;

import android.database.Cursor;

import java.util.Date;

import car.gov.co.carserviciociudadano.common.ModelBase;

public class Ruta extends ModelBase {
    public int Id;
    public int IDRuta;
    public int IDBeneficiario;
    public int IDNivel;
    public String Nombre;
    public String Descripcion;
    public String FotoPrincipal;
    public float DistanciaKM;
    public float DuracionMinutos;
    public String RutaTrayecto;
    public String UsuarioCreacion;
    public String UsuarioModificacion;
    public Date FechaCreacion;
    public Date UltimaModificacion;
    public String NombreNivel;
    public int Estado;

    public static final String TABLE_NAME = "Ruta";

    public static final String ID = "ID";
    public static final String ID_BENEFICIARIO = "IDBeneficiario";
    public static final String ID_NIVEL = "IDNivel";
    public static final String NOMBRE = "Nombre";
    public static final String DESCRIPCION = "Descripcion";
    public static final String DISTANCIA_KM = "DistanciaKM";
    public static final String DURACION_MINUTOS = "DuracionMinutos";
    public static final String RUTA_TRAYECTO = "RutaTrayecto";
    public static final String ESTADO = "Estado";
    public static final String ID_RUTA = "IDRuta";

    public Ruta(){}

    public Ruta(Cursor c) {
        if (c.getColumnIndex(ID) >= 0) this.Id = c.getInt(c.getColumnIndex(ID));
        if (c.getColumnIndex(ID_BENEFICIARIO) >= 0) this.IDBeneficiario = c.getInt(c.getColumnIndex(ID_BENEFICIARIO));
        if (c.getColumnIndex(ID_NIVEL) >= 0) this.IDNivel = c.getInt(c.getColumnIndex(ID_NIVEL));
        if (c.getColumnIndex(ID_RUTA) >= 0) this.IDRuta = c.getInt(c.getColumnIndex(ID_RUTA));
        if (c.getColumnIndex(NOMBRE) >= 0) this.Nombre = c.getString(c.getColumnIndex(NOMBRE));
        if (c.getColumnIndex(DESCRIPCION) >= 0) this.Descripcion = c.getString(c.getColumnIndex(DESCRIPCION));
        if (c.getColumnIndex(RUTA_TRAYECTO) >= 0) this.RutaTrayecto = c.getString(c.getColumnIndex(RUTA_TRAYECTO));
        if (c.getColumnIndex(DISTANCIA_KM) >= 0) this.DistanciaKM = c.getFloat(c.getColumnIndex(DISTANCIA_KM));
        if (c.getColumnIndex(DURACION_MINUTOS) >= 0) this.DuracionMinutos = c.getFloat(c.getColumnIndex(DURACION_MINUTOS));
        if (c.getColumnIndex(ESTADO) >= 0) this.Estado = c.getInt(c.getColumnIndex(ESTADO));
    }
}
