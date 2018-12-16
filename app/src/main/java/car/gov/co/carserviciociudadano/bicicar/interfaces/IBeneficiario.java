package car.gov.co.carserviciociudadano.bicicar.interfaces;


import java.util.List;

import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public interface IBeneficiario {
    void onSuccess(Beneficiario beneficiario);
    void onSuccess(List<Beneficiario> lstBeneficiarios, boolean datosServer);
    void onError(ErrorApi error);
    void onErrorListarItems(ErrorApi error);
    void onSuccessRecordarClave(String mensaje);
    void onErrorRecordarClave(ErrorApi error);
}
