package car.gov.co.carserviciociudadano.bicicar.presenter;

import java.util.List;

import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.LogTrayectos;
import car.gov.co.carserviciociudadano.bicicar.interfaces.ILogTrayecto;
import car.gov.co.carserviciociudadano.bicicar.model.LogTrayecto;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;


public class LogTrayectoPresenter {
    private IViewLogTrayecto iViewLogTrayecto;

    public  LogTrayectoPresenter(IViewLogTrayecto iViewLogTrayecto){
        this.iViewLogTrayecto = iViewLogTrayecto;
    }

    public void publicar(final int idBeneficioRegistro){

        List<LogTrayecto> logTrayectoList = new LogTrayectos().List(Enumerator.BicicarLogTrayecto.PENDIENTE_PUBLICAR);
        if (logTrayectoList.size() > 0) {
            LogTrayecto logTrayecto = logTrayectoList.get(0);
            logTrayecto.IDBeneficiarioRegistro = idBeneficioRegistro;
            new LogTrayectos().publicar(logTrayecto , new ILogTrayecto() {
                @Override
                public void onSuccessLogTrayecto(LogTrayecto logTrayecto) {
                    logTrayecto.Estado = Enumerator.BicicarLogTrayecto.PUBLICADO;
                   if ( new LogTrayectos().Update(logTrayecto))
                        publicar(idBeneficioRegistro);
                   else
                       iViewLogTrayecto.onErrorLogTrayecto(new ErrorApi(0,"Error al guardar datos localmente"));
                }

                @Override
                public void onErrorLogTrayecto(ErrorApi error) {
                    iViewLogTrayecto.onErrorLogTrayecto(error);
                }
            });
        }else{
            iViewLogTrayecto.onSuccessLogTrayecto();
        }
    }
}
