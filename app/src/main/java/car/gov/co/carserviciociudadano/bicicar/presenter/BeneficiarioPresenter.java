package car.gov.co.carserviciociudadano.bicicar.presenter;

import car.gov.co.carserviciociudadano.bicicar.dataaccess.Beneficiarios;
import car.gov.co.carserviciociudadano.bicicar.interfaces.IBeneficiario;
import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class BeneficiarioPresenter {
    IViewBeneficiario iViewBeneficiario;

    public  BeneficiarioPresenter(IViewBeneficiario iViewBeneficiario){
        this.iViewBeneficiario = iViewBeneficiario;
    }

    public  void login(String numeroId, String claveApp){
        new Beneficiarios().login(numeroId, claveApp, new IBeneficiario() {
            @Override
            public void onSuccess(Beneficiario beneficiario) {
                iViewBeneficiario.onSuccessLogin(beneficiario);
            }

            @Override
            public void onError(ErrorApi error) {
                iViewBeneficiario.onErrorLogin(error);
            }
        });
    }
}
