package car.gov.co.carserviciociudadano.consultapublica.interfaces;

import java.util.List;

import car.gov.co.carserviciociudadano.consultapublica.model.Documento;
import car.gov.co.carserviciociudadano.consultapublica.model.Tramite;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

/**
 * Created by Olger on 28/01/2017.
 */

public interface IDocumento {
    void onSuccess(List<Documento> lstDocumentos);
    void onError(ErrorApi error);
}
