package car.gov.co.carserviciociudadano.bicicar.presenter;

import java.util.List;

import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Reportes;
import car.gov.co.carserviciociudadano.bicicar.interfaces.IReportes;
import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.bicicar.model.Estadistica;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class ReportesPresenter implements IReportes {
    IViewReportes iViewReportes;

    public ReportesPresenter(IViewReportes iViewReportes){
        this.iViewReportes = iViewReportes;
    }

    public  void getGranTotal(){
        new Reportes().getGranTotal(this);
    }

    public void getEstadisticaPersona(Beneficiario beneficiario){

        int idLiderGrupo = 0;
        int idUsuario = 0;

        if (beneficiario.IDPerfil == Enumerator.BicicarPerfil.PEDAGOGO || beneficiario.IDPerfil == Enumerator.BicicarPerfil.BENEFICIARIO_APP){
            idUsuario = beneficiario.IDBeneficiario;
        }else{
            idLiderGrupo = beneficiario.IDBeneficiario;
        }

        new Reportes().getEstadisticaPersona(idLiderGrupo, idUsuario, this);

    }

    @Override
    public void onSuccessGranTotal(Estadistica estadistica) {
        iViewReportes.onSuccessGranTotal(estadistica);
    }

    @Override
    public void onSuccessEstadisticaPersona(List<Estadistica> estadistica) {
        iViewReportes.onSuccessEstadisticaPersona(estadistica);
    }

    @Override
    public void onErrorGranTotal(ErrorApi error) {
        iViewReportes.onErrorGranTotal(error);
    }
    @Override
    public void onErrorEstadisticaPersona(ErrorApi error) {
        iViewReportes.onErrorEstadisticaPersona(error);
    }
}
