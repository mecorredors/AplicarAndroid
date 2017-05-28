package car.gov.co.carserviciociudadano.parques.presenter;

import java.util.List;

import car.gov.co.carserviciociudadano.common.IView;
import car.gov.co.carserviciociudadano.parques.model.Parque;

/**
 * Created by Olger on 01/05/2017.
 */

public interface IViewParque extends IView {
    void onSuccess(List<Parque> lstParques);
}
