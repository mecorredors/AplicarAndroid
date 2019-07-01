package car.gov.co.carserviciociudadano.bicicar.presenter;

import java.util.List;

import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Asistentes;
import car.gov.co.carserviciociudadano.bicicar.interfaces.IAsistente;
import car.gov.co.carserviciociudadano.bicicar.model.Asistente;
import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class AsistentesPresenter  {
    IViewAsistente iViewAsistente;

    public AsistentesPresenter(IViewAsistente iViewAsistente){
        this.iViewAsistente = iViewAsistente;
    }

    public void publicar(final int idEvento) {
        final Asistentes asistentesData = new Asistentes();

        List<Asistente> lstAsistentes = asistentesData.list(idEvento, Enumerator.Estado.PENDIENTE_PUBLICAR);
        if (lstAsistentes.size() > 0) {
            Asistente item = lstAsistentes.get(0);
            item.UsuarioCreacion = "Android";
            asistentesData.publicar(item, new IAsistente() {

                @Override
                public void onSuccess(Asistente asistente) {
                    asistente.Estado = Enumerator.Estado.PUBLICADO;
                    if (asistentesData.update(asistente)){
                        publicar(idEvento);
                    }else{
                        iViewAsistente.onErrorAsistente(new ErrorApi(0,"Error al guardar datos localmente"));
                    }
                }

                @Override
                public void onError(ErrorApi error) {
                    iViewAsistente.onErrorAsistente(error);
                }

            });
        }else{
            iViewAsistente.onSuccess(idEvento);
        }
    }

    public void insert(Asistente asistente){
        new Asistentes().insert(asistente);
    }

    public void  desabilitarYaRegistrados(List<Beneficiario> lstBeneficiarios, int idEvento) {
        List<Asistente> items = new Asistentes().list(idEvento);

        for (Beneficiario beneficiario : lstBeneficiarios) {
            for (Asistente item : items) {
                if (item.IDBeneficiario == beneficiario.IDBeneficiario) {
                    beneficiario.Enabled = false;
                    beneficiario.Selected = true;
                    break;
                }
            }
        }

    }

}
