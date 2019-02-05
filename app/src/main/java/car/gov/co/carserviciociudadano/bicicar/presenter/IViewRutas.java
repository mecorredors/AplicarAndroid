package car.gov.co.carserviciociudadano.bicicar.presenter;

import java.util.List;

import car.gov.co.carserviciociudadano.bicicar.model.Ruta;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public interface IViewRutas {
    void onSuccess(List<Ruta> lstRutas);
    void onSuccess(Ruta ruta);
    void onError(ErrorApi errorApi);
}
