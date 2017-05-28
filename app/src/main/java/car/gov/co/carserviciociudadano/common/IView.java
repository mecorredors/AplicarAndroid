package car.gov.co.carserviciociudadano.common;

import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

/**
 * Created by Olger on 25/04/2017.
 */

public interface IView {
    void onError(ErrorApi error);
}
