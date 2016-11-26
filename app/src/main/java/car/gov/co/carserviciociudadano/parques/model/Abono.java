package car.gov.co.carserviciociudadano.parques.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.util.Date;

/**
 * Created by Olger on 26/11/2016.
 */

public class Abono {

    private long IdAbono;
    private Date FechaAbono;
    private long ValorAbono;
    private String ComprobanteAbono;
    private int EstadoAbono;
    private String UsuarioAprobacionAbono;
    private Date FechaAprobacionAbono;
    private String LlaveAbono;
    private long IDReserva;
    private String Banco;
    private String NroConsignacion;
    private String ObservacionesAbono;

    public  Abono(String json) {
        if (json!= null && !json.isEmpty()) {
            try {
                GsonBuilder builder = new GsonBuilder();
                builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Gson gson = builder.create();

                Abono element = gson.fromJson(json, Abono.class);

                this.IdAbono = element.IdAbono;
                this.FechaAbono = element.FechaAbono;
                this.ValorAbono = element.ValorAbono;
                this.ComprobanteAbono = element.ComprobanteAbono;
                this.EstadoAbono = element.EstadoAbono;
                this.UsuarioAprobacionAbono = element.UsuarioAprobacionAbono;
                this.FechaAprobacionAbono = element.FechaAprobacionAbono;
                this.LlaveAbono = element.LlaveAbono;
                this.IDReserva = element.IDReserva;
                this.Banco = element.Banco;
                this.NroConsignacion = element.NroConsignacion;
                this.ObservacionesAbono = element.ObservacionesAbono;

            } catch (JsonSyntaxException ex) {
                Log.e("Abono.json", ex.toString());
            }
        }
    }

    public long getIdAbono() {
        return IdAbono;
    }

    public void setIdAbono(long idAbono) {
        IdAbono = idAbono;
    }

    public Date getFechaAbono() {
        return FechaAbono;
    }

    public void setFechaAbono(Date fechaAbono) {
        FechaAbono = fechaAbono;
    }

    public long getValorAbono() {
        return ValorAbono;
    }

    public void setValorAbono(long valorAbono) {
        ValorAbono = valorAbono;
    }

    public String getComprobanteAbono() {
        return ComprobanteAbono;
    }

    public void setComprobanteAbono(String comprobanteAbono) {
        ComprobanteAbono = comprobanteAbono;
    }

    public int getEstadoAbono() {
        return EstadoAbono;
    }

    public void setEstadoAbono(int estadoAbono) {
        EstadoAbono = estadoAbono;
    }

    public String getUsuarioAprobacionAbono() {
        return UsuarioAprobacionAbono;
    }

    public void setUsuarioAprobacionAbono(String usuarioAprobacionAbono) {
        UsuarioAprobacionAbono = usuarioAprobacionAbono;
    }

    public Date getFechaAprobacionAbono() {
        return FechaAprobacionAbono;
    }

    public void setFechaAprobacionAbono(Date fechaAprobacionAbono) {
        FechaAprobacionAbono = fechaAprobacionAbono;
    }

    public String getLlaveAbono() {
        return LlaveAbono;
    }

    public void setLlaveAbono(String llaveAbono) {
        LlaveAbono = llaveAbono;
    }

    public long getIDReserva() {
        return IDReserva;
    }

    public void setIDReserva(long IDReserva) {
        this.IDReserva = IDReserva;
    }

    public String getBanco() {
        return Banco;
    }

    public void setBanco(String banco) {
        Banco = banco;
    }

    public String getNroConsignacion() {
        return NroConsignacion;
    }

    public void setNroConsignacion(String nroConsignacion) {
        NroConsignacion = nroConsignacion;
    }

    public String getObservacionesAbono() {
        return ObservacionesAbono;
    }

    public void setObservacionesAbono(String observacionesAbono) {
        ObservacionesAbono = observacionesAbono;
    }
}
