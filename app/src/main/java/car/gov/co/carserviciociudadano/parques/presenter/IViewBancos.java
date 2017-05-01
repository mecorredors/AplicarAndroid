package car.gov.co.carserviciociudadano.parques.presenter;

import java.util.List;

import car.gov.co.carserviciociudadano.parques.model.Banco;

/**
 * Created by Olger on 01/05/2017.
 */

public interface IViewBancos extends IView {
    void onSuccess(List<Banco> lstBanco);
}
