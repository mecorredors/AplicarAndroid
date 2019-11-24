package car.gov.co.carserviciociudadano.petcar.interfaces;


import car.gov.co.carserviciociudadano.petcar.model.MaterialRecogido;
import car.gov.co.carserviciociudadano.petcar.model.Visita;


public interface IViewAdjuntoPetCar {
    void onSuccessArchivosAdjunto(MaterialRecogido materialRecogido);
    void onSuccessArchivosAdjunto(Visita visita);
    void onErrorArchivoAdjunto(String mensaje);
}
