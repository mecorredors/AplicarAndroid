package car.gov.co.carserviciociudadano.parques.interfaces;

import java.util.List;

import car.gov.co.carserviciociudadano.parques.model.ArchivoParque;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;


/**
 * Created by Olger on 27/11/2016.
 */

public interface IArchivoParque {
    void onSuccess(List<ArchivoParque> lstArchivosParque);
    void onError(ErrorApi error);
}
