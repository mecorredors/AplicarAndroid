package car.gov.co.carserviciociudadano.parques.businessrules;

import com.anupcowkur.reservoir.Reservoir;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.parques.dataaccess.ArchivosParque;
import car.gov.co.carserviciociudadano.parques.dataaccess.IViewArchivoParque;
import car.gov.co.carserviciociudadano.parques.interfaces.IArchivoParque;
import car.gov.co.carserviciociudadano.parques.model.ArchivoParque;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;


/**
 * Created by Olger on 15/01/2017.
 */

public class BRArchivosParquePresenter {

IViewArchivoParque viewArchivoParque;

    public BRArchivosParquePresenter(IViewArchivoParque view){
        this.viewArchivoParque= view;
    }

    public void list( int idParque )    {
        String tag = ArchivosParque.TAG+idParque;
        ArchivosParque archivosParque = new ArchivosParque();

        if (Utils.existeCache(tag) && !Utils.cacheExpiro(Enumerator.CacheNumDias.ARCHIVOS_PARQUE,tag)) {
            try {
                Type resultType = new TypeToken<List<ArchivoParque>>() {}.getType();
                List<ArchivoParque> lstArchivosParque =  Reservoir.get(tag, resultType);
                setResult(lstArchivosParque);
            } catch (Exception e) {
                archivosParque.list(iArchivosParque,idParque);
            }
        }else {
            archivosParque.list(iArchivosParque,idParque);
        }

    }

    IArchivoParque iArchivosParque = new IArchivoParque() {
        @Override
        public void onSuccess(List<ArchivoParque> lstArchivosParque) {
            setResult(lstArchivosParque);
        }

        @Override
        public void onError(ErrorApi error) {
            viewArchivoParque.onError(error);
        }
    };

    private void setResult(List<ArchivoParque> lstArchivosParque){
        if(lstArchivosParque.size()>0) {
           ArchivoParque imagenPrincipal = null;
            List<ArchivoParque> lstImagenesParque = new ArrayList<>();

            int count = 0;
            for(ArchivoParque item : lstArchivosParque) {
                if (item.getIDTipoArchivo() == 1) {
                    count++;
                    imagenPrincipal = item;
                }
                if (item.getIDTipoArchivo() == 3) {
                    lstImagenesParque.add(item);
                    count++;
                }
            }
            if(count>0 && imagenPrincipal != null) lstImagenesParque.add(0,imagenPrincipal);

           viewArchivoParque.onSuccess(lstImagenesParque, imagenPrincipal,count);

        }else{
            viewArchivoParque.onError(new ErrorApi());
        }
    }
}
