package car.gov.co.carserviciociudadano.parques.interfaces;

import java.util.List;

import car.gov.co.carserviciociudadano.parques.model.DetalleReserva;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;


/**
 * Created by Olger on 27/11/2016.
 */

public interface IDetalleReserva {
    void onSuccess(List<DetalleReserva> lista);
    void onError(ErrorApi error);
}
