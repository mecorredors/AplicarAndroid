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

public class MaterialRecogido extends ModelBase {

    public int Id;
    public int IDContenedor;
    public int IDTipoMaterial;
    public Date FechaLecturaQR;
    public double  Kilos;
    public String  Comentarios;
    public int Estado;

    /// referencias
    private   Contenedor contenedor;
    private TipoMaterial tipoMaterial;

    // otros
    public String UsuarioCreacion;
    public int IDGestor;

    public static final String TABLE_NAME = "MaterialRecogido";

    public static final String ID = "Id";
    public static final String IDCONTENEDOR ="IDContenedor";
    public static final String IDTIPO_MATERIAL = "IDTipoMaterial";
    public static final String FECHALECTURA_QR = "FechaLecturaQR";
    public static final String COMENTARIOS = "Comentarios";
    public static final String KILOS = "Kilos";
    public static final String ESTADO = "Estado";



    public MaterialRecogido(){
    }

    public MaterialRecogido(Cursor c)
    {
        if(c.getColumnIndex(ID)>=0) this.Id = c.getInt(c.getColumnIndex(ID));
        if(c.getColumnIndex(IDCONTENEDOR)>=0) this.IDContenedor = c.getInt(c.getColumnIndex(IDCONTENEDOR));
        if(c.getColumnIndex(IDTIPO_MATERIAL)>=0) this.IDTipoMaterial = c.getInt(c.getColumnIndex(IDTIPO_MATERIAL));
        if(c.getColumnIndex(FECHALECTURA_QR)>=0) this.FechaLecturaQR = Utils.convertToDate(c.getString(c.getColumnIndex(FECHALECTURA_QR)));
        if(c.getColumnIndex(COMENTARIOS)>=0) this.Comentarios = c.getString(c.getColumnIndex(COMENTARIOS));
        if(c.getColumnIndex(KILOS)>=0) this.Kilos = c.getDouble(c.getColumnIndex(KILOS));
        if(c.getColumnIndex(ESTADO)>=0) this.Estado = c.getInt(c.getColumnIndex(ESTADO));

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

    public TipoMaterial getTipoMaterial(){
        if (tipoMaterial == null){
            tipoMaterial = new TiposMaterial().read(IDTipoMaterial);
        }
        return  tipoMaterial;
    }

    public Contenedor getContenedor(){
        if (contenedor == null){
            contenedor = new Contenedores().read(IDContenedor);
        }
        return  contenedor;
    }

}
