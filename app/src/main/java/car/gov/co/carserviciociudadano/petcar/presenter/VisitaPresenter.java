package car.gov.co.carserviciociudadano.petcar.presenter;

import java.util.List;

import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.petcar.dataaccess.Gestores;
import car.gov.co.carserviciociudadano.petcar.dataaccess.Visitas;
import car.gov.co.carserviciociudadano.petcar.interfaces.IVisita;
import car.gov.co.carserviciociudadano.petcar.model.Gestor;
import car.gov.co.carserviciociudadano.petcar.model.Visita;

public class VisitaPresenter {
    IViewVisita iViewVisita;

    public VisitaPresenter(IViewVisita iViewVisita){
        this.iViewVisita = iViewVisita;
    }


    public void publicar(){
        Gestor gestor = new Gestores().getLogin();
        if (gestor == null){
            iViewVisita.onErrorValidacion("No se encontró gestor, inicie sesión");
            return;
        }

        List<Visita> lstVistas = new Visitas().list(Enumerator.Estado.PENDIENTE_PUBLICAR);

        if (lstVistas.size() == 0){
            iViewVisita.onSuccessPublicarVisita();
            return;
        }

        for (Visita item : lstVistas){
            if (item.Comentarios.equals("")) {
                iViewVisita.onErrorValidacion("Ingrese los comentarios  de la visita");
                return;
            }
        }

        publicar(gestor);
    }

    public void publicar(Gestor gestor) {

        List<Visita> lstVistas = new Visitas().list(Enumerator.Estado.PENDIENTE_PUBLICAR);


        final Visitas visitasData = new Visitas();
        if (lstVistas.size() > 0) {
            Visita item = lstVistas.get(0);
            item.UsuarioCreacion = gestor.Identificacion;
            item.IDGestor = gestor.IDGestor;

            visitasData.publicar(item, new IVisita() {
                @Override
                public void onSuccessPublicarVisita(Visita visita) {
                    visita.Estado = Enumerator.Estado.PENDIENTE_PUBLICAR_FOTOS;

                    if (visitasData.guardar(visita)){
                        publicar(gestor);
                    }else{
                        iViewVisita.onErrorPublicarVisita(new ErrorApi(0, "Visita publicado pero error al guardar localmente"));
                    }

                }

                @Override
                public void onErrorPublicarVisita(ErrorApi error) {
                    iViewVisita.onErrorPublicarVisita(error);
                }
            });

        }else{
            iViewVisita.onSuccessPublicarVisita();
        }
    }

}
