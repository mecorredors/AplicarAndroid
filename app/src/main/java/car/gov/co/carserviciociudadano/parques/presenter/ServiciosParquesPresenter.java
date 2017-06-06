package car.gov.co.carserviciociudadano.parques.presenter;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.parques.dataaccess.ServiciosParque;
import car.gov.co.carserviciociudadano.parques.interfaces.IServicioParque;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.ServicioParque;

/**
 * Created by Olger on 02/12/2016.
 */

public class ServiciosParquesPresenter {

    IViewServiciosParque iViewServiciosParque;
    public ServiciosParquesPresenter(IViewServiciosParque iViewServiciosParque){
        this.iViewServiciosParque = iViewServiciosParque;
    }
    public void list(int idParque  )
    {
        ServiciosParque serviciosParque = new ServiciosParque();
        serviciosParque.list(iServicioParque,idParque);

    }

    IServicioParque iServicioParque = new IServicioParque() {
        @Override
        public void onSuccess(List<ServicioParque> lista) {
            iViewServiciosParque.onSuccess(lista);
        }

        @Override
        public void onError(ErrorApi error) {
            iViewServiciosParque.onError(error);
        }
    };

}
