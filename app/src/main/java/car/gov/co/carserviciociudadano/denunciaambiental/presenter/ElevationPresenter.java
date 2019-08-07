package car.gov.co.carserviciociudadano.denunciaambiental.presenter;

import car.gov.co.carserviciociudadano.denunciaambiental.dataacces.Elevation;
import car.gov.co.carserviciociudadano.denunciaambiental.interfaces.IElevation;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

/**
 * Created by Olger on 29/05/2017.
 */

public class ElevationPresenter {
    IViewElevation viewElevation;
    public ElevationPresenter(IViewElevation iViewElevation){
        this.viewElevation = iViewElevation;
    }

    public void getElevation(double latitude, double longitude){

        new Elevation().getElevation(latitude, longitude, new IElevation() {
            @Override
            public void onResult(double elevation) {
                viewElevation.onSuccessElevation(elevation);
            }

            @Override
            public void onErrror(ErrorApi errorApi) {
                viewElevation.onErrrorElevation(errorApi.getStatusCode());
            }
        });
    }
}
