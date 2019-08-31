package car.gov.co.carserviciociudadano.bicicar.presenter;

import java.util.List;

import car.gov.co.carserviciociudadano.bicicar.model.Lugar;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

/**
 * Created by Olger on 27/05/2017.
 */

public interface IViewLugares {
   void  onSuccessMunicipios(List<Lugar> lstMunicipios);
   void  onSuccessVeredas(List<Lugar> lstVeredas);
   void onErrorMunicipios(ErrorApi errorApi);
   void onErrorVeredas(ErrorApi errorApi);

}
