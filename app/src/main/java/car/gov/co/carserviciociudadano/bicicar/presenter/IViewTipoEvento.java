package car.gov.co.carserviciociudadano.bicicar.presenter;

import java.util.List;

import car.gov.co.carserviciociudadano.bicicar.model.TipoEvento;
import car.gov.co.carserviciociudadano.common.IView;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public interface IViewTipoEvento extends IView {
    void onSuccessTipoEvento(List<TipoEvento> lstTiposEvento);
    void onErrorTiposEvento(ErrorApi error);
}
