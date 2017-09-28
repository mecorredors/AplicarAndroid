package car.gov.co.carserviciociudadano.parques.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.util.Date;

/**
 * Created by Olger on 26/11/2016.
 */

public class DetalleReserva {
    private long IDReserva;
    private int CantidadReserva;
    private int EstadoReserva;
    private String EstadoNombre;
    private Date FechaInicialReserva;
    private Date FechaFinalReserva;
    private Date FechaSistemaReserva;
    private long PrecioReserva;
    private long TotalValorReserva;
    private int ImpuestoReserva;
    private String NombreServicio;
    private String NombreParque;

    public static final String ID_RESERVA = "IDReserva";
    public static final String VALOR_TOTAL = "ValorTotal";

    public  DetalleReserva(String json) {
        if (json!= null && !json.isEmpty()) {
            try {
                GsonBuilder builder = new GsonBuilder();
                builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Gson gson = builder.create();

                DetalleReserva element = gson.fromJson(json, DetalleReserva.class);

                this.IDReserva = element.IDReserva;
                this.CantidadReserva = element.CantidadReserva;
                this.EstadoReserva = element.EstadoReserva;
                this.EstadoNombre = element.EstadoNombre;
                this.FechaInicialReserva = element.FechaInicialReserva;
                this.FechaFinalReserva = element.FechaFinalReserva;
                this.FechaSistemaReserva = element.FechaSistemaReserva;
                this.PrecioReserva = element.PrecioReserva;
                this.TotalValorReserva = element.TotalValorReserva;
                this.ImpuestoReserva = element.ImpuestoReserva;
                this.NombreServicio = element.NombreServicio;
                this.NombreParque = element.NombreParque;

            } catch (JsonSyntaxException ex) {
                Log.e("DetalleReserva.json", ex.toString());
            }
        }
    }
    public long getIDReserva() {
        return IDReserva;
    }

    public void setIDReserva(long IDReserva) {
        this.IDReserva = IDReserva;
    }

    public int getCantidadReserva() {
        return CantidadReserva;
    }

    public void setCantidadReserva(int cantidadReserva) {
        CantidadReserva = cantidadReserva;
    }

    public int getEstadoReserva() {
        return EstadoReserva;
    }

    public void setEstadoReserva(int estadoReserva) {
        EstadoReserva = estadoReserva;
    }

    public String getEstadoNombre() {
        return EstadoNombre;
    }

    public void setEstadoNombre(String estadoNombre) {
        EstadoNombre = estadoNombre;
    }

    public Date getFechaInicialReserva() {
        return FechaInicialReserva;
    }

    public void setFechaInicialReserva(Date fechaInicialReserva) {
        FechaInicialReserva = fechaInicialReserva;
    }

    public Date getFechaFinalReserva() {
        return FechaFinalReserva;
    }

    public void setFechaFinalReserva(Date fechaFinalReserva) {
        FechaFinalReserva = fechaFinalReserva;
    }

    public Date getFechaSistemaReserva() {
        return FechaSistemaReserva;
    }

    public void setFechaSistemaReserva(Date fechaSistemaReserva) {
        FechaSistemaReserva = fechaSistemaReserva;
    }

    public long getPrecioReserva() {
        return PrecioReserva;
    }

    public void setPrecioReserva(long precioReserva) {
        PrecioReserva = precioReserva;
    }

    public long getTotalValorReserva() {
        return TotalValorReserva;
    }

    public void setTotalValorReserva(long totalValorReserva) {
        TotalValorReserva = totalValorReserva;
    }

    public int getImpuestoReserva() {
        return ImpuestoReserva;
    }

    public void setImpuestoReserva(int impuestoReserva) {
        ImpuestoReserva = impuestoReserva;
    }

    public String getNombreServicio() {
        return NombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        NombreServicio = nombreServicio;
    }

    public String getNombreParque() {
        return NombreParque;
    }

    public void setNombreParque(String nombreParque) {
        NombreParque = nombreParque;
    }
}
