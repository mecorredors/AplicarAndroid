package car.gov.co.carserviciociudadano.parques.interfaces;

import java.util.List;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.Municipio;

/**
 * Created by Olger on 26/11/2016.
 */

public interface IMunicipio {
    void onSuccess(List<Municipio> lstMunicipios);
    void onError(ErrorApi error);
}
