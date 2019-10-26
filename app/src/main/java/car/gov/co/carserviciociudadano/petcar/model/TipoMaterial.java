package car.gov.co.carserviciociudadano.petcar.model;

import android.database.Cursor;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import car.gov.co.carserviciociudadano.common.ModelBase;

public class TipoMaterial extends ModelBase {

    public int IDTipoMaterial;
    public String Nombre;
    public String  Descripcion;

    public static final String TABLE_NAME = "TiposMaterial";

    public static final String IDTIPOMATERIAL = "IDTipoMaterial";
    public static final String NOMBRE ="Nombre";
    public static final String DESCRIPCION = "Descripcion";


    public TipoMaterial(){
    }

    public TipoMaterial(Cursor c)
    {
        if(c.getColumnIndex(IDTIPOMATERIAL)>=0) this.IDTipoMaterial = c.getInt(c.getColumnIndex(IDTIPOMATERIAL));
        if(c.getColumnIndex(NOMBRE)>=0) this.Nombre = c.getString(c.getColumnIndex(NOMBRE));
        if(c.getColumnIndex(DESCRIPCION)>=0) this.Descripcion = c.getString(c.getColumnIndex(DESCRIPCION));

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

}
