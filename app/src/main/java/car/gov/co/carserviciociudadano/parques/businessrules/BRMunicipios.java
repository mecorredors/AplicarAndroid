package car.gov.co.carserviciociudadano.parques.businessrules;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.parques.dataaccess.Municipios;
import car.gov.co.carserviciociudadano.parques.dataaccess.Parques;
import car.gov.co.carserviciociudadano.parques.interfaces.IMunicipio;
import car.gov.co.carserviciociudadano.parques.interfaces.IParque;
import car.gov.co.carserviciociudadano.parques.model.Municipio;
import car.gov.co.carserviciociudadano.parques.model.Parque;

/**
 * Created by Olger on 15/01/2017.
 */

public class BRMunicipios {
    public void list(final IMunicipio iMunicipio )    {
        String tag = Municipios.TAG;
        Municipios municipios = new Municipios();

        if (Utils.existeCache(tag) && !Utils.cacheExpiro(Enumerator.CacheNumDias.MUNICIPIOS,tag)) {
            try {
                Type resultType = new TypeToken<List<Municipio>>() {}.getType();
                List<Municipio> lstMunicipios =  Reservoir.get(tag, resultType);
                iMunicipio.onSuccess( lstMunicipios);
            } catch (Exception e) {
                municipios.list(iMunicipio);
            }
        }else {
            municipios.list(iMunicipio);
        }
    }
}
