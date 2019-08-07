package car.gov.co.carserviciociudadano.bicicar.presenter;

import java.util.List;

import car.gov.co.carserviciociudadano.bicicar.model.Cuenca;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public interface IViewCuenca {
    void onSuccessCuencas(List<Cuenca> lstCuencas);
    void onErrorCuencas(ErrorApi errorApi);
}
