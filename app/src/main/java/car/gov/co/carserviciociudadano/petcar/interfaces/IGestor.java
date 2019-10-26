package car.gov.co.carserviciociudadano.petcar.interfaces;

import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.petcar.model.Gestor;

public interface IGestor {
    void onSuccessLoging(Gestor gestor);
    void onErrorLoging(ErrorApi error);
}
