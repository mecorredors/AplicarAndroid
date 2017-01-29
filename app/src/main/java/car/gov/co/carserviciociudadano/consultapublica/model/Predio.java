package car.gov.co.carserviciociudadano.consultapublica.model;

import java.util.Date;

/**
 * Created by Olger on 29/01/2017.
 */

public class Predio {
    private int IDExpediente;
    private int IDProceso;
    private int IDPredio;
    private String IDVereda;
    private String IDActividadEconomica;
    private String CedulaCatastral;
    private float Area;
    private String Nombre;
    private String Direccion;
    private String Coordenadas;
    private String Observaciones;
    private boolean NoEsPredio;
    private String NombreVereda;
    private String IDMunicipio;
    private int IDDepartamento;
    private Date FechaCreacion;
    private String UsuarioCreacion;
    private Date UltimaModificacion;
    private String UsuarioModificacion;

    public int getIDExpediente() {
        return IDExpediente;
    }

    public void setIDExpediente(int IDExpediente) {
        this.IDExpediente = IDExpediente;
    }

    public int getIDProceso() {
        return IDProceso;
    }

    public void setIDProceso(int IDProceso) {
        this.IDProceso = IDProceso;
    }

    public int getIDPredio() {
        return IDPredio;
    }

    public void setIDPredio(int IDPredio) {
        this.IDPredio = IDPredio;
    }

    public String getIDVereda() {
        return IDVereda;
    }

    public void setIDVereda(String IDVereda) {
        this.IDVereda = IDVereda;
    }

    public String getIDActividadEconomica() {
        return IDActividadEconomica;
    }

    public void setIDActividadEconomica(String IDActividadEconomica) {
        this.IDActividadEconomica = IDActividadEconomica;
    }

    public String getCedulaCatastral() {
        return CedulaCatastral;
    }

    public void setCedulaCatastral(String cedulaCatastral) {
        CedulaCatastral = cedulaCatastral;
    }

    public float getArea() {
        return Area;
    }

    public void setArea(float area) {
        Area = area;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getCoordenadas() {
        return Coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        Coordenadas = coordenadas;
    }

    public String getObservaciones() {
        return Observaciones;
    }

    public void setObservaciones(String observaciones) {
        Observaciones = observaciones;
    }

    public boolean isNoEsPredio() {
        return NoEsPredio;
    }

    public void setNoEsPredio(boolean noEsPredio) {
        NoEsPredio = noEsPredio;
    }

    public String getNombreVereda() {
        return NombreVereda;
    }

    public void setNombreVereda(String nombreVereda) {
        NombreVereda = nombreVereda;
    }

    public String getIDMunicipio() {
        return IDMunicipio;
    }

    public void setIDMunicipio(String IDMunicipio) {
        this.IDMunicipio = IDMunicipio;
    }

    public int getIDDepartamento() {
        return IDDepartamento;
    }

    public void setIDDepartamento(int IDDepartamento) {
        this.IDDepartamento = IDDepartamento;
    }

    public Date getFechaCreacion() {
        return FechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        FechaCreacion = fechaCreacion;
    }

    public String getUsuarioCreacion() {
        return UsuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        UsuarioCreacion = usuarioCreacion;
    }

    public Date getUltimaModificacion() {
        return UltimaModificacion;
    }

    public void setUltimaModificacion(Date ultimaModificacion) {
        UltimaModificacion = ultimaModificacion;
    }

    public String getUsuarioModificacion() {
        return UsuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        UsuarioModificacion = usuarioModificacion;
    }
}
