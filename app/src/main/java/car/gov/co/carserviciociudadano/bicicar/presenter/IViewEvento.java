package car.gov.co.carserviciociudadano.bicicar.presenter;


import car.gov.co.carserviciociudadano.bicicar.model.Evento;
import car.gov.co.carserviciociudadano.common.IView;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public interface IViewEvento extends IView {
    void onSuccess(Evento evento);
    void onSuccessModificar(Evento evento);
    void onSuccessEliminar(Evento evento);
    void onErrorEvento(ErrorApi error);
}
