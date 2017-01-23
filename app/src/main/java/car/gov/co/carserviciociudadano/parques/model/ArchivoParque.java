package car.gov.co.carserviciociudadano.parques.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/**
 * Created by Olger on 26/11/2016.
 */

public class ArchivoParque {
    private int IDArchivoParque;
    private String TituloArchivo;
    private String ObservacionesArchivo;
    private String ArchivoParque1;
    private int IDTipoArchivo;
    private int IDParque;
    private int ActivoArchivoParque;

    public static final String PATH = "PATH";

    public ArchivoParque(){}

    public  ArchivoParque(String json) {
        if (json!= null && !json.isEmpty()) {
            try {
                GsonBuilder builder = new GsonBuilder();
                builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Gson gson = builder.create();

                ArchivoParque element = gson.fromJson(json, ArchivoParque.class);

                this.IDArchivoParque = element.IDArchivoParque;
                this.TituloArchivo = element.TituloArchivo;
                this.ObservacionesArchivo = element.ObservacionesArchivo;
                this.ArchivoParque1 = element.ArchivoParque1;
                this.IDTipoArchivo = element.IDTipoArchivo;
                this.IDParque = element.IDParque;
                this.ActivoArchivoParque = element.ActivoArchivoParque;

            } catch (JsonSyntaxException ex) {
                Log.e("ArchivoParque.json", ex.toString());
            }
        }
    }

    public int getIDArchivoParque() {
        return IDArchivoParque;
    }

    public void setIDArchivoParque(int IDArchivoParque) {
        this.IDArchivoParque = IDArchivoParque;
    }

    public String getTituloArchivo() {
        return TituloArchivo;
    }

    public void setTituloArchivo(String tituloArchivo) {
        TituloArchivo = tituloArchivo;
    }

    public String getObservacionesArchivo() {
        return ObservacionesArchivo;
    }

    public void setObservacionesArchivo(String observacionesArchivo) {
        ObservacionesArchivo = observacionesArchivo;
    }

    public String getArchivoParque() {
        return ArchivoParque1;
    }

    public void setArchivoParque(String archivoParque) {
        ArchivoParque1 = archivoParque;
    }

    public int getIDTipoArchivo() {
        return IDTipoArchivo;
    }

    public void setIDTipoArchivo(int IDTipoArchivo) {
        this.IDTipoArchivo = IDTipoArchivo;
    }

    public int getIDParque() {
        return IDParque;
    }

    public void setIDParque(int IDParque) {
        this.IDParque = IDParque;
    }

    public int getActivoArchivoParque() {
        return ActivoArchivoParque;
    }

    public void setActivoArchivoParque(int activoArchivoParque) {
        ActivoArchivoParque = activoArchivoParque;
    }
}
