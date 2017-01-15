package car.gov.co.carserviciociudadano.parques.businessrules;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.parques.dataaccess.Parques;
import car.gov.co.carserviciociudadano.parques.dataaccess.ServiciosParque;
import car.gov.co.carserviciociudadano.parques.interfaces.IServicioParque;
import car.gov.co.carserviciociudadano.parques.model.Parque;
import car.gov.co.carserviciociudadano.parques.model.ServicioParque;
import car.gov.co.carserviciociudadano.parques.model.ServicioReserva;

/**
 * Created by Olger on 02/12/2016.
 */

public class BRServiciosParques {
    public void list(final IServicioParque iServicioParque, int idParque  )
    {

        String tag = ServiciosParque.TAG+idParque;

        ServiciosParque serviciosParque = new ServiciosParque();

        if (Utils.existeCache(tag) && !Utils.cacheExpiro(Enumerator.CacheNumDias.SERVICIOS_PARQUE,tag)) {
            try {
                Type resultType = new TypeToken<List<ServicioParque>>() {}.getType();
                List<ServicioParque> lstServiciosParques =  Reservoir.get(tag, resultType);
                iServicioParque.onSuccess( lstServiciosParques);
            } catch (Exception e) {
                serviciosParque.list(iServicioParque,idParque);
            }
        }else {
            serviciosParque.list(iServicioParque,idParque);
        }

    }
}
