package car.gov.co.carserviciociudadano.bicicar.interfaces;

import car.gov.co.carserviciociudadano.bicicar.model.Evento;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public interface IEvento {
    void onSuccess(Evento evento);
    void onSuccessModificar(Evento evento);
    void onSuccessEliminar(Evento evento);
    void onSuccessPublicoActual(Evento evento);
    void onError(ErrorApi error);
    void onErrorPublicoActual(ErrorApi error);

}
