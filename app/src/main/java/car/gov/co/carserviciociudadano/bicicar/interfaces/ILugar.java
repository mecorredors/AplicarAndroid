package car.gov.co.carserviciociudadano.bicicar.interfaces;

import java.util.List;

import car.gov.co.carserviciociudadano.bicicar.model.Lugar;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

/**
 * Created by Olger on 26/05/2017.
 */

public interface ILugar {
    void onSuccessMunicipios(List<Lugar> lstMunicipios);
    void onSuccessVeredas(List<Lugar> lstVeredas);

    void onErrorMunicipios(ErrorApi errorApi);
    void onErrorVeredas(ErrorApi errorApi);
}
