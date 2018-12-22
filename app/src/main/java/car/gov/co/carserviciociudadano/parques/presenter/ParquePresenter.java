package car.gov.co.carserviciociudadano.parques.presenter;
import android.graphics.Color;

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
    String colores[] = {"#90962C","#BE0033","#9C8E70","#77B5BA","#E2A900","#005CA5"};
    String coloresFooter[] = {"#6F7724","#8E1731","#857555","#387775","#C5911A","#084A7A"};

    public ParquePresenter(IViewParque iviewParque){
        this.iviewParque = iviewParque;
    }
    public void list( )    {
        Parques parques = new Parques();
        parques.list(iParque);

    }

    IParque iParque = new IParque() {
        @Override
        public void onSuccess(List<Parque> lstParques) {
            int i = 0;
            for(Parque item: lstParques){
                if (i >= colores.length)
                    i=0;

                item.setColor(Color.parseColor(colores[i]));
                item.setColorFooter(Color.parseColor(coloresFooter[i]));
                item.setNombreParque(item.getNombreParque().replace("PARQUE",""));
                i++;
            }

            iviewParque.onSuccess(lstParques);
        }

        @Override
        public void onError(ErrorApi error) {
           iviewParque.onError(error);
        }
    };


}
