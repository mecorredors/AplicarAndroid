package car.gov.co.carserviciociudadano.bicicar.interfaces;


import java.util.List;

import car.gov.co.carserviciociudadano.bicicar.model.Estadistica;

import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public interface IReportes {
    void onSuccessGranTotal(Estadistica estadistica);
    void onSuccessEstadisticaPersona(List<Estadistica> estadistica);
    void onErrorGranTotal(ErrorApi error);
    void onErrorEstadisticaPersona(ErrorApi error);
}
