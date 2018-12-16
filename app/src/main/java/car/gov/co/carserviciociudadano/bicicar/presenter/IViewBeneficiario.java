package car.gov.co.carserviciociudadano.bicicar.presenter;

import java.util.List;

import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public interface IViewBeneficiario {
    void onSuccess(Beneficiario beneficiario);
    void onSuccess(List<Beneficiario> lstBeneficiarios);
    void onError(ErrorApi errorApi);
    void onErrorListarItems(ErrorApi errorApi);
    void onSuccessRecordarClave(String mensaje);
    void onErrorRecordarClave(ErrorApi error);
}
