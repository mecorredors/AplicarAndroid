package car.gov.co.carserviciociudadano.petcar.interfaces;

import java.util.List;

import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.petcar.model.MaterialRecogido;
import car.gov.co.carserviciociudadano.petcar.model.TipoMaterial;

/**
 * Created by apple on 9/09/18.
 */

public interface IMaterialRecogido {
    void onSuccessPublicarMaterial(MaterialRecogido materialRecogido);
    void onErrorPublicarMaterial(ErrorApi error);
}
