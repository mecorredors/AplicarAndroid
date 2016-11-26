package car.gov.co.carserviciociudadano.parques.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/**
 * Created by Olger on 26/11/2016.
 */

public class ParametroReserva {
    private String Llave;
    private String Valor;

    public  ParametroReserva(String json) {
        if (json!= null && !json.isEmpty()) {
            try {
                GsonBuilder builder = new GsonBuilder();
                builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Gson gson = builder.create();

                ParametroReserva element = gson.fromJson(json, ParametroReserva.class);

                this.Llave = element.Llave;
                this.Valor = element.Valor;

            } catch (JsonSyntaxException ex) {
                Log.e("ParametroReserva.json", ex.toString());
            }
        }
    }
    public String getLlave() {
        return Llave;
    }

    public void setLlave(String llave) {
        Llave = llave;
    }

    public String getValor() {
        return Valor;
    }

    public void setValor(String valor) {
        Valor = valor;
    }
}
