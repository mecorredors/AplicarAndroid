package car.gov.co.carserviciociudadano.parques.interfaces;

import java.util.List;

import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.ServicioParque;


/**
 * Created by Olger on 27/11/2016.
 */

public interface IServicioParque {
    void onSuccess(List<ServicioParque> lista);
    void onError(ErrorApi error);
}
