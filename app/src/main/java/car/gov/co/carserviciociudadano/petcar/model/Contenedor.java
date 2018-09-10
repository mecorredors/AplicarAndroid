package car.gov.co.carserviciociudadano.petcar.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;

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
}
