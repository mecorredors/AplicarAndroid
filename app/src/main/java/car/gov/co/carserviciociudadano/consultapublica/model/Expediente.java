package car.gov.co.carserviciociudadano.consultapublica.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Olger on 29/01/2017.
 */

public class Expediente {
    private int IDExpediente;
    private int IDTipoExpediente;
    private int  IDProvincial;
    private int  IDExpedientePadre;
    private int  Cuadernos;
    private String Anexos;
    private Date FechaCreacion;
    private String  UsuarioCreacion;
    private String   TipoExpediente;
    private String   Provincia;
    private String   Integrante;
    private int      OrigenAcumulado;
    private String   IDRadicadoPrincipal;
    private String   Proceso;
    private Date  FRadicadoPrincipal;


    public  Expediente(String json) {
        if (json!= null && !json.isEmpty()) {
            try {
                GsonBuilder builder = new GsonBuilder();
                builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Gson gson = builder.create();

                Expediente element = gson.fromJson(json, Expediente.class);

                this.IDExpediente = element.IDExpediente;
                this.IDTipoExpediente = element.IDTipoExpediente;
                this.IDProvincial = element.IDProvincial;
                this.IDExpedientePadre = element.IDExpedientePadre;
                this.Cuadernos = element.Cuadernos;
                this.Anexos = element.Anexos;
                this.FechaCreacion = element.FechaCreacion;
                this.UsuarioCreacion = element.UsuarioCreacion;
                this.TipoExpediente = element.TipoExpediente;
                this.Provincia = element.Provincia;
                this.Integrante = element.Integrante;
                this.OrigenAcumulado = element.OrigenAcumulado;
                this.IDRadicadoPrincipal = element.IDRadicadoPrincipal;
                this.Proceso = element.Proceso;
                this.FRadicadoPrincipal = element.FRadicadoPrincipal;

            } catch (JsonSyntaxException ex) {
                Log.e("Expediente.json", ex.toString());
            }
        }
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

    public int getIDExpediente() {
        return IDExpediente;
    }

    public void setIDExpediente(int IDExpediente) {
        this.IDExpediente = IDExpediente;
    }

    public int getIDTipoExpediente() {
        return IDTipoExpediente;
    }

    public void setIDTipoExpediente(int IDTipoExpediente) {
        this.IDTipoExpediente = IDTipoExpediente;
    }

    public int getIDProvincial() {
        return IDProvincial;
    }

    public void setIDProvincial(int IDProvincial) {
        this.IDProvincial = IDProvincial;
    }

    public int getIDExpedientePadre() {
        return IDExpedientePadre;
    }

    public void setIDExpedientePadre(int IDExpedientePadre) {
        this.IDExpedientePadre = IDExpedientePadre;
    }

    public int getCuadernos() {
        return Cuadernos;
    }

    public void setCuadernos(int cuadernos) {
        Cuadernos = cuadernos;
    }

    public String getAnexos() {
        return Anexos;
    }

    public void setAnexos(String anexos) {
        Anexos = anexos;
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

    public String getTipoExpediente() {
        return TipoExpediente;
    }

    public void setTipoExpediente(String tipoExpediente) {
        TipoExpediente = tipoExpediente;
    }

    public String getProvincia() {
        return Provincia;
    }

    public void setProvincia(String provincia) {
        Provincia = provincia;
    }

    public String getIntegrante() {
        return Integrante;
    }

    public void setIntegrante(String integrante) {
        Integrante = integrante;
    }

    public int getOrigenAcumulado() {
        return OrigenAcumulado;
    }

    public void setOrigenAcumulado(int origenAcumulado) {
        OrigenAcumulado = origenAcumulado;
    }

    public String getIDRadicadoPrincipal() {
        return IDRadicadoPrincipal;
    }

    public void setIDRadicadoPrincipal(String IDRadicadoPrincipal) {
        this.IDRadicadoPrincipal = IDRadicadoPrincipal;
    }

    public String getProceso() {
        return Proceso;
    }

    public void setProceso(String proceso) {
        Proceso = proceso;
    }

    public Date getFRadicadoPrincipal() {
        return FRadicadoPrincipal;
    }

    public void setFRadicadoPrincipal(Date FRadicadoPrincipal) {
        this.FRadicadoPrincipal = FRadicadoPrincipal;
    }
}
