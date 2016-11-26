package car.gov.co.carserviciociudadano.parques.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

/**
 * Created by Olger on 26/11/2016.
 */

public class Banco {
    private int IDBanco;
    private String DetalleBanco;

    public  Banco(String json) {
        if (json!= null && !json.isEmpty()) {
            try {
                GsonBuilder builder = new GsonBuilder();
                builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                Gson gson = builder.create();

                Banco element = gson.fromJson(json, Banco.class);

                this.IDBanco = element.IDBanco;
                this.DetalleBanco = element.DetalleBanco;

            } catch (JsonSyntaxException ex) {
                Log.e("Banco.json", ex.toString());
            }
        }
    }
    public int getIDBanco() {
        return IDBanco;
    }

    public void setIDBanco(int IDBanco) {
        this.IDBanco = IDBanco;
    }

    public String getDetalleBanco() {
        return DetalleBanco;
    }

    public void setDetalleBanco(String detalleBanco) {
        DetalleBanco = detalleBanco;
    }
}
