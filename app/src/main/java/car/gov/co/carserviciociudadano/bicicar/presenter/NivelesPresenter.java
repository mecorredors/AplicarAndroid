package car.gov.co.carserviciociudadano.bicicar.presenter;

import java.util.List;

import car.gov.co.carserviciociudadano.bicicar.dataaccess.Niveles;
import car.gov.co.carserviciociudadano.bicicar.interfaces.INivel;
import car.gov.co.carserviciociudadano.bicicar.model.Nivel;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class NivelesPresenter implements INivel {
    IViewNivel iViewNivel;

    public  NivelesPresenter(IViewNivel iViewNivel){
        this.iViewNivel = iViewNivel;
    }

    public void getNiveles(){
        new Niveles().getNiveles(this);
    }


    @Override
    public void onSuccess(List<Nivel> lstNiveles) {
        iViewNivel.onSuccess(lstNiveles);
    }

    @Override
    public void onError(ErrorApi errorApi) {
        iViewNivel.onError(errorApi);
    }
}
