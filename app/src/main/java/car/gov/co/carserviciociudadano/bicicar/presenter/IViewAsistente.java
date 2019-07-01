package car.gov.co.carserviciociudadano.bicicar.presenter;


import car.gov.co.carserviciociudadano.bicicar.model.Asistente;
import car.gov.co.carserviciociudadano.common.IView;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public interface IViewAsistente extends IView {
    void onSuccess(int idEvento);
    void onErrorAsistente(ErrorApi error);
}
