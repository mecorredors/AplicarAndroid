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

public class Documento {
    private String NumeroDocumento;
    private Date Fecha;
    private String TipoDocumento;
    private String Documento;
    private String Oficina;
    private String ElaboradoPor;
    private int IDTipoDocumento;
    private String Comentarios;
    private String URLDocumento;
    private int ConsecutivoMigrado;

    public  Documento(String json) {
        if (json!= null && !json.isEmpty()) {
            try {
                GsonBuilder builder = new GsonBuilder();
                builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Gson gson = builder.create();

                Documento element = gson.fromJson(json, Documento.class);

                this.NumeroDocumento = element.NumeroDocumento;
                this.Fecha = element.Fecha;
                this.TipoDocumento = element.TipoDocumento;
                this.Documento = element.Documento;
                this.Oficina = element.Oficina;
                this.ElaboradoPor = element.ElaboradoPor;
                this.IDTipoDocumento = element.IDTipoDocumento;
                this.Comentarios = element.Comentarios;
                this.URLDocumento = element.URLDocumento;
                this.ConsecutivoMigrado = element.ConsecutivoMigrado;


            } catch (JsonSyntaxException ex) {
                Log.e("Documento.json", ex.toString());
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
    public String getNumeroDocumento() {
        return NumeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        NumeroDocumento = numeroDocumento;
    }

    public Date getFecha() {
        return Fecha;
    }

    public void setFecha(Date fecha) {
        Fecha = fecha;
    }

    public String getTipoDocumento() {
        return TipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        TipoDocumento = tipoDocumento;
    }

    public String getDocumento() {
        return Documento;
    }

    public void setDocumento(String documento) {
        Documento = documento;
    }

    public String getOficina() {
        return Oficina;
    }

    public void setOficina(String oficina) {
        Oficina = oficina;
    }

    public String getElaboradoPor() {
        return ElaboradoPor;
    }

    public void setElaboradoPor(String elaboradoPor) {
        ElaboradoPor = elaboradoPor;
    }

    public int getIDTipoDocumento() {
        return IDTipoDocumento;
    }

    public void setIDTipoDocumento(int IDTipoDocumento) {
        this.IDTipoDocumento = IDTipoDocumento;
    }

    public String getComentarios() {
        return Comentarios;
    }

    public void setComentarios(String comentarios) {
        Comentarios = comentarios;
    }

    public String getURLDocumento() {
        return URLDocumento;
    }

    public void setURLDocumento(String URLDocumento) {
        this.URLDocumento = URLDocumento;
    }

    public int getConsecutivoMigrado() {
        return ConsecutivoMigrado;
    }

    public void setConsecutivoMigrado(int consecutivoMigrado) {
        ConsecutivoMigrado = consecutivoMigrado;
    }
}
