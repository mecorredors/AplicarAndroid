package car.gov.co.carserviciociudadano.denunciaambiental.presenter;

import java.util.ArrayList;
import java.util.List;

import car.gov.co.carserviciociudadano.denunciaambiental.dataacces.Lugares;
import car.gov.co.carserviciociudadano.denunciaambiental.interfaces.ILugar;
import car.gov.co.carserviciociudadano.denunciaambiental.model.Lugar;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

/**
 * Created by Olger on 27/05/2017.
 */

public class LugaresPresenter {
    IViewLugares viewLugares;
    public LugaresPresenter(IViewLugares viewLugares){
        this.viewLugares = viewLugares;
    }

    public void obtenerDepartamentos(){
         new Lugares().list("COL", 1, new ILugar() {
             @Override
             public void onSuccess(List<Lugar> lstLugares) {
                 lstLugares.add(0,new Lugar("","Departamentos (" +lstLugares.size() +")"));
                 viewLugares.onSuccessDepartamentos(lstLugares);
             }
             public void onSuccessIdXCoordenada(String idLugar) {
             }
             @Override
             public void onError(ErrorApi errorApi) {
                 viewLugares.onErrorDepartamentos(errorApi);
             }
         });
    }

    public void obtenerMunicipios(String idPadreLugar){
        if (idPadreLugar.equals("")) {
            List<Lugar> lstLugares = new ArrayList<>();
            lstLugares.add(0, new Lugar("", "Municipio"));
            viewLugares.onSuccessMunicipios(lstLugares);
        }else {
            new Lugares().list(idPadreLugar, 2, new ILugar() {
                @Override
                public void onSuccess(List<Lugar> lstLugares) {
                    lstLugares.add(0, new Lugar("", "Municipios (" +lstLugares.size() +")" ));
                    viewLugares.onSuccessMunicipios(lstLugares);
                }

                public void onSuccessIdXCoordenada(String idLugar) {
                }

                @Override
                public void onError(ErrorApi errorApi) {
                    viewLugares.onErrorMunicipios(errorApi);
                }
            });
        }
    }

    public void obtenerVeredas(String idPadreLugar){

        if (idPadreLugar.equals("")) {
            List<Lugar> lstLugares = new ArrayList<>();
            lstLugares.add(0, new Lugar("", "Vereda"));
            viewLugares.onSuccessVeredas(lstLugares);
        }else {
            new Lugares().list(idPadreLugar, 3, new ILugar() {
                @Override
                public void onSuccess(List<Lugar> lstLugares) {
                    if (lstLugares.size() > 1)
                        lstLugares.add(0, new Lugar("", "Veredas (" +lstLugares.size() +")"));
                    if (lstLugares.size() == 0)
                        lstLugares.add(0, new Lugar("", "No hay veredas"));

                    viewLugares.onSuccessVeredas(lstLugares);
                }

                @Override
                public void onSuccessIdXCoordenada(String idLugar) {
                }

                @Override
                public void onError(ErrorApi errorApi) {
                    viewLugares.onErrorVeredas(errorApi);
                }
            });
        }
    }
}
