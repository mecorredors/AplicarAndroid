package car.gov.co.carserviciociudadano.bicicar.interfaces;

import java.util.List;

import car.gov.co.carserviciociudadano.bicicar.model.Cuenca;
import car.gov.co.carserviciociudadano.bicicar.model.Nivel;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public interface ICuenca {
    void onSuccess(List<Cuenca> lstCuencas);
    void onError(ErrorApi errorApi);
}
