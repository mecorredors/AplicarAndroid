package car.gov.co.carserviciociudadano.parques.interfaces;

import java.util.List;
import car.gov.co.carserviciociudadano.parques.model.Banco;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

/**
 * Created by Olger on 26/11/2016.
 */

public interface IBanco {
    void onSuccess(List<Banco> lstBanco);
    void onError(ErrorApi error);
}
