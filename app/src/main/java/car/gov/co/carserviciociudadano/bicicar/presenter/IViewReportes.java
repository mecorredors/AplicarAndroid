package car.gov.co.carserviciociudadano.bicicar.presenter;

import java.util.List;

import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.bicicar.model.Estadistica;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public interface IViewReportes {
    void onSuccessGranTotal(Estadistica estadistica);
    void onSuccessEstadisticaPersona(List<Estadistica> estadistica);
    void onErrorGranTotal(ErrorApi errorApi);
    void onErrorEstadisticaPersona(ErrorApi error);
}
