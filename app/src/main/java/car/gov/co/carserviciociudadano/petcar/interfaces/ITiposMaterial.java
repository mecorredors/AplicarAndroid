package car.gov.co.carserviciociudadano.petcar.interfaces;

import java.util.List;

import car.gov.co.carserviciociudadano.common.IView;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.petcar.model.Contenedor;
import car.gov.co.carserviciociudadano.petcar.model.TipoMaterial;

/**
 * Created by apple on 9/09/18.
 */

public interface ITiposMaterial {
    void onSuccessTiposMaterial(List<TipoMaterial> lstTiposMaterial);
    void onErrorTiposMaterial(ErrorApi error);
}
