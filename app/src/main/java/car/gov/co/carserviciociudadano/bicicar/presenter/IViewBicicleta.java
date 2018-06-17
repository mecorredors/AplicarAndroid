package car.gov.co.carserviciociudadano.bicicar.presenter;

import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.bicicar.model.Bicicleta;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public interface IViewBicicleta {
    void onSuccessBicicleta(Bicicleta bicicleta);
    void onErrorBicicleta(ErrorApi errorApi);
}
