package car.gov.co.carserviciociudadano.bicicar.activities;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
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

    @BindView(R.id.lblKgCO2) TextView lblKgCO2;
    @BindView(R.id.lblBicicletas) TextView lblBicicletas;
    @BindView(R.id.lblKilometros) TextView lblKilometros;
    @BindView(R.id.lyMainBicicar) View lyMainBicicar;
    @BindView(R.id.progressBar)   ProgressBar progressBar;
    ReportesPresenter reportesPresenter;
    public static final int REQUEST_CODE_BICICAR_LOGIN = 202;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bicicar);
        ButterKnife.bind(this);
        reportesPresenter = new ReportesPresenter(this);
        getGranTotal();

    }
    private void getGranTotal(){
        progressBar.setVisibility(View.VISIBLE);
        reportesPresenter.getGranTotal();
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
            lblKgCO2.setText(String.valueOf(Utils.round(2, estadistica.KGCO2)));
            lblBicicletas.setText(String.valueOf(Utils.round(2,estadistica.Bicicletas)));
            lblKilometros.setText(String.valueOf(Utils.round(2,estadistica.Kilometros)));
        }
    }

    @Override
    public void onSuccessEstadisticaPersona(List<Estadistica> estadistica) {

    }

    @Override
    public void onErrorGranTotal(ErrorApi errorApi) {
        progressBar.setVisibility(View.GONE);
        Snackbar.make(lyMainBicicar, errorApi.getMessage(), Snackbar.LENGTH_INDEFINITE)
                //.setActionTextColor(Color.CYAN)
                .setActionTextColor(ContextCompat.getColor(this, R.color.green) )
                .setAction("REINTENTAR", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getGranTotal();
                    }
                })
                .show();
    }

    @Override
    public void onErrorEstadisticaPersona(ErrorApi error) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_BICICAR_LOGIN){
            if (resultCode == Activity.RESULT_OK){
                Intent i = new Intent(this, RegistrarActividadActivity.class);
                startActivity(i);
            }
        }
    }

    @OnClick(R.id.lyRegistrarActividad) void onRegistrarActividad(){
        Beneficiario beneficiario = Beneficiarios.readBeneficio();
        if (beneficiario == null){
            Intent i = new Intent(this, LoginBiciCarActivity.class);
            startActivityForResult(i, REQUEST_CODE_BICICAR_LOGIN);
        }else{
            Intent i = new Intent(this, RegistrarActividadActivity.class);
            startActivity(i);
        }

    }

    @OnClick(R.id.lyUnirmeaBicicar) void onUnivermeaBicicar(){
            Intent i = new Intent(this, WebViewBicicarActivity.class);
            i.putExtra(WebViewActivity.URL, Server.ServerBICICAR() + "Modulos/Inscripcion/Registro");
            i.putExtra(WebViewActivity.TITULO, "Registro en BiciCAR");
            startActivity(i);
    }

    @OnClick(R.id.lyHuellaAmbiental) void onHuellaAmbiental(){
        Intent i = new Intent(this, WebViewBicicarActivity.class);
        i.putExtra(WebViewActivity.URL, Server.ServerBICICAR() + "HuellaAmbiental");
        i.putExtra(WebViewActivity.TITULO, "Huella ambiental");
        startActivity(i);
    }

    @OnClick(R.id.btnSigueme) void onSigueme(){

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Server.ServerBICICAR()));
        startActivity(browserIntent);
    }
}
