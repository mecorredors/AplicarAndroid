package car.gov.co.carserviciociudadano.parques.presenter;
import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.parques.dataaccess.Parques;
import car.gov.co.carserviciociudadano.parques.interfaces.IParque;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.Parque;


/**
 * Created by Olger on 15/01/2017.
 */

public class ParquePresenter {
    IViewParque iviewParque;
    public ParquePresenter(IViewParque iviewParque){
        this.iviewParque = iviewParque;
    }
    public void list( )    {
        String tag = Parques.TAG;
        Parques parques = new Parques();
        parques.list(iParque);

       /* if (Utils.existeCache(tag) && !Utils.cacheExpiro(Enumerator.CacheNumDias.PARQUES,tag)) {
            try {
                Type resultType = new TypeToken<List<Parque>>() {}.getType();
                List<Parque> lstParques =  Reservoir.get(tag, resultType);
                iParque.onSuccess( lstParques);
            } catch (Exception e) {
                parques.list(iParque);
            }
        }else {
            parques.list(iParque);
        }*/
    }

    IParque iParque = new IParque() {
        @Override
        public void onSuccess(List<Parque> lstParques) {
            iviewParque.onSuccess(lstParques);
        }

        @Override
        public void onError(ErrorApi error) {
           iviewParque.onError(error);
        }
    };
}
