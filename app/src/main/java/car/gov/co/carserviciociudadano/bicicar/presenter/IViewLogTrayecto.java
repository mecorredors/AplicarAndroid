package car.gov.co.carserviciociudadano.bicicar.presenter;

import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.bicicar.model.LogTrayecto;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public interface IViewLogTrayecto {
    void onSuccessLogTrayecto();
    void onErrorLogTrayecto(ErrorApi errorApi);
}
