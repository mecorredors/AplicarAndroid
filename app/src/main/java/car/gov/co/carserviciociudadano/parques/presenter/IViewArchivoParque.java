package car.gov.co.carserviciociudadano.parques.presenter;

import java.util.List;

import car.gov.co.carserviciociudadano.common.IView;
import car.gov.co.carserviciociudadano.parques.model.ArchivoParque;

/**
 * Created by Olger on 25/04/2017.
 */

public interface IViewArchivoParque extends IView {
    void onSuccess(List<ArchivoParque> lstArchivosParque, ArchivoParque archivoParque, int count);
}
