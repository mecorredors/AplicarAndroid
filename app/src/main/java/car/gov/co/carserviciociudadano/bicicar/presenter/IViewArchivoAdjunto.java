package car.gov.co.carserviciociudadano.bicicar.presenter;

import java.util.List;

import car.gov.co.carserviciociudadano.bicicar.model.ArchivoAdjunto;


public interface IViewArchivoAdjunto {
    void onSuccessArchivosAdjunto(List<ArchivoAdjunto> lstArchivoAdjunto);
    void onErrorArchivoAdjunto(String mensaje);
}
