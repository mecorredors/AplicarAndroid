package car.gov.co.carserviciociudadano.denunciaambiental.presenter;

import java.util.List;

import car.gov.co.carserviciociudadano.denunciaambiental.model.Lugar;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

/**
 * Created by Olger on 27/05/2017.
 */

public interface IViewLugares  {
   void  onSuccessDepartamentos(List<Lugar> lstDepartamentos);
   void  onSuccessMunicipios(List<Lugar> lstMunicipios);
   void  onSuccessVeredas(List<Lugar> lstVeredas);
   void onErrorDepartamentos(ErrorApi errorApi);
   void onErrorMunicipios(ErrorApi errorApi);
   void onErrorVeredas(ErrorApi errorApi);

}
