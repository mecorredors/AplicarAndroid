package car.gov.co.carserviciociudadano.petcar.model;

import android.database.Cursor;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import car.gov.co.carserviciociudadano.common.ModelBase;

public class Gestor extends ModelBase {

    public int IDGestor;
    public String IDMunicipio;
    public int  TipoPersona;
    public String TipoIdentificacion ;
    public String Identificacion ;
    public String NombreCompleto;
    public String DireccionContacto;
    public String Email;
    public String Telefono;
    public int TipoGestor;


    public static final String TABLE_NAME = "Gestores";

    public static final String IDGESTOR = "IDGestor";
    public static final String IDMUNICPIO ="IDMunicipio";
    public static final String TIPO_PERSONA = "TipoPersona";
    public static final String TIPO_IDENTIFICACION = "TipoIdentificacion";
    public static final String IDENTIFICACION = "Identificacion";
    public static final String NOMBRE_COMPLETO = "NombreCompleto";
    public static final String DIRECCION_CONTACTO = "DireccionContacto";
    public static final String EMAIL = "Email";
    public static final String TELEFONO = "Telefono";
    public static final String TIPO_GESTOR = "TipoGestor";


    public Gestor(){
    }

    public Gestor(Cursor c)
    {
        if(c.getColumnIndex(IDGESTOR)>=0) this.IDGestor = c.getInt(c.getColumnIndex(IDGESTOR));
        if(c.getColumnIndex(IDMUNICPIO)>=0) this.IDMunicipio = c.getString(c.getColumnIndex(IDMUNICPIO));
        if(c.getColumnIndex(TIPO_PERSONA)>=0) this.TipoPersona = c.getInt(c.getColumnIndex(TIPO_PERSONA));
        if(c.getColumnIndex(TIPO_IDENTIFICACION)>=0) this.TipoIdentificacion = c.getString((c.getColumnIndex(TIPO_IDENTIFICACION)));
        if(c.getColumnIndex(IDENTIFICACION)>=0) this.Identificacion = c.getString(c.getColumnIndex(IDENTIFICACION));
        if(c.getColumnIndex(NOMBRE_COMPLETO)>=0) this.NombreCompleto = c.getString(c.getColumnIndex(NOMBRE_COMPLETO));
        if(c.getColumnIndex(DIRECCION_CONTACTO)>=0) this.DireccionContacto = c.getString(c.getColumnIndex(DIRECCION_CONTACTO));
        if(c.getColumnIndex(EMAIL)>=0) this.Email = c.getString(c.getColumnIndex(EMAIL));
        if(c.getColumnIndex(TELEFONO)>=0) this.Telefono = c.getString(c.getColumnIndex(TELEFONO));
        if(c.getColumnIndex(TIPO_GESTOR)>=0) this.TipoGestor = c.getInt(c.getColumnIndex(TIPO_GESTOR));

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


    public static class Tipo{
        public static final int RECICLADOR = 2;
        public static final int INSTALADOR = 4;
        public static final int VISITA = 8;
        public static final int INSTALDOR_VISITA = 12;
    }

    public int getTipoGestor(){
        if (TipoGestor == 0 ) return Tipo.RECICLADOR;
         return TipoGestor;
    }

}
