package car.gov.co.carserviciociudadano.bicicar.presenter;

import android.content.Context;

import java.util.Calendar;
import java.util.List;

import car.gov.co.carserviciociudadano.AppCar;
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

    public  BeneficiarioPresenter(IViewBeneficiario iViewBeneficiario){
        this.iViewBeneficiario = iViewBeneficiario;
    }

    public  void login(String numeroId, String claveApp){
        new Beneficiarios().login(numeroId, claveApp, this);
    }

    public  void obtenerItem(String serial, String rin){
        new Beneficiarios().obtenerItem(serial, rin, this);
    }
    public  void listarItems(String curso){

        if (Utils.isOnline(AppCar.getContext()))
             new Beneficiarios().listarItems(curso, this);
        else{
            listarItemsLocal();
        }
    }

    private  void listarItemsLocal(){
        List<Beneficiario> lstBeneficiarios = new Beneficiarios().List();
        if (lstBeneficiarios.size() == 0){
            onErrorListarItems(new ErrorApi(500, "No se encontrarón estudiantes, necesita internet para obtener los estudiantes del curso "));
        }else{
            onSuccess(lstBeneficiarios, false);
        }
    }

    public void GuardarLogTrayecto(Beneficiario beneficiario, Beneficiario beneficiarioLogin){
        LogTrayecto logTrayecto = new LogTrayecto();

        logTrayecto.Nombre = beneficiario.Nombres + " " + beneficiario.Apellidos;
        logTrayecto.Estado = Enumerator.BicicarLogTrayecto.PENDIENTE_PUBLICAR;

        //Calendar ca = Calendar.getInstance();
        //ca.add(Calendar.DATE , -1);

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
            beneficiarios.DeleteAll();
            for (Beneficiario item : lstBeneficiarios){
               boolean res = beneficiarios.Insert(item);
            }
        }

        procesar(lstBeneficiarios);
        iViewBeneficiario.onSuccess(lstBeneficiarios);
    }

    @Override
    public void onError(ErrorApi error) {
        iViewBeneficiario.onError(error);
    }

    @Override
    public void onErrorListarItems(ErrorApi error) {
        if (error.getStatusCode() == 404)
            iViewBeneficiario.onErrorListarItems(error);
        else
            listarItemsLocal();
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
        List<LogTrayecto> items = new LogTrayectos().List(Enumerator.BicicarLogTrayecto.PENDIENTE_PUBLICAR, beneficiarioLogin.IDBeneficiario);

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