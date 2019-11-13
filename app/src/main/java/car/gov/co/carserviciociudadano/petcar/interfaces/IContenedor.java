package car.gov.co.carserviciociudadano.petcar.interfaces;

import java.util.List;

import car.gov.co.carserviciociudadano.common.IView;

import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.petcar.model.Contenedor;

/**
 * Created by apple on 9/09/18.
 */

public interface IContenedor {
    void onSuccessContenedores(List<Contenedor> lstContenedores);
    void onErrorContenedores(ErrorApi error);
}
