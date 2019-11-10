package car.gov.co.carserviciociudadano.petcar.interfaces;

import car.gov.co.carserviciociudadano.bicicar.model.RespuestaApi;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.petcar.model.Gestor;

public interface IGestor {
    void onSuccessLoging(Gestor gestor);
    void onErrorLoging(ErrorApi error);
    void onSuccessCambiarClave(Gestor gestor);
    void onErrorCambiarClave(ErrorApi error);
    void onSuccessRercordarClave(RespuestaApi respuestaApi);
    void onErrorRecordarClave(ErrorApi error);

}
