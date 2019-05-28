package car.gov.co.carserviciociudadano.bicicar.presenter;

import java.util.List;

import car.gov.co.carserviciociudadano.bicicar.model.Colegio;
import car.gov.co.carserviciociudadano.common.IView;

public interface IViewColegio extends IView {
    void onSuccess(List<Colegio> lstColegios);
    void onSuccess(Colegio colegio);
}
