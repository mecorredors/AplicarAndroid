package car.gov.co.carserviciociudadano.bicicar.interfaces;

import java.util.List;

import car.gov.co.carserviciociudadano.bicicar.model.TipoEvento;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public interface ITipoEvento {
    void onSuccess(List<TipoEvento> lstTiposEvento);
    void onError(ErrorApi error);
}
