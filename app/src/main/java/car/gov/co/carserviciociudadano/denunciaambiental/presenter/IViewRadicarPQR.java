package car.gov.co.carserviciociudadano.denunciaambiental.presenter;

import java.util.List;
import car.gov.co.carserviciociudadano.denunciaambiental.model.Denuncia;
import car.gov.co.carserviciociudadano.denunciaambiental.model.Foto;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

/**
 * Created by Olger on 27/05/2017.
 */

public interface IViewRadicarPQR {
    void onSuccessImages(List<Foto> lstArchivoAdjunto);
    void onSuccessRadicarPQR(Denuncia denunciad);
    void onErrorImages(String mensaje);
    void onErrorRadicarPQR(ErrorApi errorApi);



}
