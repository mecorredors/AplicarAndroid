package car.gov.co.carserviciociudadano.bicicar.presenter;

import java.util.List;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Colegios;
import car.gov.co.carserviciociudadano.bicicar.interfaces.IColegio;
import car.gov.co.carserviciociudadano.bicicar.model.Colegio;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class ColegiosPresenter implements IColegio {
    IViewColegio iViewColegio;

    public ColegiosPresenter(IViewColegio iViewColegio){
        this.iViewColegio = iViewColegio;
    }

    public void publicar() {
        final Colegios colegiosData = new Colegios();
        String where = Colegio.ESTADO + "= " + Enumerator.Estado.PENDIENTE_PUBLICAR;
        List<Colegio> lstColegios = colegiosData.list(where);
        if (lstColegios.size() > 0) {
            Colegio item = lstColegios.get(0);
            colegiosData.actualizar(item, new IColegio() {
                @Override
                public void onSuccess(List<Colegio> lstColegios) {

                }

                @Override
                public void onSuccess(Colegio colegio) {
                    colegio.Estado = Enumerator.Estado.PUBLICADO;
                    if (colegiosData.update(colegio)){
                        publicar();
                    }else{
                        iViewColegio.onErrorColegios(new ErrorApi(0,"Error al guardar datos localmente"));
                    }
                }

                @Override
                public void onError(ErrorApi error) {
                    iViewColegio.onErrorColegios(error);
                }
            });
        }else{
            iViewColegio.onSuccess(new Colegio());
        }
    }
    public void actualizar(Colegio colegio){
        new Colegios().actualizar(colegio, this);
    }

    public List<Colegio> listLocal(){
        return new Colegios().list();
    }

    public  void list(){
            new Colegios().list(this);
    }
    private   void guardarColegios(List<Colegio> lstColegios){
        List<Colegio> lstColegiosLocal = new Colegios().list();
        Colegios colegiosData = new Colegios();
        for (Colegio item : lstColegios) {
            if (lstColegiosLocal.size() > 0) {
                Colegio colegio = findColegio(lstColegiosLocal , item.IDColegio);
                if (colegio != null){
                    if (colegio.Estado == Enumerator.Estado.PENDIENTE_PUBLICAR){
                        item.Estado = Enumerator.Estado.PENDIENTE_PUBLICAR;
                        item.Latitude = colegio.Latitude;
                        item.Longitude = colegio.Longitude;
                        item.Norte = colegio.Norte;
                        item.Este = colegio.Este;
                    }
                    colegiosData.update(item);
                }else{
                    colegiosData.insert(item);
                }
            }else{
                colegiosData.insert(item);
            }
        }


    }

    private Colegio findColegio(List<Colegio> lstColegios, int idColegio){
        for (Colegio item : lstColegios){
            if (item.IDColegio == idColegio){
                return item;
            }
        }
        return  null;
    }

    @Override
    public void onSuccess(List<Colegio> lstColegios) {
        guardarColegios(lstColegios);
        iViewColegio.onSuccessColegios(lstColegios);
    }

    @Override
    public void onSuccess(Colegio colegio) {
        iViewColegio.onSuccess(colegio);
    }

    @Override
    public void onError(ErrorApi error) {
        iViewColegio.onErrorColegios(error);
    }
}
