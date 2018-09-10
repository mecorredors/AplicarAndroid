package car.gov.co.carserviciociudadano.petcar.presenter;

import java.util.List;

import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.petcar.dataaccess.Contenedores;
import car.gov.co.carserviciociudadano.petcar.interfaces.IContenedor;
import car.gov.co.carserviciociudadano.petcar.model.Contenedor;

/**
 * Created by apple on 9/09/18.
 */

public class ContenedorPresenter implements IContenedor {


    IViewContenedor iview;
    public  ContenedorPresenter(IViewContenedor iview){
        this.iview = iview;
    }

    public void getContenedores(String idMunicipio){
        Contenedores.getContenedores(idMunicipio, this);
    }
    public void getContenedores(){
        Contenedores.getContenedores(this);
    }

    @Override
    public void onError(ErrorApi error) {
        iview.onError(error);
    }

    @Override
    public void onSuccess(List<Contenedor> lstContenedores) {
        iview.onSuccess(lstContenedores);
    }
}
