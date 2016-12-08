package car.gov.co.carserviciociudadano.parques.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/**
 * Created by Olger on 26/11/2016.
 */

public class Municipio {
    private int IDMunicipio;
    private String DetalleMunicipio;
    private int ActivoMunicipio;
    private String CodigoDane;

    public Municipio(int idMunicipio,String nombre){
        IDMunicipio = idMunicipio;
        DetalleMunicipio = nombre;
    }
    public  Municipio(String json) {
        if (json!= null && !json.isEmpty()) {
            try {
                GsonBuilder builder = new GsonBuilder();
                builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Gson gson = builder.create();

                Municipio element = gson.fromJson(json, Municipio.class);

                this.IDMunicipio = element.IDMunicipio;
                this.DetalleMunicipio = element.DetalleMunicipio;
                this.ActivoMunicipio = element.ActivoMunicipio;
                this.CodigoDane = element.CodigoDane;

            } catch (JsonSyntaxException ex) {
                Log.e("Municipio.json", ex.toString());
            }
        }
    }

    @Override
    public String  toString(){
        return this.DetalleMunicipio;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Municipio && getIDMunicipio() == ((Municipio) o).getIDMunicipio() );
    }
    public int getIDMunicipio() {
        return IDMunicipio;
    }

    public void setIDMunicipio(int IDMunicipio) {
        this.IDMunicipio = IDMunicipio;
    }

    public String getDetalleMunicipio() {
        return DetalleMunicipio;
    }

    public void setDetalleMunicipio(String detalleMunicipio) {
        DetalleMunicipio = detalleMunicipio;
    }

    public int getActivoMunicipio() {
        return ActivoMunicipio;
    }

    public void setActivoMunicipio(int activoMunicipio) {
        ActivoMunicipio = activoMunicipio;
    }

    public String getCodigoDane() {
        return CodigoDane;
    }

    public void setCodigoDane(String codigoDane) {
        CodigoDane = codigoDane;
    }
}
