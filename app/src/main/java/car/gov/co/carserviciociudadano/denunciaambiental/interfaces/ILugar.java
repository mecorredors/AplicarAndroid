package car.gov.co.carserviciociudadano.denunciaambiental.interfaces;

import java.util.List;

import car.gov.co.carserviciociudadano.denunciaambiental.model.Lugar;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

/**
 * Created by Olger on 26/05/2017.
 */

public interface ILugar {
    void onSuccess(List<Lugar> lstLugares);
    void onError(ErrorApi errorApi);
}
