package car.gov.co.carserviciociudadano.bicicar.presenter;

import java.util.List;

import car.gov.co.carserviciociudadano.bicicar.model.Colegio;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public interface IViewColegio  {
    void onSuccessColegios(List<Colegio> lstColegios);
    void onSuccess(Colegio colegio);
    void onErrorColegios(ErrorApi error);
}
