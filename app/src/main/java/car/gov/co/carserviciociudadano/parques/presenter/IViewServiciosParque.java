package car.gov.co.carserviciociudadano.parques.presenter;

import java.util.List;

import car.gov.co.carserviciociudadano.parques.model.ServicioParque;

/**
 * Created by Olger on 01/05/2017.
 */

public interface IViewServiciosParque extends IView {
    void onSuccess(List<ServicioParque> lista);
}
