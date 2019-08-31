package car.gov.co.carserviciociudadano.bicicar.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/**
 * Created by Olger on 26/05/2017.
 */

public class Lugar {
    private String IDLugar;
    private String IDLugarPadre;
    private String Nombre;
    private String NombreLugarPadre;
    private int TipoLugar;


    public String getIDLugar() {
        return IDLugar;
    }

    public void setIDLugar(String IDLugar) {
        this.IDLugar = IDLugar;
    }

    public String getIDLugarPadre() {
        return IDLugarPadre;
    }

    public void setIDLugarPadre(String IDLugarPadre) {
        this.IDLugarPadre = IDLugarPadre;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getNombreLugarPadre() {
        return NombreLugarPadre;
    }

    public void setNombreLugarPadre(String nombreLugarPadre) {
        NombreLugarPadre = nombreLugarPadre;
    }

    public int getTipoLugar() {
        return TipoLugar;
    }

    public void setTipoLugar(int tipoLugar) {
        TipoLugar = tipoLugar;
    }

    public Lugar(String idLugar, String nombre){
        this.IDLugar = idLugar;
        this.Nombre = nombre;
        this.IDLugarPadre = "";
        this.NombreLugarPadre = "";
    }
    public Lugar(String json) {
        if (json!= null && !json.isEmpty()) {
            try {
                GsonBuilder builder = new GsonBuilder();
                builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Gson gson = builder.create();
                Lugar element = gson.fromJson(json, Lugar.class);
                this.IDLugar = element.getIDLugar();
                this.IDLugarPadre = element.getIDLugarPadre();
                this.Nombre = element.getNombre();
                this.NombreLugarPadre = element.getNombreLugarPadre();
                this.TipoLugar = element.getTipoLugar();

            } catch (JsonSyntaxException ex) {
                Log.e("Lugar.json", ex.toString());
            }
        }
    }
    @Override
    public String  toString(){
        return this.Nombre;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Lugar && getIDLugar() == ((Lugar) o).getIDLugar() );
    }
}
