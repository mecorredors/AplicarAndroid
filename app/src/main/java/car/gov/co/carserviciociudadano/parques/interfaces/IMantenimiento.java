package car.gov.co.carserviciociudadano.parques.interfaces;

import java.util.List;

import car.gov.co.carserviciociudadano.parques.model.Mantenimiento;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;


/**
 * Created by Olger on 27/11/2016.
 */

public interface IMantenimiento {
    void onSuccess(List<Mantenimiento> lstMantenimientos);
    void onError(ErrorApi error);
}
