package car.gov.co.carserviciociudadano.petcar.model;

import android.database.Cursor;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.common.ModelBase;

public class AdjuntoPetCar extends ModelBase {

    public int Id;
    public int IDAdjunto;
    public String Path;
    public int IDMaterialRecogido;
    public int Estado;
    //otros no bd
    public String PathTemporal;
    public int Type;

    public static final String TABLE_NAME = "AdjuntosPetCar";

    public static final String ID = "Id";
    public static final String IDADJUNTO ="IDAdjunto";
    public static final String PATH = "Path";
    public static final String IDMATERIAL_RECOGIDO = "IDMaterialRecogido";
    public static final String ESTADO = "Estado";



    public AdjuntoPetCar(){
    }

    public AdjuntoPetCar(Cursor c)
    {
        if(c.getColumnIndex(ID)>=0) this.Id = c.getInt(c.getColumnIndex(ID));
        if(c.getColumnIndex(IDADJUNTO)>=0) this.IDAdjunto = c.getInt(c.getColumnIndex(IDADJUNTO));
        if(c.getColumnIndex(PATH)>=0) this.Path = c.getString(c.getColumnIndex(PATH));
        if(c.getColumnIndex(IDMATERIAL_RECOGIDO)>=0) this.IDMaterialRecogido = c.getInt(c.getColumnIndex(IDMATERIAL_RECOGIDO));
        if(c.getColumnIndex(ESTADO)>=0) this.Estado = c.getInt(c.getColumnIndex(ESTADO));
        Type = Enumerator.TipoFoto.FOTO;
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
