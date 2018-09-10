package car.gov.co.carserviciociudadano.petcar.presenter;

import java.util.List;

import car.gov.co.carserviciociudadano.common.IView;
import car.gov.co.carserviciociudadano.petcar.model.Contenedor;

/**
 * Created by apple on 9/09/18.
 */

public interface IViewContenedor extends IView {
    void onSuccess(List<Contenedor> lstContenedores);
}
