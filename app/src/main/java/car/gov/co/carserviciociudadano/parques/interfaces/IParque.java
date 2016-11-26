package car.gov.co.carserviciociudadano.parques.interfaces;

import java.util.List;

import car.gov.co.carserviciociudadano.parques.model.Parque;

/**
 * Created by Olger on 26/11/2016.
 */

public interface IParque {
    void onSuccess(List<Parque> lstParques);
    void onError(int statusCode);

}
