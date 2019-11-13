package car.gov.co.carserviciociudadano.petcar.model;

import android.database.Cursor;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by apple on 9/09/18.
 */

public class Contenedor implements ClusterItem {
    @SerializedName("IDContenedor")
    public int IDContenedor;
    @SerializedName("IDMunicipio")
    public String IDMunicipio;
    @SerializedName("Latitude")
    public double Latitude;
    @SerializedName("Longitude")
    public double Longitude;
    @SerializedName("Altitud")
    public double Altitud;
    @SerializedName("Direccion")
    public String Direccion;
    @SerializedName("FechaInstalacion")
    public Date FechaInstalacion;
    @SerializedName("FotoPrincipal")
    public String FotoPrincipal;
    @SerializedName("UsuarioCreacion")
    public String UsuarioCreacion;
    @SerializedName("FechaCreacion")
    public Date FechaCreacion;
    @SerializedName("UsuarioModificacion")
    public String UsuarioModificacion;
    @SerializedName("FechaModificacion")
    public Date FechaModificacion;
    @SerializedName("Icono")
    public String Icono;
    @SerializedName("Municipio")
    public String Municipio;
    @SerializedName("Codigo")
    public String Codigo;
    @SerializedName("TopeMaximoKG")
    public Double TopeMaximoKG;

    public static final String TABLE_NAME = "Contenedores";

    public static final String IDCONTENEDOR = "IDContenedor";
    public static final String IDMUNICIPIO ="IDMunicipio";
    public static final String LATITUDE = "Latitude";
    public static final String LONGITUDE = "Longitude";
    public static final String ALTITUD = "Altitud";
    public static final String FOTO_PRINCIPAL = "FotoPrincipal";
    public static final String CODIGO = "Codigo";
    public static final String TOPE_MAXIMO_KG = "TopeMaxKG";
    public static final String DIRECCION = "Direccion";
    public static final String MUNICIPIO = "Municipio";


    @Override
    public LatLng getPosition() {
        return new LatLng(Latitude, Longitude);
    }

    @Override
    public String getTitle() {
        return Direccion;
    }

    @Override
    public String getSnippet() {
        return null;
    }

    public Contenedor(){
    }

    public Contenedor(Cursor c)
    {
        if(c.getColumnIndex(IDCONTENEDOR)>=0) this.IDContenedor = c.getInt(c.getColumnIndex(IDCONTENEDOR));
        if(c.getColumnIndex(IDMUNICIPIO)>=0) this.IDMunicipio = c.getString(c.getColumnIndex(IDMUNICIPIO));
        if(c.getColumnIndex(LATITUDE)>=0) this.Latitude = c.getDouble(c.getColumnIndex(LATITUDE));
        if(c.getColumnIndex(LONGITUDE)>=0) this.Longitude = c.getDouble(c.getColumnIndex(LONGITUDE));
        if(c.getColumnIndex(ALTITUD)>=0) this.Altitud = c.getDouble(c.getColumnIndex(ALTITUD));
        if(c.getColumnIndex(DIRECCION)>=0) this.Direccion = c.getString(c.getColumnIndex(DIRECCION));
        if(c.getColumnIndex(CODIGO)>=0) this.Codigo = c.getString(c.getColumnIndex(CODIGO));
        if(c.getColumnIndex(FOTO_PRINCIPAL)>=0) this.FotoPrincipal = c.getString(c.getColumnIndex(FOTO_PRINCIPAL));
        if(c.getColumnIndex(TOPE_MAXIMO_KG)>=0) this.TopeMaximoKG = c.getDouble(c.getColumnIndex(TOPE_MAXIMO_KG));
        if(c.getColumnIndex(MUNICIPIO)>=0) this.Municipio = c.getString(c.getColumnIndex(MUNICIPIO));

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
