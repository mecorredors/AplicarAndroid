package car.gov.co.carserviciociudadano.bicicar.interfaces;

import car.gov.co.carserviciociudadano.bicicar.model.Bicicleta;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public interface IBicicleta {
    void onSuccessBicicleta(Bicicleta bicicleta);
    void onErrorBicicleta(ErrorApi error);
}
