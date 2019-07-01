package car.gov.co.carserviciociudadano.bicicar.interfaces;

import car.gov.co.carserviciociudadano.bicicar.model.Asistente;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public interface IAsistente {
    void onSuccess(Asistente asistente);
    void onError(ErrorApi error);
}
