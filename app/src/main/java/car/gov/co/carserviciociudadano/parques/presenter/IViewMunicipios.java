package car.gov.co.carserviciociudadano.parques.presenter;

import java.util.List;

import car.gov.co.carserviciociudadano.parques.model.Municipio;

/**
 * Created by Olger on 01/05/2017.
 */

public interface IViewMunicipios extends IView {
    void onSuccess(List<Municipio> lstMunicipios);
}
