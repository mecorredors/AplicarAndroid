package car.gov.co.carserviciociudadano.parques.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.util.Date;

/**
 * Created by Olger on 26/11/2016.
 */

public class ServicioParque {
    //tabla servicio
    private int IDServicio;
    private String NombreServicio;
    private String  DescripcionServicio;
    private String LlaveServicio;
    private int TipoCabana;
    private String CodigoTesoreria;
    private String CodigoContabilidad;

    //tabla servicioParque
    private int IDServiciosParque;
    private long PrecioServicio;
    private int ImpuestoServicio;
    private int DescuentoServicio;
    private String LlaveServicioParque;
    private int IDParque;
    private int ActivoServicio;
    private int ActivarRangoDesactivacion;
    private Date DesactivarDesde;
    private Date DesactivarHasta;
    private long PrecioCar;

    public  ServicioParque(String json) {
        if (json != null && !json.isEmpty()) {
            try {
                GsonBuilder builder = new GsonBuilder();
                builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Gson gson = builder.create();

                ServicioParque element = gson.fromJson(json, ServicioParque.class);

                this.IDServicio = element.IDServicio;
                this.NombreServicio = element.NombreServicio;
                this.DescripcionServicio = element.DescripcionServicio;
                this.LlaveServicio = element.LlaveServicio;
                this.TipoCabana = element.TipoCabana;
                this.CodigoTesoreria = element.CodigoTesoreria;
                this.CodigoContabilidad = element.CodigoContabilidad;
                this.IDServiciosParque = element.IDServiciosParque;
                this.PrecioServicio = element.PrecioServicio;
                this.DesactivarDesde = element.DesactivarDesde;
                this.DesactivarHasta = element.DesactivarHasta;
                this.ActivarRangoDesactivacion = element.ActivarRangoDesactivacion;
                this.ImpuestoServicio = element.ImpuestoServicio;
                this.DescuentoServicio = element.DescuentoServicio;
                this.LlaveServicioParque = element.LlaveServicioParque;
                this.IDParque = element.IDParque;
                this.ActivoServicio = element.ActivoServicio;
                this.PrecioCar = element.PrecioCar;

            } catch (JsonSyntaxException ex) {
                Log.e("Parque.json", ex.toString());
            }
        }
    }

    public int getIDServicio() {
        return IDServicio;
    }

    public void setIDServicio(int IDServicio) {
        this.IDServicio = IDServicio;
    }

    public String getNombreServicio() {
        return NombreServicio;
    }

    public void setNombreServicio(String nombreServicio) {
        NombreServicio = nombreServicio;
    }

    public String getDescripcionServicio() {
        return DescripcionServicio;
    }

    public void setDescripcionServicio(String descripcionServicio) {
        DescripcionServicio = descripcionServicio;
    }

    public String getLlaveServicio() {
        return LlaveServicio;
    }

    public void setLlaveServicio(String llaveServicio) {
        LlaveServicio = llaveServicio;
    }

    public int getTipoCabana() {
        return TipoCabana;
    }

    public void setTipoCabana(int tipoCabana) {
        TipoCabana = tipoCabana;
    }

    public String getCodigoTesoreria() {
        return CodigoTesoreria;
    }

    public void setCodigoTesoreria(String codigoTesoreria) {
        CodigoTesoreria = codigoTesoreria;
    }

    public String getCodigoContabilidad() {
        return CodigoContabilidad;
    }

    public void setCodigoContabilidad(String codigoContabilidad) {
        CodigoContabilidad = codigoContabilidad;
    }

    public int getIDServiciosParque() {
        return IDServiciosParque;
    }

    public void setIDServiciosParque(int IDServiciosParque) {
        this.IDServiciosParque = IDServiciosParque;
    }

    public long getPrecioServicio() {
        return PrecioServicio;
    }

    public void setPrecioServicio(long precioServicio) {
        PrecioServicio = precioServicio;
    }

    public int getImpuestoServicio() {
        return ImpuestoServicio;
    }

    public void setImpuestoServicio(int impuestoServicio) {
        ImpuestoServicio = impuestoServicio;
    }

    public int getDescuentoServicio() {
        return DescuentoServicio;
    }

    public void setDescuentoServicio(int descuentoServicio) {
        DescuentoServicio = descuentoServicio;
    }

    public String getLlaveServicioParque() {
        return LlaveServicioParque;
    }

    public void setLlaveServicioParque(String llaveServicioParque) {
        LlaveServicioParque = llaveServicioParque;
    }

    public int getIDParque() {
        return IDParque;
    }

    public void setIDParque(int IDParque) {
        this.IDParque = IDParque;
    }

    public int getActivoServicio() {
        return ActivoServicio;
    }

    public void setActivoServicio(int activoServicio) {
        ActivoServicio = activoServicio;
    }

    public int getActivarRangoDesactivacion() {
        return ActivarRangoDesactivacion;
    }

    public void setActivarRangoDesactivacion(int activarRangoDesactivacion) {
        ActivarRangoDesactivacion = activarRangoDesactivacion;
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

    public long getPrecioCar() {
        return PrecioCar;
    }

    public void setPrecioCar(long precioCar) {
        PrecioCar = precioCar;
    }
}
