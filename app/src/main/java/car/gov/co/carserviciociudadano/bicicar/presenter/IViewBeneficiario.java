package car.gov.co.carserviciociudadano.bicicar.presenter;

import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public interface IViewBeneficiario {
    void onSuccess(Beneficiario beneficiario);
    void onError(ErrorApi errorApi);
    void onSuccessRecordarClave(String mensaje);
    void onErrorRecordarClave(ErrorApi error);
}
