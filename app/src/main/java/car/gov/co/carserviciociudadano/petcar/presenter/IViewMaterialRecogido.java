package car.gov.co.carserviciociudadano.petcar.presenter;


import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

/**
 * Created by apple on 9/09/18.
 */

public interface IViewMaterialRecogido {
    void onSuccessPublicarMaterial();
    void onErrorPublicarMaterial(ErrorApi error);
    void onErrorValidacion(String  mensaje);

}
