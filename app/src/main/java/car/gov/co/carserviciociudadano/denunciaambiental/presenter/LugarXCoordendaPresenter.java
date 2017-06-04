package car.gov.co.carserviciociudadano.denunciaambiental.presenter;

import java.util.List;

import car.gov.co.carserviciociudadano.denunciaambiental.dataacces.Lugares;
import car.gov.co.carserviciociudadano.denunciaambiental.interfaces.ILugar;
import car.gov.co.carserviciociudadano.denunciaambiental.model.Lugar;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

/**
 * Created by Olger on 03/06/2017.
 */

public class LugarXCoordendaPresenter {
    IViewIdLugarXCoordenada viewIdLugarXCoordenada;
    public LugarXCoordendaPresenter(IViewIdLugarXCoordenada iViewIdLugarXCoordenada){
        this.viewIdLugarXCoordenada = iViewIdLugarXCoordenada;
    }

    public void getId(String norte, String este){
        new Lugares().getIdxCoordenada(norte,este, new ILugar() {
            @Override
            public void onSuccess(List<Lugar> lstLugares) {
            }
            public void onSuccessIdXCoordenada(String idLugar) {
                viewIdLugarXCoordenada.onSuccessIdLugarXCoordenada(idLugar);
            }
            @Override
            public void onError(ErrorApi errorApi) {
                viewIdLugarXCoordenada.onErrorIdLugarXCoordenada(errorApi);
            }
        });
    }


}
