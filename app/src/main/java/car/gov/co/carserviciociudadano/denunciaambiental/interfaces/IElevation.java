package car.gov.co.carserviciociudadano.denunciaambiental.interfaces;

import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

/**
 * Created by Olger on 29/05/2017.
 */

public interface IElevation {
    void onResult(double elevation);
    void onErrror(ErrorApi errorApi);
}
