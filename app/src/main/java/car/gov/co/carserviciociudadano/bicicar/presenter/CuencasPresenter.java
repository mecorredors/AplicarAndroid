package car.gov.co.carserviciociudadano.bicicar.presenter;

import java.util.List;

import car.gov.co.carserviciociudadano.bicicar.dataaccess.Cuencas;
import car.gov.co.carserviciociudadano.bicicar.interfaces.ICuenca;
import car.gov.co.carserviciociudadano.bicicar.model.Cuenca;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class CuencasPresenter implements ICuenca {
    IViewCuenca iViewCuencas;

    public CuencasPresenter(IViewCuenca iViewCuencas){
        this.iViewCuencas = iViewCuencas;
    }

    public void getCuencas(){
          new Cuencas().getCuencas(this);
    }

    @Override
    public void onSuccess(List<Cuenca> lstCuencas) {
        lstCuencas.add(0, new Cuenca(0, "Seleccione cuenca"));
        iViewCuencas.onSuccessCuencas(lstCuencas);
    }

    @Override
    public void onError(ErrorApi errorApi) {
       iViewCuencas.onErrorCuencas(errorApi);
    }

}
