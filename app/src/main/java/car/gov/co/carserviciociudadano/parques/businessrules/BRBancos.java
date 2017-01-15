package car.gov.co.carserviciociudadano.parques.businessrules;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.parques.dataaccess.Bancos;
import car.gov.co.carserviciociudadano.parques.interfaces.IBanco;
import car.gov.co.carserviciociudadano.parques.model.Banco;

/**
 * Created by Olger on 15/01/2017.
 */

public class BRBancos {
    public void list(final IBanco iBanco )    {
        String tag = Bancos.TAG;
        Bancos bancos = new Bancos();

        if (Utils.existeCache(tag) && !Utils.cacheExpiro(Enumerator.CacheNumDias.BANCOS,tag)) {
            try {
                Type resultType = new TypeToken<List<Banco>>() {}.getType();
                List<Banco> lstBancos =  Reservoir.get(tag, resultType);
                iBanco.onSuccess( lstBancos);
            } catch (Exception e) {
                bancos.list(iBanco);
            }
        }else {
            bancos.list(iBanco);
        }
    }
}
