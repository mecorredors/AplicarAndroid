package car.gov.co.carserviciociudadano.denunciaambiental.presenter;

import java.util.List;

import car.gov.co.carserviciociudadano.denunciaambiental.model.Lugar;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

/**
 * Created by Olger on 27/05/2017.
 */

public interface IViewIdLugarXCoordenada {
   void  onSuccessIdLugarXCoordenada(String idLugar);;
   void onErrorIdLugarXCoordenada(ErrorApi errorApi);

}
