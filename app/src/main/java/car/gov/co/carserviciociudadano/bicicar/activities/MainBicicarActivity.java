package car.gov.co.carserviciociudadano.bicicar.activities;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Server;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Beneficiarios;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Reportes;
import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.bicicar.model.Estadistica;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewReportes;
import car.gov.co.carserviciociudadano.bicicar.presenter.ReportesPresenter;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.parques.activities.WebViewActivity;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class MainBicicarActivity extends BaseActivity implements IViewReportes {

    private static final int REQUEST_CODE_BICICAR_LOGIN_RUTAS = 203;
    @BindView(R.id.lblKgCO2) TextView lblKgCO2;
    @BindView(R.id.lblBicicletas) TextView lblBicicletas;
    @BindView(R.id.lblKilometros) TextView lblKilometros;
    @BindView(R.id.lyMainBicicar) View lyMainBicicar;
    @BindView(R.id.lyMisDatos) View lyMisDatos;
    @BindView(R.id.lyUnirmeaBicicar) View lyUnirmeaBicicar;
    @BindView(R.id.progressBar)   ProgressBar progressBar;
    ReportesPresenter reportesPresenter;
    public static final int REQUEST_CODE_BICICAR_LOGIN = 202;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bicicar);
        ButterKnife.bind(this);
        reportesPresenter = new ReportesPresenter(this);
        progressBar.setVisibility(View.GONE);
        getGranTotal();

        Beneficiario beneficiario = Beneficiarios.readBeneficio();
        if (beneficiario == null){
            lyUnirmeaBicicar.setVisibility(View.VISIBLE);
            lyMisDatos.setVisibility(View.GONE);
        }else{
            lyUnirmeaBicicar.setVisibility(View.GONE);
            lyMisDatos.setVisibility(View.VISIBLE);
        }

    }
    private void getGranTotal(){
        if (Utils.isOnline(this)) {
            progressBar.setVisibility(View.VISIBLE);
            reportesPresenter.getGranTotal();
        }
    }

    @Override
    public void onPause() {
        AppCar.VolleyQueue().cancelAll(Reportes.TAG);
        super.onPause();
    }
    @Override
    public void onDestroy() {
        AppCar.VolleyQueue().cancelAll(Reportes.TAG);
        super.onDestroy();
    }

    @Override
    public void onSuccessGranTotal(Estadistica estadistica) {
        progressBar.setVisibility(View.GONE);
        if (estadistica != null){
            lblKgCO2.setText(Utils.formatoNumbero(Utils.round(0, estadistica.KGCO2)));
            lblBicicletas.setText(Utils.formatoNumbero(Utils.round(0,estadistica.Bicicletas)));
            lblKilometros.setText(Utils.formatoNumbero(Utils.round(0,estadistica.Kilometros)));
        }
    }

    @Override
    public void onSuccessEstadistica(List<Estadistica> estadistica) {

    }


    @Override
    public void onErrorGranTotal(ErrorApi errorApi) {
        progressBar.setVisibility(View.GONE);
        mostrarError(errorApi);
    }

    @Override
    public void onErrorEstadistica(ErrorApi error) {
    }

    @Override
    public void onSuccessEstadisticaMesual(List<Estadistica> estadistica) {
    }

    @Override
    public void onSuccessEstadisticaAnual(List<Estadistica> estadistica) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_BICICAR_LOGIN){
            if (resultCode == Activity.RESULT_OK){
                Intent i = new Intent(this, RegistrarActividadActivity.class);
                startActivity(i);
            }
        }else if(requestCode == REQUEST_CODE_BICICAR_LOGIN_RUTAS){
            if (resultCode == Activity.RESULT_OK) {
                Intent i = new Intent(this, CrearRutaActivity.class);
                startActivity(i);
            }
        }
    }

  /*  @OnClick({R.id.imgLogo, R.id.btnVerRecorrido}) void onImgLogo(){
        Intent i = new Intent(this, SeguirRutaActivity.class);
        startActivity(i);
    }
*/

    @OnClick(R.id.lyRegistrarActividad) void onRegistrarActividad(){

        Beneficiario beneficiario = Beneficiarios.readBeneficio();
        if (beneficiario == null){
            mostrarMensajeCrearCuenta(REQUEST_CODE_BICICAR_LOGIN);
        }else{
            Intent i = new Intent(this, RegistrarActividadActivity.class);
            startActivity(i);
        }

    }

    @OnClick(R.id.lyUnirmeaBicicar) void onUnirmeaBicicar(){
           abrirUnirmeAbicicar();
    }

    @OnClick(R.id.lyHuellaAmbiental) void onHuellaAmbiental(){
        Intent i = new Intent(this, HuellaAmbientalActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.lyCrearRuta) void onCrearRuta(){
        Beneficiario beneficiario = Beneficiarios.readBeneficio();
        if (beneficiario == null) {
            mostrarMensajeCrearCuenta(REQUEST_CODE_BICICAR_LOGIN_RUTAS);
        }else{
            Intent i = new Intent(this, CrearRutaActivity.class);
            startActivity(i);
        }
    }

    @OnClick(R.id.lyRutas) void onRutas(){
        Intent i = new Intent(this, RutasActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.btnSigueme) void onSigueme(){

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Server.ServerBICICAR()));
        startActivity(browserIntent);
    }

    @OnClick(R.id.lyMisDatos) void onMisDatos(){

        Intent i = new Intent(this, EstadisticaPersonaActivity.class);
        startActivity(i);
    }

    private void mostrarMensajeCrearCuenta( final int  requestCode){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainBicicarActivity.this);
        builder.setMessage("Debe crear una cuenta o iniciar sesión para ingresar en esta opción");

        builder.setPositiveButton("Crear cuenta", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                abrirUnirmeAbicicar();
            }
        });

        builder.setNegativeButton("Iniciar Sesión", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Intent j = new Intent(MainBicicarActivity.this, LoginBiciCarActivity.class);
                startActivityForResult(j, requestCode);
            }
        });
        builder.show();
    }

    private void abrirUnirmeAbicicar(){
        Intent i = new Intent(MainBicicarActivity.this, WebViewBicicarActivity.class);
        i.putExtra(WebViewActivity.URL, Server.ServerBICICAR() + "WebSite/paginas/BiciUsuario");
        i.putExtra(WebViewActivity.TITULO, "Registro en BiciCAR");
        startActivity(i);
    }
    private void mostrarError(ErrorApi errorApi){
        if (errorApi.getStatusCode() == 404)
            return;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(errorApi.getMessage());
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getGranTotal();

            }
        });
        builder.show();
    }
}
