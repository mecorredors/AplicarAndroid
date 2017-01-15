package car.gov.co.carserviciociudadano.parques.businessrules;
import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.parques.dataaccess.Parques;
import car.gov.co.carserviciociudadano.parques.interfaces.IParque;
import car.gov.co.carserviciociudadano.parques.model.Parque;


/**
 * Created by Olger on 15/01/2017.
 */

public class BRParques {
    public void list(final IParque iParque )    {
        String tag = Parques.TAG;
        Parques parques = new Parques();

        if (Utils.existeCache(tag) && !Utils.cacheExpiro(Enumerator.CacheNumDias.PARQUES,tag)) {
            try {
                Type resultType = new TypeToken<List<Parque>>() {}.getType();
                List<Parque> lstParques =  Reservoir.get(tag, resultType);
                iParque.onSuccess( lstParques);
            } catch (Exception e) {
                parques.list(iParque);
            }
        }else {
            parques.list(iParque);
        }
    }
}
