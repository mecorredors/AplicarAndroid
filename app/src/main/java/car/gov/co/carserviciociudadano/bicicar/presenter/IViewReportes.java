package car.gov.co.carserviciociudadano.bicicar.presenter;

import java.util.List;

import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.bicicar.model.Estadistica;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public interface IViewReportes {
    void onSuccessGranTotal(Estadistica estadistica);
    void onSuccessEstadistica(List<Estadistica> estadistica);
    void onErrorGranTotal(ErrorApi errorApi);
    void onErrorEstadistica(ErrorApi error);
    void onSuccessEstadisticaMesual(List<Estadistica> estadistica);
    void onSuccessEstadisticaAnual(List<Estadistica> estadistica);

}
