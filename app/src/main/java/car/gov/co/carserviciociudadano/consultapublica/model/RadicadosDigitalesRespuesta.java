package car.gov.co.carserviciociudadano.consultapublica.model;

import java.util.Date;

/**
 * Created by Olger on 28/01/2017.
 */

public class RadicadosDigitalesRespuesta {
    private String NumeroRadicado;
    private Date FechaRadicado;
    private String DescTipoRespuesta;
    private String Asunto;
    private String TipoDocumento;
    private int IDRadicadoDigital;
    private int Paginas;

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

    public String getDescTipoRespuesta() {
        return DescTipoRespuesta;
    }

    public void setDescTipoRespuesta(String descTipoRespuesta) {
        DescTipoRespuesta = descTipoRespuesta;
    }

    public String getAsunto() {
        return Asunto;
    }

    public void setAsunto(String asunto) {
        Asunto = asunto;
    }

    public String getTipoDocumento() {
        return TipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        TipoDocumento = tipoDocumento;
    }

    public int getIDRadicadoDigital() {
        return IDRadicadoDigital;
    }

    public void setIDRadicadoDigital(int IDRadicadoDigital) {
        this.IDRadicadoDigital = IDRadicadoDigital;
    }

    public int getPaginas() {
        return Paginas;
    }

    public void setPaginas(int paginas) {
        Paginas = paginas;
    }
}
