package car.gov.co.carserviciociudadano.parques.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Olger on 26/11/2016.
 */

public class ServicioReserva {

    private int IDServiciosReserva;
    private int IDServiciosParque;
    private long IDReserva;
    private int CantidadReserva;
    private int EstadoReserva;
    private int IDParque;
    private String  OrigenReserva;
    //datos canasta
    private int IDUsuario;
    private Date FechaInicialReserva;
    private Date FechaFinalReserva;
    private long TotalValorReserva;
    private long PrecioReserva;
    private int ImpuestoReserva;
    private String ClaveUsuario;
    private String Login;


    public ServicioReserva(){}

    public  ServicioReserva(String json) {
        if (json!= null && !json.isEmpty()) {
            try {
                GsonBuilder builder = new GsonBuilder();
                builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Gson gson = builder.create();

                ServicioReserva element = gson.fromJson(json, ServicioReserva.class);

                this.IDServiciosReserva = element.IDServiciosReserva;
                this.IDServiciosParque = element.IDServiciosParque;
                this.IDReserva = element.IDReserva;
                this.CantidadReserva = element.CantidadReserva;
                this.EstadoReserva = element.EstadoReserva;
                this.IDUsuario = element.IDUsuario;
                this.FechaInicialReserva = element.FechaInicialReserva;
                this.FechaFinalReserva = element.FechaFinalReserva;
                this.TotalValorReserva = element.TotalValorReserva;
                this.PrecioReserva = element.PrecioReserva;
                this.ImpuestoReserva = element.ImpuestoReserva;
                this.IDParque = element.IDParque;
                this.Login = element.Login;
                this.ClaveUsuario = element.ClaveUsuario;


            } catch (JsonSyntaxException ex) {
                Log.e("ServicioReserva.json", ex.toString());
            }
        }
    }

    public JSONObject toJSONObject(){

        Gson gson = new Gson();
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(gson.toJson(this));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return jsonObject;
    }

    public int getIDServiciosReserva() {
        return IDServiciosReserva;
    }

    public void setIDServiciosReserva(int IDServiciosReserva) {
        this.IDServiciosReserva = IDServiciosReserva;
    }

    public int getIDServiciosParque() {
        return IDServiciosParque;
    }

    public void setIDServiciosParque(int IDServiciosParque) {
        this.IDServiciosParque = IDServiciosParque;
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

    public int getIDUsuario() {
        return IDUsuario;
    }

    public void setIDUsuario(int IDUsuario) {
        this.IDUsuario = IDUsuario;
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

    public long getTotalValorReserva() {
        return TotalValorReserva;
    }

    public void setTotalValorReserva(long totalValorReserva) {
        TotalValorReserva = totalValorReserva;
    }

    public long getPrecioReserva() {
        return PrecioReserva;
    }

    public void setPrecioReserva(long precioReserva) {
        PrecioReserva = precioReserva;
    }

    public int getImpuestoReserva() {
        return ImpuestoReserva;
    }

    public void setImpuestoReserva(int impuestoReserva) {
        ImpuestoReserva = impuestoReserva;
    }

    public int getIDParque() {
        return IDParque;
    }

    public void setIDParque(int IDParque) {
        this.IDParque = IDParque;
    }

    public String getClaveUsuario() {
        return ClaveUsuario;
    }

    public void setClaveUsuario(String claveUsuario) {
        ClaveUsuario = claveUsuario;
    }

    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        Login = login;
    }

    public String getOrigenReserva() {
        return OrigenReserva;
    }

    public void setOrigenReserva(String origenReserva) {
        OrigenReserva = origenReserva;
    }
}
