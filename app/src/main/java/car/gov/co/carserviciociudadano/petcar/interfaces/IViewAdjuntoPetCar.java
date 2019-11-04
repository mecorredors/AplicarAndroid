package car.gov.co.carserviciociudadano.petcar.interfaces;


import car.gov.co.carserviciociudadano.petcar.model.MaterialRecogido;


public interface IViewAdjuntoPetCar {
    void onSuccessArchivosAdjunto(MaterialRecogido materialRecogido);
    void onErrorArchivoAdjunto(String mensaje);
}
