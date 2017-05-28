package car.gov.co.carserviciociudadano.denunciaambiental.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/**
 * Created by Olger on 26/05/2017.
 */

public class ArchivoAdjunto {
 /*   private String Descripcion;
    private String DirLocal;
    private String Nombre;
    private String Size;

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getDirLocal() {
        return DirLocal;
    }

    public void setDirLocal(String dirLocal) {
        DirLocal = dirLocal;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public  ArchivoAdjunto() {
    }
    public  ArchivoAdjunto(String dirLocal,String nombre) {
        this.DirLocal = dirLocal;
        this.Nombre = nombre;
    }
    public  ArchivoAdjunto(String json) {
        if (json!= null && !json.isEmpty()) {
            try {
                GsonBuilder builder = new GsonBuilder();
                builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Gson gson = builder.create();
                ArchivoAdjunto element = gson.fromJson(json, ArchivoAdjunto.class);
                this.Nombre = element.getNombre();
                this.DirLocal = element.getDirLocal();
                this.Descripcion = element.getDescripcion();
                this.Size = element.getSize();

            } catch (JsonSyntaxException ex) {
                Log.e("ArchivoAdjunto.json", ex.toString());
            }
        }
    }
*/
}
