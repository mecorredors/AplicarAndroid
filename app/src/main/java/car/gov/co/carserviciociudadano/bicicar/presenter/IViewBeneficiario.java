package car.gov.co.carserviciociudadano.bicicar.presenter;

import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public interface IViewBeneficiario {
    void onSuccessLogin(Beneficiario beneficiario);
    void onErrorLogin(ErrorApi errorApi);
}
