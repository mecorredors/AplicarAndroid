package car.gov.co.carserviciociudadano.bicicar.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.Beneficiarios;
import car.gov.co.carserviciociudadano.bicicar.model.Beneficiario;
import car.gov.co.carserviciociudadano.bicicar.model.Bicicleta;
import car.gov.co.carserviciociudadano.bicicar.model.Estadistica;
import car.gov.co.carserviciociudadano.bicicar.presenter.BicicletaPresenter;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewBicicleta;
import car.gov.co.carserviciociudadano.bicicar.presenter.IViewReportes;
import car.gov.co.carserviciociudadano.bicicar.presenter.ReportesPresenter;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class EstadisticaPersonaActivity extends BaseActivity implements IViewReportes, IViewBicicleta {

    @BindView(R.id.lblSerial) TextView lblSerial;
    @BindView(R.id.lblRin) TextView lblRin;
    @BindView(R.id.lblColor) TextView lblColor;
    @BindView(R.id.lblNombre) TextView lblNombre;
    @BindView(R.id.lblKilometros) TextView lblKilometros;
    @BindView(R.id.lblHuellaAmbiental) TextView lblHuellaAmbiental;
    @BindView(R.id.progressEstadistica) ProgressBar progressEstadistica;
    @BindView(R.id.progressBicicleta) ProgressBar progressBicicleta;
    @BindView(R.id.rbuSemanal) RadioButton rbuSemanal;
    @BindView(R.id.rbuMensual) RadioButton rbuMensual;
    @BindView(R.id.rbuTotal) RadioButton rbuTotal;

    Beneficiario mBeneficiarioLogin;
    ReportesPresenter mReportesPresenter;
    BicicletaPresenter mBicicletaPresenter;
    List<Estadistica> mLstEstadistica = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadistica_persona);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        mBeneficiarioLogin  = Beneficiarios.readBeneficio();
        bar.setDisplayHomeAsUpEnabled(true);

        lblNombre.setText(mBeneficiarioLogin.Nombres.toUpperCase() + " " +mBeneficiarioLogin.Apellidos.toUpperCase());
        lblSerial.setText(String.valueOf(mBeneficiarioLogin.Serial));
        lblRin.setText(String.valueOf(mBeneficiarioLogin.Rin));
        //lblSerial.setText(mBeneficiarioLogin.);

        mReportesPresenter = new ReportesPresenter(this);
        mBicicletaPresenter = new BicicletaPresenter(this);
        obtenerDatos();
    }

    private void obtenerDatos(){
        progressEstadistica.setVisibility(View.VISIBLE);
        progressBicicleta.setVisibility(View.VISIBLE);
        mReportesPresenter.getEstadisticaPersona(mBeneficiarioLogin);
        mBicicletaPresenter.obtenerItem(mBeneficiarioLogin.IDBeneficiario);
    }

    private void mostrarEstadistica(){
        String tipoTotal = "SEMANA";
        if (rbuMensual.isChecked())
            tipoTotal = "MES";
        else if (rbuTotal.isChecked())
            tipoTotal = "ACUMULADO";

        lblHuellaAmbiental.setText("0");
        lblKilometros.setText("0");
        for (Estadistica item : mLstEstadistica){
            if (item.TipoTotal.equals(tipoTotal)){
                lblHuellaAmbiental.setText(String.valueOf(Utils.round(2, item.KGCO2)));
                lblKilometros.setText(String.valueOf(Utils.round(2, item.Kilometros)));
                return;
            }
        }
    }

    @OnClick(R.id.rbuSemanal) void onSemanal(){
        mostrarEstadistica();
    }
    @OnClick(R.id.rbuMensual) void onMensual(){
        mostrarEstadistica();
    }
    @OnClick(R.id.rbuTotal) void onTotal(){
        mostrarEstadistica();
    }

    @OnClick(R.id.btnHistorialTrayectos) void onHistorialTrayectos(){
        Intent i = new Intent(this, HistorialTrayectosActivity.class);
        startActivity(i);
    }
    @Override
    public void onSuccessGranTotal(Estadistica estadistica) {

    }

    @Override
    public void onSuccessEstadisticaPersona(List<Estadistica> estadistica) {
        progressEstadistica.setVisibility(View.GONE);
        mLstEstadistica.clear();
        mLstEstadistica.addAll(estadistica);
        mostrarEstadistica();
    }

    @Override
    public void onErrorGranTotal(ErrorApi errorApi) {

    }

    @Override
    public void onErrorEstadisticaPersona(ErrorApi error) {
        progressEstadistica.setVisibility(View.GONE);
        mostrarError(error);

    }

    @Override
    public void onSuccessBicicleta(Bicicleta bicicleta) {
        progressBicicleta.setVisibility(View.GONE);
        if (bicicleta != null){
            lblSerial.setText(bicicleta.Serial);
            lblRin.setText(bicicleta.TamanioRin);
            lblColor.setText(bicicleta.Color);
        }
    }

    @Override
    public void onErrorBicicleta(ErrorApi errorApi) {
        progressBicicleta.setVisibility(View.GONE);
        mostrarMensaje(errorApi.getMessage());

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
                obtenerDatos();

            }
        });
        builder.show();
    }
}
