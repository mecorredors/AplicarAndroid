package car.gov.co.carserviciociudadano.petcar.presenter;

import java.util.List;

import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.petcar.dataaccess.TiposMaterial;
import car.gov.co.carserviciociudadano.petcar.interfaces.ITiposMaterial;
import car.gov.co.carserviciociudadano.petcar.model.TipoMaterial;

public class TiposMaterialPresenter implements ITiposMaterial {
    ITiposMaterial iTiposMaterial;

    public TiposMaterialPresenter(ITiposMaterial iTiposMaterial ){
        this.iTiposMaterial = iTiposMaterial;
    }


    public  void getTiposMaterial(){
        new TiposMaterial().getTiposMaterial(this);
    }

    @Override
    public void onSuccessTiposMaterial(List<TipoMaterial> lstTiposMaterial) {
        iTiposMaterial.onSuccessTiposMaterial(lstTiposMaterial);
    }

    @Override
    public void onErrorTiposMaterial(ErrorApi error) {
        iTiposMaterial.onErrorTiposMaterial(error);
    }
}