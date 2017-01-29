package car.gov.co.carserviciociudadano.consultapublica.model;

import java.util.Date;

/**
 * Created by Olger on 28/01/2017.
 */

public class Radicado {
    private byte IDTipoDocumento;
    private String NumeroRadicado;
    private Date FechaRadicado;
    private String Estado;
    private String Asunto;
    private String TramiteDescripcion;
    private String TipoTramite;
    private String Tema;

    public byte getIDTipoDocumento() {
        return IDTipoDocumento;
    }

    public void setIDTipoDocumento(byte IDTipoDocumento) {
        this.IDTipoDocumento = IDTipoDocumento;
    }

    public String getNumeroRadicado() {
        return NumeroRadicado;
    }

    public void setNumeroRadicado(String numeroRadicado) {
        NumeroRadicado = numeroRadicado;
    }

    public Date getFechaRadicado() {
        return FechaRadicado;
    }

    public void setFechaRadicado(Date fechaRadicado) {
        FechaRadicado = fechaRadicado;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public String getAsunto() {
        return Asunto;
    }

    public void setAsunto(String asunto) {
        Asunto = asunto;
    }

    public String getTramiteDescripcion() {
        return TramiteDescripcion;
    }

    public void setTramiteDescripcion(String tramiteDescripcion) {
        TramiteDescripcion = tramiteDescripcion;
    }

    public String getTipoTramite() {
        return TipoTramite;
    }

    public void setTipoTramite(String tipoTramite) {
        TipoTramite = tipoTramite;
    }

    public String getTema() {
        return Tema;
    }

    public void setTema(String tema) {
        Tema = tema;
    }
}
