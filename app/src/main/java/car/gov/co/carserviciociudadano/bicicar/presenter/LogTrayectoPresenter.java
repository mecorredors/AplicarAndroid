package car.gov.co.carserviciociudadano.bicicar.presenter;


import java.util.Calendar;
import java.util.List;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.SexaDecimalCoordinate;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Beneficiarios;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Eventos;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.LogTrayectos;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.TiposEvento;
import car.gov.co.carserviciociudadano.bicicar.interfaces.ILogTrayecto;
import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.bicicar.model.Evento;
import car.gov.co.carserviciociudadano.bicicar.model.LogTrayecto;
import car.gov.co.carserviciociudadano.bicicar.model.TipoEvento;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;


public class LogTrayectoPresenter {
    private IViewLogTrayecto iViewLogTrayecto;

    public  LogTrayectoPresenter(IViewLogTrayecto iViewLogTrayecto){
        this.iViewLogTrayecto = iViewLogTrayecto;
    }


    /**
     * Actualiza todos los trayectos marcados en asistencia con los datos de recorrido realizados por el pedagogo
     * @param idEvento
     */
    public void actualizarDistanciaConEvento(int idEvento){
        List<LogTrayecto> logTrayectoList = new LogTrayectos().List(Enumerator.Estado.PENDIENTE_PUBLICAR, 0,0, idEvento);
        Evento evento = new Eventos().read(idEvento);
        LogTrayectos logTrayectosData = new LogTrayectos();
        if (evento != null){
            for (LogTrayecto item : logTrayectoList){
                item.DistanciaKm = evento.DistanciaKm;
                item.DuracionMinutos = evento.DuracionMinutos;
                logTrayectosData.Update(item);
            }
        }

    }

    /**
     * Actualiza todos los trayectos marcados en asistencia con los datos de recorrido realizados por el pedagogo
     * @param idBeneficiario
     * @param idEvento
     */
    public void actualizarConRecorridoPedagogo(int idBeneficiario, int idEvento){
        List<LogTrayecto> logTrayectoList = new LogTrayectos().List(Enumerator.Estado.PENDIENTE_PUBLICAR, 0, 0, idEvento);
        List<LogTrayecto> logTrayectoPedagogoList = new LogTrayectos().List(Enumerator.Estado.TODOS, 0 , idBeneficiario, idEvento);
        LogTrayectos logTrayectosData = new LogTrayectos();
        if (logTrayectoPedagogoList.size() > 0){
            LogTrayecto logTrayectoPedagogo = logTrayectoPedagogoList.get(0);
            for (LogTrayecto item : logTrayectoList){
                item.DistanciaKm = logTrayectoPedagogo.DistanciaKm;
                item.DuracionMinutos = logTrayectoPedagogo.DuracionMinutos;
                item.LatitudePuntoA = logTrayectoPedagogo.LatitudePuntoA;
                item.LongitudePuntoA = logTrayectoPedagogo.LongitudePuntoA;
                item.LatitudePuntoB  = logTrayectoPedagogo.LatitudePuntoB;
                item.NortePuntoA = logTrayectoPedagogo.NortePuntoA;
                item.EstePuntoA = logTrayectoPedagogo.EstePuntoA;
                item.NortePuntoB = logTrayectoPedagogo.NortePuntoB;
                item.EstePuntoB = logTrayectoPedagogo.EstePuntoB;
                item.Ruta = logTrayectoPedagogo.Ruta;
                logTrayectosData.Update(item);
            }
        }

    }


    public void publicar(final int idBeneficiario){
        publicar(idBeneficiario, 0);
    }
    public void publicar(final int idBeneficiario, final int  idEvento){
        List<LogTrayecto> logTrayectoList;
        if (idEvento > 0){
            logTrayectoList = new LogTrayectos().List(Enumerator.Estado.PENDIENTE_PUBLICAR, 0, 0, idEvento);
        }else {
            logTrayectoList = new LogTrayectos().List(Enumerator.Estado.PENDIENTE_PUBLICAR, idBeneficiario);
        }
        if (logTrayectoList.size() > 0) {
            LogTrayecto logTrayecto = logTrayectoList.get(0);
            new LogTrayectos().publicar(logTrayecto , new ILogTrayecto() {
                @Override
                public void onSuccessLogTrayecto(LogTrayecto logTrayecto) {
                   logTrayecto.Estado = Enumerator.Estado.PUBLICADO;
                   if ( new LogTrayectos().Update(logTrayecto))
                        publicar(idBeneficiario, idEvento);
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

    public static LogTrayecto agregarMiRecorrido(float distancia, float minutos, String ruta, double latitudePuntoA, double longitudePuntoA, double latitudePuntoB, double longitudePuntoB, Evento evento) {
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

            if (evento != null){
                TipoEvento tipoEvento = new TiposEvento().read(evento.IDTipoEvento);
                if (tipoEvento != null && tipoEvento.Recorrido){
                    logTrayecto.IDEvento = evento.IDEvento;
                    List<LogTrayecto> lstLogTrayectosEvento = new LogTrayectos().List(Enumerator.Estado.TODOS, 0 ,mBeneficiarioLogin.IDBeneficiario, evento.IDEvento);
                    if (lstLogTrayectosEvento.size() > 0){ // eliminamos en caso que ya haya una previo
                        new LogTrayectos().Delete(lstLogTrayectosEvento.get(0).IDLogTrayecto);
                    }
                }
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
