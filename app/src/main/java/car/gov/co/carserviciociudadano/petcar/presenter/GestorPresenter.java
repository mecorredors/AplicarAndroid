package car.gov.co.carserviciociudadano.petcar.presenter;

import car.gov.co.carserviciociudadano.bicicar.model.RespuestaApi;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.petcar.dataaccess.Gestores;
import car.gov.co.carserviciociudadano.petcar.interfaces.IGestor;
import car.gov.co.carserviciociudadano.petcar.model.Gestor;

public class GestorPresenter implements IGestor {
    IGestor iGestor;

    public GestorPresenter(IGestor iGestor ){
        this.iGestor = iGestor;
    }


    public  void login(String identificacion, String claveApp){
        new Gestores().login(identificacion, claveApp, this);
    }

    public  void cambiarClave(String identificacion, String claveApp, String nuevaClave){
        new Gestores().cambiarClave(identificacion, claveApp, nuevaClave, this);
    }
    public  void recordarClave(String identificacion, String email){
        Gestor gestor = new Gestor();
        gestor.Identificacion = identificacion;
        gestor.Email = email;
        new Gestores().recordarClave(gestor, this);
    }


    @Override
    public void onSuccessLoging(Gestor gestor) {
        iGestor.onSuccessLoging(gestor);
    }

    @Override
    public void onErrorLoging(ErrorApi error) {
        iGestor.onErrorLoging(error);
    }

    @Override
    public void onSuccessCambiarClave(Gestor gestor) {
        iGestor.onSuccessCambiarClave(gestor);
    }

    @Override
    public void onErrorCambiarClave(ErrorApi error) {
        iGestor.onErrorCambiarClave(error);
    }

    @Override
    public void onSuccessRercordarClave(RespuestaApi respuestaApi) {
        iGestor.onSuccessRercordarClave(respuestaApi);
    }

    @Override
    public void onErrorRecordarClave(ErrorApi error) {
        iGestor.onErrorRecordarClave(error);
    }
}