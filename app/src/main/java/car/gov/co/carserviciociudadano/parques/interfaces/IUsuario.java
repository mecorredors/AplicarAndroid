package car.gov.co.carserviciociudadano.parques.interfaces;

import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.ServicioReserva;
import car.gov.co.carserviciociudadano.parques.model.Usuario;


/**
 * Created by Olger on 27/11/2016.
 */

public interface IUsuario {
    void onSuccess(Usuario usuario);
    void onError(ErrorApi error);
}