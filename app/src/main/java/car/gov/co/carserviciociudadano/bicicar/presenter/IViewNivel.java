package car.gov.co.carserviciociudadano.bicicar.presenter;

import java.util.List;

import car.gov.co.carserviciociudadano.bicicar.model.Nivel;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public interface IViewNivel {
    void onSuccess(List<Nivel> lstNiveles);
    void onError(ErrorApi errorApi);
}
