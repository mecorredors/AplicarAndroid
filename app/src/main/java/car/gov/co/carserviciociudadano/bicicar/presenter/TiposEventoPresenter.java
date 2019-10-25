package car.gov.co.carserviciociudadano.bicicar.presenter;


import android.database.SQLException;

import java.util.ArrayList;
import java.util.List;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.TiposEvento;
import car.gov.co.carserviciociudadano.bicicar.interfaces.ITipoEvento;
import car.gov.co.carserviciociudadano.bicicar.model.TipoEvento;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class TiposEventoPresenter implements ITipoEvento {
    IViewTipoEvento iViewTipoEvento;

    public TiposEventoPresenter(IViewTipoEvento iViewTipoEvento){
        this.iViewTipoEvento = iViewTipoEvento;
    }

    public List<TipoEvento> listLocal(){
        return new TiposEvento().list();
    }

    public  void list(){
            new TiposEvento().list(this);
    }
    private   void guardarTiposEvento(List<TipoEvento> lstTiposEvento){

        TiposEvento tiposEventosData = new TiposEvento();
        try {
            tiposEventosData.deleteAll();
        }catch (SQLException ex){
            onError(new ErrorApi(500, "Error en base de datos local, ingrese a configuración android y elimine los datos de Aplicar"));
            return;
        }
        for (TipoEvento item : lstTiposEvento) {
            tiposEventosData.insert(item);
        }
    }

    @Override
    public void onSuccess(List<TipoEvento> lstTiposEvento) {
        guardarTiposEvento(lstTiposEvento);
        List<TipoEvento> eventos = new ArrayList<>();
        eventos.add( new TipoEvento("Seleccione tipo evento"));
        for (TipoEvento item : lstTiposEvento){
            if (!item.Publico){
                eventos.add(item);
            }
        }

        iViewTipoEvento.onSuccessTipoEvento(eventos);
    }

    @Override
    public void onError(ErrorApi error) {
        iViewTipoEvento.onErrorTiposEvento(error);
    }
}
