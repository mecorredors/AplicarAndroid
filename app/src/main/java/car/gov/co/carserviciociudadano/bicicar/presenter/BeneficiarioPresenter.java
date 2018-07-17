package car.gov.co.carserviciociudadano.bicicar.presenter;

import car.gov.co.carserviciociudadano.bicicar.dataaccess.Beneficiarios;
import car.gov.co.carserviciociudadano.bicicar.interfaces.IBeneficiario;
import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class BeneficiarioPresenter implements IBeneficiario {
    IViewBeneficiario iViewBeneficiario;

    public  BeneficiarioPresenter(IViewBeneficiario iViewBeneficiario){
        this.iViewBeneficiario = iViewBeneficiario;
    }

    public  void login(String numeroId, String claveApp){
        new Beneficiarios().login(numeroId, claveApp, this);
    }

    public  void obtenerItem(String serial, String rin){
        new Beneficiarios().obtenerItem(serial, rin, this);
    }

    public  void recordarClave(String numeroId, String email){
        Beneficiario beneficiario = new Beneficiario();
        beneficiario.NumeroID = numeroId;
        beneficiario.Email = email;

        new Beneficiarios().recordarClave(beneficiario, this);
    }


    @Override
    public void onSuccess(Beneficiario beneficiario) {
        iViewBeneficiario.onSuccess(beneficiario);
    }

    @Override
    public void onError(ErrorApi error) {
        iViewBeneficiario.onError(error);
    }

    @Override
    public void onSuccessRecordarClave(String mensaje) {
        iViewBeneficiario.onSuccessRecordarClave(mensaje);

    }

    @Override
    public void onErrorRecordarClave(ErrorApi error) {
        iViewBeneficiario.onErrorRecordarClave(error);
    }
}
