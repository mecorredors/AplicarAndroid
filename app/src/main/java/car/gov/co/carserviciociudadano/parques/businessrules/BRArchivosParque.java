package car.gov.co.carserviciociudadano.parques.businessrules;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.parques.dataaccess.ArchivosParque;
import car.gov.co.carserviciociudadano.parques.interfaces.IArchivoParque;
import car.gov.co.carserviciociudadano.parques.model.ArchivoParque;


/**
 * Created by Olger on 15/01/2017.
 */

public class BRArchivosParque {
    public void list(final IArchivoParque iArchivosParque,int idParque )    {
        String tag = ArchivosParque.TAG+idParque;
        ArchivosParque archivosParque = new ArchivosParque();

        if (Utils.existeCache(tag) && !Utils.cacheExpiro(Enumerator.CacheNumDias.ARCHIVOS_PARQUE,tag)) {
            try {
                Type resultType = new TypeToken<List<ArchivoParque>>() {}.getType();
                List<ArchivoParque> lstArchivosParque =  Reservoir.get(tag, resultType);
                iArchivosParque.onSuccess( lstArchivosParque);
            } catch (Exception e) {
                archivosParque.list(iArchivosParque,idParque);
            }
        }else {
            archivosParque.list(iArchivosParque,idParque);
        }

    }
}
