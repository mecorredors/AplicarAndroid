package car.gov.co.carserviciociudadano.petcar.presenter;

import java.util.List;

import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.petcar.model.Municipio;

/**
 * Created by apple on 23/09/18.
 */

public interface IViewMunicipios {
    void onSuccess(List<Municipio> lstMunicipios);
    void onError(ErrorApi errorApi);
}
