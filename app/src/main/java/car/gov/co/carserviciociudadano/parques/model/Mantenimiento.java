package car.gov.co.carserviciociudadano.parques.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.util.Date;

/**
 * Created by Olger on 26/11/2016.
 */

public class Mantenimiento {
    private int IDMantenimiento;
    private int IDServiciosParque;
    private Date Fecha;

    public  Mantenimiento(String json) {
        if (json!= null && !json.isEmpty()) {
            try {
                GsonBuilder builder = new GsonBuilder();
                builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Gson gson = builder.create();

                Mantenimiento element = gson.fromJson(json, Mantenimiento.class);

                this.IDMantenimiento = element.IDMantenimiento;
                this.IDServiciosParque = element.IDServiciosParque;
                this.Fecha = element.Fecha;

            } catch (JsonSyntaxException ex) {
                Log.e("Mantenimiento.json", ex.toString());
            }
        }
    }
    public int getIDMantenimiento() {
        return IDMantenimiento;
    }

    public void setIDMantenimiento(int IDMantenimiento) {
        this.IDMantenimiento = IDMantenimiento;
    }

    public int getIDServiciosParque() {
        return IDServiciosParque;
    }

    public void setIDServiciosParque(int IDServiciosParque) {
        this.IDServiciosParque = IDServiciosParque;
    }

    public Date getFecha() {
        return Fecha;
    }

    public void setFecha(Date fecha) {
        Fecha = fecha;
    }
}
