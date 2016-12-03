package car.gov.co.carserviciociudadano.parques.interfaces;

import java.util.List;

import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.Mantenimiento;
import car.gov.co.carserviciociudadano.parques.model.ServicioReserva;


/**
 * Created by Olger on 27/11/2016.
 */

public interface IServicioReserva {
    void onSuccess(List<ServicioReserva> lstServicioReservas);
    void onError(ErrorApi error);
}
