package car.gov.co.carserviciociudadano.bicicar.presenter;

import android.util.Log;

import java.util.Calendar;
import java.util.List;

import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.SexaDecimalCoordinate;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Beneficiarios;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.LogTrayectos;
import car.gov.co.carserviciociudadano.bicicar.interfaces.ILogTrayecto;
import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.bicicar.model.LogTrayecto;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;


public class LogTrayectoPresenter {
    private IViewLogTrayecto iViewLogTrayecto;

    public  LogTrayectoPresenter(IViewLogTrayecto iViewLogTrayecto){
        this.iViewLogTrayecto = iViewLogTrayecto;
    }

    public void publicar(final int idBeneficiario){

        List<LogTrayecto> logTrayectoList = new LogTrayectos().List(Enumerator.Estado.PENDIENTE_PUBLICAR, idBeneficiario);
        if (logTrayectoList.size() > 0) {
            LogTrayecto logTrayecto = logTrayectoList.get(0);
            new LogTrayectos().publicar(logTrayecto , new ILogTrayecto() {
                @Override
                public void onSuccessLogTrayecto(LogTrayecto logTrayecto) {
                   logTrayecto.Estado = Enumerator.Estado.PUBLICADO;
                   if ( new LogTrayectos().Update(logTrayecto))
                        publicar(idBeneficiario);
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

    public static LogTrayecto agregarMiRecorrido(float distancia, float minutos, String ruta, double latitudePuntoA, double longitudePuntoA, double latitudePuntoB, double longitudePuntoB) {
        Beneficiario mBeneficiarioLogin =  Beneficiarios.readBeneficio();
        if (distancia > 0) {
            LogTrayecto logTrayecto = new LogTrayecto();

            logTrayecto.Estado = Enumerator.Estado.PENDIENTE_PUBLICAR;
            logTrayecto.Fecha = Calendar.getInstance().getTime();

            logTrayecto.DistanciaKm = distancia;
            logTrayecto.DuracionMinutos = minutos;

            logTrayecto.IDBeneficiario = mBeneficiarioLogin.IDBeneficiario;
            logTrayecto.IDBeneficiarioRegistro = mBeneficiarioLogin.IDBeneficiario;
            logTrayecto.Ruta = ruta;
            logTrayecto.LatitudePuntoA = latitudePuntoA;
            logTrayecto.LongitudePuntoA = longitudePuntoA;
            logTrayecto.LatitudePuntoB = latitudePuntoB;
            logTrayecto.LongitudePuntoB = longitudePuntoB;

            if (latitudePuntoA != 0 && longitudePuntoA != 0) {
                SexaDecimalCoordinate sexaDecimalCoordinate = new SexaDecimalCoordinate(latitudePuntoA, longitudePuntoA);
                sexaDecimalCoordinate.ConvertToFlatCoordinate();
                logTrayecto.NortePuntoA = sexaDecimalCoordinate.get_coorPlanaNorteFinal();
                logTrayecto.EstePuntoA = sexaDecimalCoordinate.get_coorPlanaEsteFinal();
            }

            if (latitudePuntoB != 0 && longitudePuntoB != 0) {
                SexaDecimalCoordinate sexaDecimalCoordinate = new SexaDecimalCoordinate(latitudePuntoB, longitudePuntoB);
                sexaDecimalCoordinate.ConvertToFlatCoordinate();
                logTrayecto.NortePuntoB = sexaDecimalCoordinate.get_coorPlanaNorteFinal();
                logTrayecto.EstePuntoB = sexaDecimalCoordinate.get_coorPlanaEsteFinal();
            }

            if (new LogTrayectos().Insert(logTrayecto)){
                return  logTrayecto;
            }


           // if (new LogTrayectos().Insert(logTrayecto)) {
             //   obtenerItemsActividad();
              //  if (ruta != null && !ruta.isEmpty())
                //    verRutaMapa(logTrayecto);
          //  }

        }
        return  null;
    }
}
