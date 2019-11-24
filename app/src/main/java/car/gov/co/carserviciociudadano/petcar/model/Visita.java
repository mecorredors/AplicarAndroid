package car.gov.co.carserviciociudadano.petcar.model;

import android.database.Cursor;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.common.ModelBase;
import car.gov.co.carserviciociudadano.petcar.dataaccess.Contenedores;
import car.gov.co.carserviciociudadano.petcar.dataaccess.TiposMaterial;

public class Visita extends ModelBase {

    public int Id;
    public int IDVisita;
    public int IDContenedor;
    public Date FechaLecturaQR;
    public String  Comentarios;
    public int Estado;
    public int IDGestor;

    /// referencias
    private   Contenedor contenedor;


    // otros
    public String UsuarioCreacion;


    public static final String TABLE_NAME = "Visitas";

    public static final String ID = "Id";
    public static final String ID_VISITA = "IDVisita";
    public static final String IDCONTENEDOR ="IDContenedor";
    public static final String FECHALECTURA_QR = "FechaLecturaQR";
    public static final String COMENTARIOS = "Comentarios";
    public static final String ESTADO = "Estado";
    public static final String ID_GESTOR = "IDGestor";


    public Visita(){
    }

    public Visita(Cursor c)
    {
        if(c.getColumnIndex(ID)>=0) this.Id = c.getInt(c.getColumnIndex(ID));
        if(c.getColumnIndex(ID_VISITA)>=0) this.IDVisita = c.getInt(c.getColumnIndex(ID_VISITA));
        if(c.getColumnIndex(IDCONTENEDOR)>=0) this.IDContenedor = c.getInt(c.getColumnIndex(IDCONTENEDOR));
        if(c.getColumnIndex(FECHALECTURA_QR)>=0) this.FechaLecturaQR = Utils.convertToDate(c.getString(c.getColumnIndex(FECHALECTURA_QR)));
        if(c.getColumnIndex(COMENTARIOS)>=0) this.Comentarios = c.getString(c.getColumnIndex(COMENTARIOS));
        if(c.getColumnIndex(ESTADO)>=0) this.Estado = c.getInt(c.getColumnIndex(ESTADO));
        if(c.getColumnIndex(ID_GESTOR)>=0) this.IDGestor = c.getInt(c.getColumnIndex(ID_GESTOR));

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

    public Contenedor getContenedor(){
        if (contenedor == null){
            contenedor = new Contenedores().read(IDContenedor);
        }
        return  contenedor;
    }

}
