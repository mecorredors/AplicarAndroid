package car.gov.co.carserviciociudadano.consultapublica.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.AppCar;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.consultapublica.dataaccesss.Tramites;
import car.gov.co.carserviciociudadano.consultapublica.interfaces.ITramite;
import car.gov.co.carserviciociudadano.consultapublica.model.Tramite;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.parques.activities.IntentHelper;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

public class TramitesActivity extends BaseActivity {

    @BindView(R.id.activity_tramites)   View activity_tramites;
    @BindView(R.id.lyTramite)   View lyTramite;
    @BindView(R.id.txtNumero)    EditText txtNumero;
    @BindView(R.id.lblNumero)   TextView lblNumero;
    @BindView(R.id.lblFecha)   TextView lblFecha;
    @BindView(R.id.lblRemitente)   TextView lblRemitente;
    @BindView(R.id.lblAsunto)   TextView lblAsunto;
    @BindView(R.id.lblTipoTramite)   TextView lblTipoTramite;
    @BindView(R.id.lblTema)   TextView lblTema;
    @BindView(R.id.lblAreaResponsable)   TextView lblAreaResponsable;
    @BindView(R.id.lblEstadoActual)   TextView lblEstadoActual;
    @BindView(R.id.progressView)   ProgressBar progressView;
    @BindView(R.id.btnVerRespuestas)   Button btnVerRespuestas;

    Tramite mTramite ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tramites);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        lyTramite.setVisibility(View.GONE);
    }
    @Override
    public void onPause() {
        AppCar.VolleyQueue().cancelAll(Tramites.TAG);
        super.onPause();
    }

    @OnClick(R.id.btnConsultar) void onConsultar() {
        consultar();
    }
    @OnClick(R.id.btnVerRespuestas) void onVerRespuestas() {
        if(mTramite != null) {
            IntentHelper.addObjectForKey(mTramite, Tramites.TAG);
            Intent i = new Intent(this, DocumentosRespuestaActivity.class);
            startActivity(i);
        }else{
            mostrarMensaje("No se encontraron documentos de respuesta",activity_tramites);
        }
    }
    private void consultar(){
        ocultarTeclado(activity_tramites);
        if (txtNumero.getText().length()> 0){
           showProgress(progressView,true);
            lyTramite.setVisibility(View.GONE);
            new Tramites().list(txtNumero.getText().toString(), new ITramite() {
                @Override
                public void onSuccess(Tramite tramite) {
                    showProgress(progressView,false);
                    mTramite = tramite;
                    if (tramite.getRadicadoItem().getIDTipoDocumento() != 1 ){
                        mostrarMensajeDialog(getString(R.string.no_disponible_publico));
                    }else {
                        lyTramite.setVisibility(View.VISIBLE);
                        lblNumero.setText(tramite.getRadicadoItem().getNumeroRadicado());
                        lblFecha.setText(Utils.toStringLargeFromDate(tramite.getRadicadoItem().getFechaRadicado()));
                        lblNumero.setText(tramite.getRadicadoItem().getNumeroRadicado());
                        lblRemitente.setText(Html.fromHtml(tramite.getRemitente()));
                        lblEstadoActual.setText(tramite.getRadicadoItem().getEstado());
                        lblAsunto.setText(tramite.getRadicadoItem().getAsunto());
                        if (!tramite.getRadicadoItem().getTipoTramite().isEmpty() )
                            lblTipoTramite.setText(tramite.getRadicadoItem().getTipoTramite() +" - " + tramite.getRadicadoItem().getTramiteDescripcion());
                        lblTema.setText(tramite.getRadicadoItem().getTema());
                        lblAreaResponsable.setText(tramite.getOficinaResponsable());
                        lblAreaResponsable.setText(tramite.getOficinaResponsable());

                        if (tramite.getRadicadosDigitalesRespuesta()!= null && tramite.getRadicadosDigitalesRespuesta().size() >0 ){
                            btnVerRespuestas.setVisibility(View.VISIBLE);
                        }else {
                            btnVerRespuestas.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onError(ErrorApi error) {
                    showProgress(progressView,false);
                    mostrarMensajeDialog(error.getMessage());
                }
            });

        }else{
            mostrarMensaje(getString(R.string.ingrese_numero_radicado),activity_tramites);
        }
    }
}
