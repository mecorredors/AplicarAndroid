package car.gov.co.carserviciociudadano.parques.presenter;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.parques.dataaccess.Bancos;
import car.gov.co.carserviciociudadano.parques.interfaces.IBanco;
import car.gov.co.carserviciociudadano.parques.model.Banco;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

/**
 * Created by Olger on 15/01/2017.
 */

public class BancosPresenter {
    IViewBancos iViewBancos;
    public BancosPresenter(IViewBancos iViewBancos){
        this.iViewBancos = iViewBancos;
    }
    public void list()    {
        String tag = Bancos.TAG;
        Bancos bancos = new Bancos();
        bancos.list(iBanco);
    }

    IBanco iBanco = new IBanco() {
        @Override
        public void onSuccess(List<Banco> lstBanco) {
            iViewBancos.onSuccess(lstBanco);
        }

        @Override
        public void onError(ErrorApi error) {
            iViewBancos.onError(error);
        }
    };
}
