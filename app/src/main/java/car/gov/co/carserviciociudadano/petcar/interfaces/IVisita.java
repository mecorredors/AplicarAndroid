package car.gov.co.carserviciociudadano.petcar.interfaces;

import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.petcar.model.MaterialRecogido;
import car.gov.co.carserviciociudadano.petcar.model.Visita;

/**
 * Created by apple on 9/09/18.
 */

public interface IVisita {
    void onSuccessPublicarVisita(Visita visita);
    void onErrorPublicarVisita(ErrorApi error);
}
