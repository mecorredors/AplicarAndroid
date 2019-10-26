package car.gov.co.carserviciociudadano.petcar.presenter;

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


    @Override
    public void onSuccessLoging(Gestor gestor) {
        iGestor.onSuccessLoging(gestor);
    }

    @Override
    public void onErrorLoging(ErrorApi error) {
        iGestor.onErrorLoging(error);
    }
}