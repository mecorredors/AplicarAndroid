package car.gov.co.carserviciociudadano.bicicar.presenter;

import java.util.List;

import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Rutas;
import car.gov.co.carserviciociudadano.bicicar.interfaces.IRuta;
import car.gov.co.carserviciociudadano.bicicar.model.Ruta;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class RutasPresenter implements IRuta {
    IViewRutas iViewRutas;

    public RutasPresenter(IViewRutas iViewRutas){
        this.iViewRutas = iViewRutas;
    }

    public void  getRutas(int idBeneficiario){
        new Rutas().getRutas(idBeneficiario, this);
    }


    public void publicar(final int idBeneficiario){

        List<Ruta> listRutas = new Rutas().List(Enumerator.Estado.PENDIENTE_PUBLICAR, idBeneficiario);
        if (listRutas.size() > 0) {
            Ruta ruta = listRutas.get(0);
            new Rutas().publicar(ruta, new IRuta() {
                @Override
                public void onSuccess(List<Ruta> lstRutas) {

                }

                @Override
                public void onSuccess(Ruta ruta) {
                    ruta.Estado = Enumerator.Estado.PUBLICADO;
                    if ( new Rutas().Update(ruta))
                        publicar(idBeneficiario);
                    else
                        iViewRutas.onError(new ErrorApi(0,"Error al guardar datos localmente"));
                }

                @Override
                public void onError(ErrorApi errorApi) {
                    iViewRutas.onError(errorApi);
                }
            });
        }else{
            iViewRutas.onSuccess();
        }
    }

    public  void publicarRutas(Ruta ruta){
        new Rutas().publicar(ruta, this);
    }

    public void guardarRuta(Ruta ruta){
        if (ruta.Id > 0){
            new Rutas().Update(ruta);
        }else{
            new Rutas().Insert(ruta);
        }
    }

    @Override
    public void onSuccess(List<Ruta> lstRutas) {
        iViewRutas.onSuccess(lstRutas);
    }

    @Override
    public void onSuccess(Ruta ruta) {

    }

    @Override
    public void onError(ErrorApi errorApi) {
        iViewRutas.onError(errorApi);
    }
}
