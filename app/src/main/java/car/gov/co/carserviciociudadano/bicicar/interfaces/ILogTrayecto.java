package car.gov.co.carserviciociudadano.bicicar.interfaces;


import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.bicicar.model.LogTrayecto;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public interface ILogTrayecto {
    void onSuccessLogTrayecto(LogTrayecto logTrayecto);
    void onErrorLogTrayecto(ErrorApi error);
}
