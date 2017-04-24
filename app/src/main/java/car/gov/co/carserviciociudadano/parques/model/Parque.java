package car.gov.co.carserviciociudadano.parques.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.util.Date;

/**
 * Created by Olger on 26/11/2016.
 */

public class Parque {
    private int IDParque;
    private String NombreParque;
    private String InicialesParque;
    private int ActivoParque;
    private String DireccionParque;
    private int IDMunicipio;
    private String ObservacionesParque;
    private String TelefonosParque;
    private String LlaveParque;
    private Date DesactivarDesde;
    private Date DesactivarHasta;
    private int ActivarRangoDesactivacion;
    private String DetalleCuenta;
    private String PoliticasParque;
    private String ArchivoParque;
    private String UrlArchivoParque;
    private double Latitude;
    private double Longitude;

    public static final String ID_PARQUE = "IDParque";
    public static final String NOMBRE_PARQUE = "NombreParque";
    public static final String OBSERVACIONES_PARQUE = "ObservacionesParque";
    public static final String URL_ARCHIVO_PARQUE = "UrlArchivoParque";
    public static final String DETALLE_CUENTA = "DetalleCuenta";
    public static final String POLITICAS_PARQUE = "PoliticasParque";

    public Parque(){}

    public  Parque(String json) {
        if (json!= null && !json.isEmpty()) {
            try {
                GsonBuilder builder = new GsonBuilder();
                builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Gson gson = builder.create();

                Parque element = gson.fromJson(json, Parque.class);

                this.IDParque = element.IDParque;
                this.NombreParque = element.NombreParque;
                this.InicialesParque = element.InicialesParque;
                this.ActivoParque = element.ActivoParque;
                this.DireccionParque = element.DireccionParque;
                this.IDMunicipio = element.IDMunicipio;
                this.ObservacionesParque = element.ObservacionesParque;
                this.TelefonosParque = element.TelefonosParque;
                this.LlaveParque = element.LlaveParque;
                this.DesactivarDesde = element.DesactivarDesde;
                this.DesactivarHasta = element.DesactivarHasta;
                this.ActivarRangoDesactivacion = element.ActivarRangoDesactivacion;
                this.DetalleCuenta = element.DetalleCuenta;
                this.PoliticasParque = element.PoliticasParque;
                this.ArchivoParque = element.ArchivoParque;
                this.UrlArchivoParque = element.UrlArchivoParque;
                this.Latitude = element.Latitude;
                this.Longitude = element.Longitude;

            } catch (JsonSyntaxException ex) {
                Log.e("Parque.json", ex.toString());
            }
        }
    }

    public int getIDParque() {
        return IDParque;
    }

    public void setIDParque(int IDParque) {
        this.IDParque = IDParque;
    }

    public String getNombreParque() {
        return NombreParque;
    }

    public void setNombreParque(String nombreParque) {
        NombreParque = nombreParque;
    }

    public String getInicialesParque() {
        return InicialesParque;
    }

    public void setInicialesParque(String inicialesParque) {
        InicialesParque = inicialesParque;
    }

    public int getActivoParque() {
        return ActivoParque;
    }

    public void setActivoParque(int activoParque) {
        ActivoParque = activoParque;
    }

    public String getDireccionParque() {
        return DireccionParque;
    }

    public void setDireccionParque(String direccionParque) {
        DireccionParque = direccionParque;
    }

    public int getIDMunicipio() {
        return IDMunicipio;
    }

    public void setIDMunicipio(int IDMunicipio) {
        this.IDMunicipio = IDMunicipio;
    }

    public String getObservacionesParque() {
        return ObservacionesParque;
    }

    public void setObservacionesParque(String observacionesParque) {
        ObservacionesParque = observacionesParque;
    }

    public String getTelefonosParque() {
        return TelefonosParque;
    }

    public void setTelefonosParque(String telefonosParque) {
        TelefonosParque = telefonosParque;
    }

    public String getLlaveParque() {
        return LlaveParque;
    }

    public void setLlaveParque(String llaveParque) {
        LlaveParque = llaveParque;
    }

    public Date getDesactivarDesde() {
        return DesactivarDesde;
    }

    public void setDesactivarDesde(Date desactivarDesde) {
        DesactivarDesde = desactivarDesde;
    }

    public Date getDesactivarHasta() {
        return DesactivarHasta;
    }

    public void setDesactivarHasta(Date desactivarHasta) {
        DesactivarHasta = desactivarHasta;
    }

    public int getActivarRangoDesactivacion() {
        return ActivarRangoDesactivacion;
    }

    public void setActivarRangoDesactivacion(int activarRangoDesactivacion) {
        ActivarRangoDesactivacion = activarRangoDesactivacion;
    }

    public String getDetalleCuenta() {
        return DetalleCuenta;
    }

    public void setDetalleCuenta(String detalleCuenta) {
        DetalleCuenta = detalleCuenta;
    }

    public String getPoliticasParque() {
        return PoliticasParque;
    }

    public void setPoliticasParque(String politicasParque) {
        PoliticasParque = politicasParque;
    }

    public String getArchivoParque() {
        return ArchivoParque;
    }

    public void setArchivoParque(String archivoParque) {
        ArchivoParque = archivoParque;
    }

    public String getUrlArchivoParque() {
        return UrlArchivoParque;
    }

    public void setUrlArchivoParque(String urlArchivoParque) {
        UrlArchivoParque = urlArchivoParque;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}
