package car.gov.co.carserviciociudadano.parques.presenter;
import android.graphics.Color;
import android.util.SparseArray;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import car.gov.co.carserviciociudadano.parques.dataaccess.Parques;
import car.gov.co.carserviciociudadano.parques.interfaces.IParque;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.Parque;


/**
 * Created by Olger on 15/01/2017.
 */

public class ParquePresenter {
    IViewParque iviewParque;
    String colores[] = {"#90962C","#BE0033","#9C8E70","#77B5BA","#E2A900","#005CA5"};
    String coloresFooter[] = {"#6F7724","#8E1731","#857555","#387775","#C5911A","#084A7A"};
    int idsParques[] = {81,83,86,85,84,82};


    SparseArray coloresMap = new SparseArray();
    SparseArray coloresFooterMap = new SparseArray();

    public ParquePresenter(IViewParque iviewParque){
        this.iviewParque = iviewParque;
        int i = 0;
        for (int id : idsParques){
            coloresMap.put(id, colores[i]);
            coloresFooterMap.put(id, coloresFooter[i]);
            i++;
        }

    }
    public void list( )    {
        Parques parques = new Parques();
        parques.list(iParque);

    }

    IParque iParque = new IParque() {
        @Override
        public void onSuccess(List<Parque> lstParques) {
            int i = 0;
            int indexJuanPablo = 0;
            int indexGuatavita = 0;
            for(Parque item: lstParques){
                item.setNombreParque(item.getNombreParque().replace("PARQUE",""));
                if (i >= coloresMap.size()) {
                    item.setColor(Color.parseColor(colores[0]));
                    item.setColorFooter(Color.parseColor(coloresFooter[0]));
                    break;
                }

                if (item.getIDParque() == 86) // ID Juan Pablo
                    indexJuanPablo = i;
                if (item.getIDParque() == 85) // ID Guatavita
                    indexGuatavita = i;

                item.setColor(Color.parseColor(coloresMap.get(item.getIDParque()).toString()));
                item.setColorFooter(Color.parseColor(coloresFooterMap.get(item.getIDParque()).toString()));
                i++;
            }
            //cammbiar posicion entre Juan pablo y Guatavita
          //  Collections.swap(lstParques, indexJuanPablo , indexGuatavita);

            iviewParque.onSuccess(lstParques);
        }

        @Override
        public void onError(ErrorApi error) {
           iviewParque.onError(error);
        }
    };


}
