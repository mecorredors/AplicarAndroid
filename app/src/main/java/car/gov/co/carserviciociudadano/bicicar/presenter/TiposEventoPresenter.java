package car.gov.co.carserviciociudadano.bicicar.presenter;


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
        tiposEventosData.deleteAll();
        for (TipoEvento item : lstTiposEvento) {
            tiposEventosData.insert(item);
        }
    }

    @Override
    public void onSuccess(List<TipoEvento> lstTiposEvento) {
        guardarTiposEvento(lstTiposEvento);
        lstTiposEvento.add(0, new TipoEvento("Seleccione tipo evento"));
        iViewTipoEvento.onSuccessTipoEvento(lstTiposEvento);
    }

    @Override
    public void onError(ErrorApi error) {
        iViewTipoEvento.onErrorTiposEvento(error);
    }
}
