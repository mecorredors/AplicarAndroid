package car.gov.co.carserviciociudadano.bicicar.presenter;

import java.util.ArrayList;
import java.util.List;

import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.Utils.Utils;
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
        if (Utils.isOnline(AppCar.getContext())) {
            new Niveles().getNiveles(this);
        }else{
            obtenerNivelesLocal();
        }
    }


    @Override
    public void onSuccess(List<Nivel> lstNiveles) {
        iViewNivel.onSuccessNiveles(lstNiveles);
    }

    @Override
    public void onError(ErrorApi errorApi) {

       obtenerNivelesLocal();

    }

    private void obtenerNivelesLocal(){
        List<Nivel> lstNiveles = new ArrayList<>();
        lstNiveles.add(new Nivel(1, "Familiar"));
        lstNiveles.add(new Nivel(2, "Básico"));
        lstNiveles.add(new Nivel(3, "Medio"));
        lstNiveles.add(new Nivel(4, "Avanzado"));
        lstNiveles.add(new Nivel(5, "Profesional"));

        iViewNivel.onSuccessNiveles(lstNiveles);
    }
}
