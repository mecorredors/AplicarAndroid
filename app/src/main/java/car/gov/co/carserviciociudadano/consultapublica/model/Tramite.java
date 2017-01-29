package car.gov.co.carserviciociudadano.consultapublica.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Olger on 28/01/2017.
 */

public class Tramite {
    private Radicado RadicadoItem;
    private String OficinaResponsable;
    List<RadicadosDigitalesRespuesta> RadicadosDigitalesRespuesta;
    private String Remitente;


    public  Tramite(String json) {
        if (json!= null && !json.isEmpty()) {
            try {
                GsonBuilder builder = new GsonBuilder();
                builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Gson gson = builder.create();

                Tramite element = gson.fromJson(json, Tramite.class);

                this.RadicadoItem = element.RadicadoItem;
                this.OficinaResponsable = element.OficinaResponsable;
                this.RadicadosDigitalesRespuesta = element.RadicadosDigitalesRespuesta;
                this.Remitente = element.Remitente;


            } catch (JsonSyntaxException ex) {
                Log.e("Tramite.json", ex.toString());
            }
        }
    }

    public Radicado getRadicadoItem() {
        return RadicadoItem;
    }

    public void setRadicadoItem(Radicado radicadoItem) {
        RadicadoItem = radicadoItem;
    }

    public String getOficinaResponsable() {
        return OficinaResponsable;
    }

    public void setOficinaResponsable(String oficinaResponsable) {
        OficinaResponsable = oficinaResponsable;
    }

    public List<car.gov.co.carserviciociudadano.consultapublica.model.RadicadosDigitalesRespuesta> getRadicadosDigitalesRespuesta() {
        return RadicadosDigitalesRespuesta;
    }

    public void setRadicadosDigitalesRespuesta(List<car.gov.co.carserviciociudadano.consultapublica.model.RadicadosDigitalesRespuesta> radicadosDigitalesRespuesta) {
        RadicadosDigitalesRespuesta = radicadosDigitalesRespuesta;
    }

    public String getRemitente() {
        return Remitente;
    }

    public void setRemitente(String remitente) {
        Remitente = remitente;
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
}
