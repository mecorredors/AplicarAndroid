package car.gov.co.carserviciociudadano.bicicar.interfaces;

import java.util.List;

import car.gov.co.carserviciociudadano.bicicar.model.Nivel;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public interface INivel {
    void onSuccess(List<Nivel> lstNiveles);
    void onError(ErrorApi errorApi);
}
