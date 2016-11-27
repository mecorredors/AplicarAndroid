package car.gov.co.carserviciociudadano.parques.interfaces;

import java.util.List;

import car.gov.co.carserviciociudadano.parques.model.Abono;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.Parque;

/**
 * Created by Olger on 26/11/2016.
 */

public interface IAbono {
    void onSuccess(List<Abono> lstAbono);
    void onSuccess();
    void onError(ErrorApi error);
}
