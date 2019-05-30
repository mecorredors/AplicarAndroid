package car.gov.co.carserviciociudadano.bicicar.presenter;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Beneficiarios;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.LogTrayectos;
import car.gov.co.carserviciociudadano.bicicar.interfaces.IBeneficiario;
import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.bicicar.model.LogTrayecto;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class BeneficiarioPresenter implements IBeneficiario {
    IViewBeneficiario iViewBeneficiario;
    List<Beneficiario> mBeneficiariosLocal = new ArrayList<>();
    public  BeneficiarioPresenter(IViewBeneficiario iViewBeneficiario){
        this.iViewBeneficiario = iViewBeneficiario;
    }

    public void publicarBeneficiarioLogin(Beneficiario beneficiario){
        if (beneficiario.Estado == Enumerator.Estado.PENDIENTE_PUBLICAR) {
            new Beneficiarios().actualizar(beneficiario, new IBeneficiario() {
                @Override
                public void onSuccess(Beneficiario beneficiario) {
                    beneficiario.Estado = Enumerator.Estado.PUBLICADO;
                    beneficiario.guardar();
                    iViewBeneficiario.onSuccess(new Beneficiario());
                }

                @Override
                public void onSuccess(List<Beneficiario> lstBeneficiarios, boolean datosServer) {

                }

                @Override
                public void onError(ErrorApi error) {
                    iViewBeneficiario.onError(error);
                }

                @Override
                public void onErrorListarItems(ErrorApi error) {

                }

                @Override
                public void onSuccessRecordarClave(String mensaje) {

                }

                @Override
                public void onErrorRecordarClave(ErrorApi error) {

                }
            });
        }else{
            iViewBeneficiario.onError(new ErrorApi(0,"Datos ya publicados"));
        }
    }

    public void publicar(){
        final Beneficiarios beneficiariosData = new Beneficiarios();
        String where =  Beneficiario.ESTADO + "= " + Enumerator.Estado.PENDIENTE_PUBLICAR;
        List<Beneficiario> lstBeneficiarios = beneficiariosData.List(where);
        if (lstBeneficiarios.size() > 0){
            Beneficiario item = lstBeneficiarios.get(0);
            beneficiariosData.actualizar(item, new IBeneficiario() {
                @Override
                public void onSuccess(Beneficiario beneficiario) {
                    beneficiario.Estado = Enumerator.Estado.PUBLICADO;
                    if  (beneficiariosData.Update(beneficiario)) {
                        publicar();
                    }else{
                        iViewBeneficiario.onError(new ErrorApi(0,"Error al guardar datos localmente"));
                    }
                }

                @Override
                public void onSuccess(List<Beneficiario> lstBeneficiarios, boolean datosServer) {

                }

                @Override
                public void onError(ErrorApi error) {
                    iViewBeneficiario.onError(error);
                }

                @Override
                public void onErrorListarItems(ErrorApi error) {

                }

                @Override
                public void onSuccessRecordarClave(String mensaje) {

                }

                @Override
                public void onErrorRecordarClave(ErrorApi error) {

                }
            });


        }else{


            iViewBeneficiario.onSuccess(new Beneficiario());
        }

    }

    public  void login(String numeroId, String claveApp){
        new Beneficiarios().login(numeroId, claveApp, this);
    }

    public  void obtenerItem(String serial, String rin){
        new Beneficiarios().obtenerItem(serial, rin, this);
    }


    public  void list( int idColegio){
        mBeneficiariosLocal = listLocal(idColegio);
        list(null, idColegio);
    }

    public  void list(String curso, int idColegio){
        mBeneficiariosLocal = listLocal(curso, idColegio);
        new Beneficiarios().listarItems(curso, idColegio, this);
    }

    public   List<Beneficiario> listLocal(int idColegio){
        return new Beneficiarios().List(idColegio);
    }

    public   List<Beneficiario> listLocal(String curso, int idColegio){
        return new Beneficiarios().List(curso, idColegio);
    }


    public void GuardarLogTrayecto(Beneficiario beneficiario, Beneficiario beneficiarioLogin){
        LogTrayecto logTrayecto = new LogTrayecto();

        logTrayecto.Nombre = beneficiario.Nombres + " " + beneficiario.Apellidos;
        logTrayecto.Estado = Enumerator.Estado.PENDIENTE_PUBLICAR;

        logTrayecto.Fecha =  Calendar.getInstance().getTime();
        logTrayecto.IDBeneficiario = beneficiario.IDBeneficiario;
        logTrayecto.IDBicicleta = beneficiario.IDBicicleta;
        logTrayecto.DistanciaKm = beneficiario.DistanciaKm;

        if (beneficiarioLogin != null)
            logTrayecto.IDBeneficiarioRegistro = beneficiarioLogin.IDBeneficiario;

        new LogTrayectos().Insert(logTrayecto);
    }

    public  void recordarClave(String numeroId, String email){
        Beneficiario beneficiario = new Beneficiario();
        beneficiario.NumeroID = numeroId;
        beneficiario.Email = email;

        new Beneficiarios().recordarClave(beneficiario, this);
    }


    @Override
    public void onSuccess(Beneficiario beneficiario) {
        iViewBeneficiario.onSuccess(beneficiario);
    }

    @Override
    public void onSuccess(List<Beneficiario> lstBeneficiarios, boolean datosServer) {

        if (datosServer){
            Beneficiarios beneficiarios = new Beneficiarios();
            for (Beneficiario item : lstBeneficiarios) {
                if (mBeneficiariosLocal.size() > 0) {
                    Beneficiario beneficiario = findBeneficiario(mBeneficiariosLocal , item.IDBeneficiario);
                    if (beneficiario != null){
                        if (beneficiario.Estado == Enumerator.Estado.PENDIENTE_PUBLICAR){
                            item.Estado = Enumerator.Estado.PENDIENTE_PUBLICAR;
                            item.Latitude = beneficiario.Latitude;
                            item.Longitude = beneficiario.Longitude;
                            item.Norte = beneficiario.Norte;
                            item.Este = beneficiario.Este;
                        }
                        beneficiarios.Update(item);
                    }else{
                        beneficiarios.Insert(item);
                    }
                }else{
                    beneficiarios.Insert(item);
                }
            }
        }

        procesar(lstBeneficiarios);
        iViewBeneficiario.onSuccess(lstBeneficiarios);
    }

    private Beneficiario findBeneficiario(List<Beneficiario> lstBeneficiarios, int idBeneficiario){
        for (Beneficiario item : lstBeneficiarios){
            if (item.IDBeneficiario == idBeneficiario){
                return item;
            }
        }
        return  null;
    }

    @Override
    public void onError(ErrorApi error) {
        iViewBeneficiario.onError(error);
    }

    @Override
    public void onErrorListarItems(ErrorApi error) {
      //  if (error.getStatusCode() == 404)
        iViewBeneficiario.onErrorListarItems(error);
       // else
         //   listarItemsLocal();
    }

    @Override
    public void onSuccessRecordarClave(String mensaje) {
        iViewBeneficiario.onSuccessRecordarClave(mensaje);

    }

    @Override
    public void onErrorRecordarClave(ErrorApi error) {
        iViewBeneficiario.onErrorRecordarClave(error);
    }


    private void  procesar(List<Beneficiario> lstBeneficiarios) {
        Beneficiario beneficiarioLogin =  Beneficiarios.readBeneficio();
        List<LogTrayecto> items = new LogTrayectos().List(Enumerator.Estado.PENDIENTE_PUBLICAR, beneficiarioLogin.IDBeneficiario);

        Calendar fechaActual = Calendar.getInstance();

        for (Beneficiario beneficiario : lstBeneficiarios) {
            for (LogTrayecto item : items) {
                Calendar fechaItem = Utils.convertToCalendar(item.Fecha);
                if ( Utils.isEqualsDate(fechaItem, fechaActual)){
                    if (item.IDBeneficiario == beneficiario.IDBeneficiario) {
                        beneficiario.Enabled = false;
                        break;
                    }
                }
            }
        }
    }
}