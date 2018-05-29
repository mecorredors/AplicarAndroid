package car.gov.co.carserviciociudadano.bicicar.interfaces;


import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public interface IBeneficiario {
    void onSuccess(Beneficiario beneficiario);
    void onError(ErrorApi error);
}
