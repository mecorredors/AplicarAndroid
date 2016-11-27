package car.gov.co.carserviciociudadano.parques.interfaces;

import java.util.List;

import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.ParametroReserva;

/**
 * Created by Olger on 26/11/2016.
 */

public interface IParametro {
    void onSuccess(List<ParametroReserva> lstParametros);
    void onError(ErrorApi error);
}
