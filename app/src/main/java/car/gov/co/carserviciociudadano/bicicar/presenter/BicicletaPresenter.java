package car.gov.co.carserviciociudadano.bicicar.presenter;

import car.gov.co.carserviciociudadano.bicicar.dataaccess.Bicicletas;
import car.gov.co.carserviciociudadano.bicicar.interfaces.IBicicleta;
import car.gov.co.carserviciociudadano.bicicar.model.Bicicleta;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class BicicletaPresenter implements IBicicleta {
    IViewBicicleta iViewBicicleta;

    public BicicletaPresenter(IViewBicicleta iViewBicicleta){
        this.iViewBicicleta = iViewBicicleta;
    }

    public  void obtenerItem(int idBicicleta){
        new Bicicletas().obtenerItem(idBicicleta, this);
    }

    @Override
    public void onSuccessBicicleta(Bicicleta bicicleta) {
        iViewBicicleta.onSuccessBicicleta(bicicleta);
    }

    @Override
    public void onErrorBicicleta(ErrorApi error) {
        iViewBicicleta.onErrorBicicleta(error);
    }
}
