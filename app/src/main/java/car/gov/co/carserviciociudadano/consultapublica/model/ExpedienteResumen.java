package car.gov.co.carserviciociudadano.consultapublica.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


/**
 * Created by Olger on 29/01/2017.
 */

public class ExpedienteResumen {
    private int IDExpediente;
    private int IDProceso;
    private String Proceso;
    private String FechaCreacion;
    private String ExpedientePadre;
    private String IntegrantesTitulo;
    private String Cuadernos;
    private String Anexos;
    private String TituloProceso;
    private String VigenciaProceso;
    private boolean NoManejaPredio;
    private String CostoProyecto;
    private String UltimoActoAdmin;
    private String Actividad;
    private List<Predio> Predios;
    private List<Integrante> Integrantes;
    private String TituloPredio;
    private String DireccionPredio;

    public  ExpedienteResumen(String json) {
        if (json!= null && !json.isEmpty()) {
            try {
                GsonBuilder builder = new GsonBuilder();
                builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Gson gson = builder.create();

                ExpedienteResumen element = gson.fromJson(json, ExpedienteResumen.class);

                this.IDExpediente = element.IDExpediente;
                this.IDProceso = element.IDProceso;
                this.Proceso = element.Proceso;
                this.FechaCreacion = element.FechaCreacion;
                this.ExpedientePadre = element.ExpedientePadre;
                this.IntegrantesTitulo = element.IntegrantesTitulo;
                this.Cuadernos = element.Cuadernos;
                this.Anexos = element.Anexos;
                this.TituloProceso = element.TituloProceso;
                this.VigenciaProceso = element.VigenciaProceso;
                this.NoManejaPredio = element.NoManejaPredio;
                this.CostoProyecto = element.CostoProyecto;
                this.UltimoActoAdmin = element.UltimoActoAdmin;
                this.Actividad = element.Actividad;
                this.Predios = element.Predios;
                this.Integrantes = element.Integrantes;
                this.TituloPredio = element.TituloPredio;
                this.DireccionPredio = element.DireccionPredio;


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

    public int getIDProceso() {
        return IDProceso;
    }

    public void setIDProceso(int IDProceso) {
        this.IDProceso = IDProceso;
    }

    public String getProceso() {
        return Proceso;
    }

    public void setProceso(String proceso) {
        Proceso = proceso;
    }

    public String getFechaCreacion() {
        return FechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        FechaCreacion = fechaCreacion;
    }

    public String getExpedientePadre() {
        return ExpedientePadre;
    }

    public void setExpedientePadre(String expedientePadre) {
        ExpedientePadre = expedientePadre;
    }

    public String getIntegrantesTitulo() {
        return IntegrantesTitulo;
    }

    public void setIntegrantesTitulo(String integrantesTitulo) {
        IntegrantesTitulo = integrantesTitulo;
    }

    public String getCuadernos() {
        return Cuadernos;
    }

    public void setCuadernos(String cuadernos) {
        Cuadernos = cuadernos;
    }

    public String getAnexos() {
        return Anexos;
    }

    public void setAnexos(String anexos) {
        Anexos = anexos;
    }

    public String getTituloProceso() {
        return TituloProceso;
    }

    public void setTituloProceso(String tituloProceso) {
        TituloProceso = tituloProceso;
    }

    public String getVigenciaProceso() {
        return VigenciaProceso;
    }

    public void setVigenciaProceso(String vigenciaProceso) {
        VigenciaProceso = vigenciaProceso;
    }

    public boolean isNoManejaPredio() {
        return NoManejaPredio;
    }

    public void setNoManejaPredio(boolean noManejaPredio) {
        NoManejaPredio = noManejaPredio;
    }

    public String getCostoProyecto() {
        return CostoProyecto;
    }

    public void setCostoProyecto(String costoProyecto) {
        CostoProyecto = costoProyecto;
    }

    public String getUltimoActoAdmin() {
        return UltimoActoAdmin;
    }

    public void setUltimoActoAdmin(String ultimoActoAdmin) {
        UltimoActoAdmin = ultimoActoAdmin;
    }

    public String getActividad() {
        return Actividad;
    }

    public void setActividad(String actividad) {
        Actividad = actividad;
    }

    public List<Predio> getPredios() {
        return Predios;
    }

    public void setPredios(List<Predio> predios) {
        Predios = predios;
    }

    public List<Integrante> getIntegrantes() {
        return Integrantes;
    }

    public void setIntegrantes(List<Integrante> integrantes) {
        Integrantes = integrantes;
    }

    public String getTituloPredio() {
        return TituloPredio;
    }

    public void setTituloPredio(String tituloPredio) {
        TituloPredio = tituloPredio;
    }

    public String getDireccionPredio() {
        return DireccionPredio;
    }

    public void setDireccionPredio(String direccionPredio) {
        DireccionPredio = direccionPredio;
    }
}
