package car.gov.co.carserviciociudadano.bicicar.presenter;

import java.util.ArrayList;
import java.util.List;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Lugares;
import car.gov.co.carserviciociudadano.bicicar.interfaces.ILugar;
import car.gov.co.carserviciociudadano.bicicar.model.Lugar;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;


/**
 * Created by Olger on 27/05/2017.
 */

public class LugaresPresenter implements ILugar {
    IViewLugares iViewLugares;
    public LugaresPresenter(IViewLugares iViewLugares){
        this.iViewLugares = iViewLugares;
    }

    public void obtenerMunicipios(){
        Lugares.getMunicipios(this);
    }
    public void obtenerVeredas(String idMunicipio){
        if (idMunicipio.equals("")) {
            List<car.gov.co.carserviciociudadano.bicicar.model.Lugar> lstLugares = new ArrayList<>();
            lstLugares.add(0, new car.gov.co.carserviciociudadano.bicicar.model.Lugar("", "Vereda"));
            iViewLugares.onSuccessVeredas(lstLugares);
            return;
        }

        Lugares.getVeredas(idMunicipio, this);
    }

    @Override
    public void onSuccessMunicipios(List<Lugar> lstMunicipios) {
        lstMunicipios.add(0, new car.gov.co.carserviciociudadano.bicicar.model.Lugar("", "Municipios (" +lstMunicipios.size() +")" ));
        iViewLugares.onSuccessMunicipios(lstMunicipios);
    }

    @Override
    public void onSuccessVeredas(List<Lugar> lstVeredas) {
        if (lstVeredas.size() > 1)
            lstVeredas.add(0, new car.gov.co.carserviciociudadano.bicicar.model.Lugar("", "Veredas (" +lstVeredas.size() +")"));
        if (lstVeredas.size() == 0)
            lstVeredas.add(0, new car.gov.co.carserviciociudadano.bicicar.model.Lugar("", "No hay veredas"));
        iViewLugares.onSuccessVeredas(lstVeredas);
    }

    @Override
    public void onErrorMunicipios(ErrorApi errorApi) {
        iViewLugares.onErrorMunicipios(errorApi);
    }

    @Override
    public void onErrorVeredas(ErrorApi errorApi) {
        iViewLugares.onErrorVeredas(errorApi);
    }
}
